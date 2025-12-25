package com.astronix1.socialapp.controller

import com.astronix1.socialapp.dto.TagDto
import com.astronix1.socialapp.entity.*
import com.astronix1.socialapp.exception.EmptyPostException
import com.astronix1.socialapp.response.CommentResponse
import com.astronix1.socialapp.response.PostResponse
import com.astronix1.socialapp.service.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
class PostController(
    private val postService: PostService,
    private val commentService: CommentService,
    private val userService: UserService,
    private val tagService: TagService,
    private val objectMapper: ObjectMapper
) {

    @PostMapping("/posts/create")
    fun createNewPost(
        @RequestParam(required = false) content: String?,
        @RequestParam(required = false) postPhoto: MultipartFile?,
        @RequestParam(required = false) postTags: String?
    ): ResponseEntity<Post> {

        if ((content.isNullOrBlank()) && (postPhoto == null || postPhoto.size <= 0)) {
            throw EmptyPostException()
        }

        val tags: List<TagDto>? = postTags?.let {
            objectMapper.readValue(it, object : TypeReference<List<TagDto>>() {})
        }

        val post = postService.createNewPost(content, postPhoto, tags)
        return ResponseEntity(post, HttpStatus.CREATED)
    }

    @PostMapping("/posts/{postId}/update")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestParam(required = false) content: String?,
        @RequestParam(required = false) postPhoto: MultipartFile?,
        @RequestParam(required = false) postTags: String?
    ): ResponseEntity<Post> {

        if ((content.isNullOrBlank()) && (postPhoto == null || postPhoto.size <= 0)) {
            throw EmptyPostException()
        }

        val tags: List<TagDto>? = postTags?.let {
            objectMapper.readValue(it, object : TypeReference<List<TagDto>>() {})
        }

        val updated = postService.updatePost(postId, content, postPhoto, tags)
        return ResponseEntity(updated, HttpStatus.OK)
    }

    @PostMapping("/posts/{postId}/delete")
    fun deletePost(@PathVariable postId: Long): ResponseEntity<Void> {
        postService.deletePost(postId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/posts/{postId}/photo/delete")
    fun deletePostPhoto(@PathVariable postId: Long): ResponseEntity<Void> {
        postService.deletePostPhoto(postId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/posts/{postId}")
    fun getPostById(@PathVariable postId: Long): ResponseEntity<PostResponse> =
        ResponseEntity.ok(postService.getPostResponseById(postId))

    @PostMapping("/posts/{postId}/like")
    fun likePost(@PathVariable postId: Long): ResponseEntity<PostResponse> {
        val post = postService.likePost(postId)
        return ResponseEntity.ok(PostResponse(post, likedByAuthUser = true))
    }

    @PostMapping("/posts/{postId}/unlike")
    fun unlikePost(@PathVariable postId: Long): ResponseEntity<PostResponse> {
        val post = postService.unlikePost(postId)
        return ResponseEntity.ok(PostResponse(post, likedByAuthUser = false))
    }


    @GetMapping("/posts/{postId}/likes")
    fun getPostLikes(
        @PathVariable postId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<User>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        val post = postService.getPostById(postId)
        return ResponseEntity.ok(userService.getLikesByPostPaginate(post, safePage, safeSize))
    }

    @GetMapping("/posts/{postId}/comments")
    fun getPostComments(
        @PathVariable postId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<CommentResponse>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        val post = postService.getPostById(postId)
        return ResponseEntity.ok(commentService.getPostCommentsPaginate(post, safePage, safeSize))
    }

    @PostMapping("/posts/{postId}/comments/create")
    fun createPostComment(
        @PathVariable postId: Long,
        @RequestParam content: String
    ): ResponseEntity<CommentResponse> {

        val comment = postService.createPostComment(postId, content)
        return ResponseEntity.ok(
            CommentResponse(comment = comment, likedByAuthUser = false)
        )
    }

    @PostMapping("/posts/comments/{commentId}/like")
    fun likeComment(@PathVariable commentId: Long) =
        ResponseEntity.ok(commentService.likeComment(commentId))

    @PostMapping("/posts/comments/{commentId}/unlike")
    fun unlikeComment(@PathVariable commentId: Long) =
        ResponseEntity.ok(commentService.unlikeComment(commentId))



    @PostMapping("/posts/{postId}/share/create")
    fun createPostShare(
        @PathVariable postId: Long,
        @RequestParam(required = false) content: String?
    ): ResponseEntity<Post> {
        val share = postService.createPostShare(content, postId)
        return ResponseEntity(share, HttpStatus.CREATED)
    }

    @PostMapping("/posts/share/{shareId}/update")
    fun updatePostShare(
        @PathVariable shareId: Long,
        @RequestParam(required = false) content: String?
    ): ResponseEntity<Post> {
        val updatedShare = postService.updatePostShare(content, shareId)
        return ResponseEntity.ok(updatedShare)
    }

    @PostMapping("/posts/share/{shareId}/delete")
    fun deletePostShare(@PathVariable shareId: Long): ResponseEntity<Void> {
        postService.deletePostShare(shareId)
        return ResponseEntity.ok().build()
    }


    @GetMapping("/posts/tags/{tagName}")
    fun getPostsByTag(
        @PathVariable tagName: String,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<PostResponse>> {
        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        val tag = tagService.getTagByName(tagName)
        return ResponseEntity.ok(postService.getPostByTagPaginate(tag, safePage, safeSize))
    }

    @GetMapping("/posts/{postId}/shares")
    fun getPostShares(
        @PathVariable postId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<PostResponse>> {
        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        val post = postService.getPostById(postId)
        return ResponseEntity.ok(postService.getPostSharesPaginate(post, safePage, safeSize))
    }

}