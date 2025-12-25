package com.astronix1.socialapp.service

import com.astronix1.socialapp.dto.TagDto
import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.Tag
import com.astronix1.socialapp.entity.User
import com.astronix1.socialapp.enumeration.NotificationType
import com.astronix1.socialapp.exception.*
import com.astronix1.socialapp.repository.PostRepository
import com.astronix1.socialapp.response.PostResponse
import com.astronix1.socialapp.util.FileNamingUtil
import com.astronix1.socialapp.util.FileUploadUtil
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime

@Service
@Transactional
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val commentService: CommentService,
    private val tagService: TagService,
    private val notificationService: NotificationService,
    private val environment: Environment,
    private val fileNamingUtil: FileNamingUtil,
    private val fileUploadUtil: FileUploadUtil
) : PostService {

    override fun getPostById(postId: Long): Post =
        postRepository.findById(postId).orElseThrow { PostNotFoundException() }

    override fun getPostResponseById(postId: Long): PostResponse {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postId)
        return PostResponse(post, post.likeList.contains(authUser))
    }

    override fun getTimelinePostsPaginate(page: Int, size: Int): List<PostResponse> {
        val authUser = userService.getAuthenticatedUser()
        val users = authUser.followingUsers.toMutableList()
        users.add(authUser)

        val ids = users.mapNotNull { it.id }

        return postRepository.findPostsByAuthorIdIn(
            ids,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        ).map { postToResponse(it) }
    }

    override fun getPostSharesPaginate(sharedPost: Post, page: Int, size: Int): List<PostResponse> =
        postRepository.findPostsBySharedPost(
            sharedPost,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        ).map { postToResponse(it) }

    override fun getPostByTagPaginate(tag: Tag, page: Int, size: Int): List<PostResponse> =
        postRepository.findPostsByPostTags(
            tag,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        ).map { postToResponse(it) }

    override fun getPostsByUserPaginate(author: User, page: Int, size: Int): List<PostResponse> =
        postRepository.findPostsByAuthor(
            author,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        ).map { postToResponse(it) }

    override fun createNewPost(
        content: String?,
        postPhoto: MultipartFile?,
        postTags: List<TagDto>?
    ): Post {
        val authUser = userService.getAuthenticatedUser()

        val post = Post(
            content = content,
            author = authUser,
            likeCount = 0,
            shareCount = 0,
            commentCount = 0,
            isTypeShare = false,
            sharedPost = null,
            dateCreated = LocalDateTime.now(),
            dateLastModified = LocalDateTime.now()
        )

        postPhoto?.takeIf { it.size > 0 }?.let {
            val uploadDir = environment.getProperty("upload.post.images")!!
            val fileName = fileNamingUtil.nameFile(it)
            val url =
                "${environment.getProperty("app.root.backend")}${File.separator}$uploadDir${File.separator}$fileName"
            post.postPhoto = url
            fileUploadUtil.saveNewFile(uploadDir, fileName, it)
        }

        postTags?.forEach { dto ->
            val tag = try {
                tagService.increaseTagUseCounter(dto.tagName)
            } catch (e: TagNotFoundException) {
                tagService.createNewTag(dto.tagName)
            }
            post.postTags.add(tag)
        }

        return postRepository.save(post)
    }

    override fun updatePost(
        postId: Long,
        content: String?,
        postPhoto: MultipartFile?,
        postTags: List<TagDto>?
    ): Post {
        val post = getPostById(postId)

        if (!content.isNullOrBlank()) post.content = content

        postPhoto?.takeIf { it.size > 0 }?.let {
            val uploadDir = environment.getProperty("upload.post.images")!!
            val old = getPhotoNameFromUrl(post.postPhoto)
            val newName = fileNamingUtil.nameFile(it)
            val url =
                "${environment.getProperty("app.root.backend")}${File.separator}$uploadDir${File.separator}$newName"
            post.postPhoto = url

            if (old == null)
                fileUploadUtil.saveNewFile(uploadDir, newName, it)
            else
                fileUploadUtil.updateFile(uploadDir, old, newName, it)
        }

        postTags?.forEach { dto ->
            val tag = try {
                tagService.getTagByName(dto.tagName)
            } catch (e: TagNotFoundException) {
                tagService.createNewTag(dto.tagName)
            }

            when (dto.action.lowercase()) {
                "add" -> {
                    post.postTags.add(tag)
                    tagService.increaseTagUseCounter(dto.tagName)
                }

                "remove" -> {
                    post.postTags.remove(tag)
                    tagService.decreaseTagUseCounter(dto.tagName)
                }
            }
        }

        post.dateLastModified = LocalDateTime.now()
        return postRepository.save(post)
    }

    override fun deletePost(postId: Long) {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postId)

        if (post.author != authUser) throw InvalidOperationException()

        post.shareList.forEach {
            it.sharedPost = null
            postRepository.save(it)
        }

        notificationService.deleteNotificationByOwningPost(post)
        postRepository.deleteById(postId)

        post.postPhoto?.let {
            val uploadDir = environment.getProperty("upload.post.images")!!
            fileUploadUtil.deleteFile(uploadDir, getPhotoNameFromUrl(it)!!)
        }
    }

    override fun deletePostPhoto(postId: Long) {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postId)

        if (post.author != authUser) throw InvalidOperationException()

        post.postPhoto?.let {
            val uploadDir = environment.getProperty("upload.post.images")!!
            fileUploadUtil.deleteFile(uploadDir, getPhotoNameFromUrl(it)!!)
        }

        post.postPhoto = null
        postRepository.save(post)
    }

    override fun likePost(postId: Long): Post {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postId)

        if (!post.likeList.add(authUser)) throw InvalidOperationException()

        post.likeCount++
        val savedPost = postRepository.save(post)

        if (post.author != authUser)
            notificationService.sendNotification(
                post.author!!,
                authUser,
                post,
                null,
                NotificationType.POST_LIKE.name
            )

        return savedPost
    }

    override fun unlikePost(postId: Long): Post {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postId)

        if (!post.likeList.remove(authUser)) throw InvalidOperationException()

        post.likeCount--
        val savedPost = postRepository.save(post)



        if (post.author != authUser) {
            notificationService.removeNotification(
                post.author!!,
                post,
                NotificationType.POST_LIKE.name
            )
        }

        return savedPost
    }

    override fun createPostComment(postId: Long, content: String): Comment {
        if (content.isBlank()) throw EmptyCommentException()

        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postId)
        val comment = commentService.createNewComment(content, post)

        post.commentCount++
        postRepository.save(post)

        if (post.author != authUser)
            notificationService.sendNotification(
                post.author!!,
                authUser,
                post,
                comment,
                NotificationType.POST_COMMENT.name
            )

        return comment
    }

    override fun updatePostComment(commentId: Long, postId: Long, content: String): Comment {
        if (content.isBlank()) throw EmptyCommentException()
        return commentService.updateComment(commentId, content)
    }

    override fun deletePostComment(commentId: Long, postId: Long) {
        val post = getPostById(postId)
        commentService.deleteComment(commentId)
        post.commentCount--
        post.dateLastModified = LocalDateTime.now()
        postRepository.save(post)
    }

    override fun createPostShare(content: String?, postShareId: Long): Post {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postShareId)

        if (post.isTypeShare) throw InvalidOperationException()

        val share = Post(
            content = content,
            author = authUser,
            likeCount = 0,
            shareCount = 0,
            commentCount = 0,
            isTypeShare = true,
            sharedPost = post,
            dateCreated = LocalDateTime.now(),
            dateLastModified = LocalDateTime.now()
        )

        val saved = postRepository.save(share)
        post.shareList.add(saved)
        post.shareCount++
        postRepository.save(post)

        notificationService.sendNotification(
            post.author!!,
            authUser,
            post,
            null,
            NotificationType.POST_SHARE.name
        )

        return saved
    }

    override fun updatePostShare(content: String?, postShareId: Long): Post {
        val authUser = userService.getAuthenticatedUser()
        val post = getPostById(postShareId)

        if (post.author != authUser) throw InvalidOperationException()

        post.content = content
        post.dateLastModified = LocalDateTime.now()
        return postRepository.save(post)
    }

    override fun deletePostShare(postShareId: Long) {
        val authUser = userService.getAuthenticatedUser()
        val share = getPostById(postShareId)

        if (share.author != authUser) throw InvalidOperationException()

        val parent = share.sharedPost!!
        parent.shareList.remove(share)
        parent.shareCount--
        postRepository.save(parent)

        postRepository.deleteById(postShareId)
        notificationService.deleteNotificationByOwningPost(share)
    }

    private fun getPhotoNameFromUrl(url: String?): String? {
        if (url == null) return null
        val prefix =
            "${environment.getProperty("app.root.backend")}${File.separator}${environment.getProperty("upload.post.images")}${File.separator}"
        return url.removePrefix(prefix)
    }

    private fun postToResponse(post: Post): PostResponse {
        val authUser = userService.getAuthenticatedUser()
        return PostResponse(post, post.likeList.contains(authUser))
    }
}