package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.enumeration.NotificationType
import com.astronix1.socialapp.exception.CommentNotFoundException
import com.astronix1.socialapp.exception.InvalidOperationException
import com.astronix1.socialapp.repository.CommentRepository
import com.astronix1.socialapp.response.CommentResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val notificationService: NotificationService
) : CommentService {

    override fun getCommentById(commentId: Long): Comment =
        commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException() }

    override fun createNewComment(content: String, post: Post): Comment {
        val authUser = userService.getAuthenticatedUser()

        val newComment = Comment(
            content = content,
            author = authUser,
            post = post,
            likeCount = 0,
            dateCreated = LocalDateTime.now(),
            dateLastModified = LocalDateTime.now()
        )

        return commentRepository.save(newComment)
    }

    override fun updateComment(commentId: Long, content: String): Comment {
        val authUser = userService.getAuthenticatedUser()
        val targetComment = getCommentById(commentId)

        if (targetComment.author != authUser) {
            throw InvalidOperationException()
        }

        targetComment.content = content
        targetComment.dateLastModified = LocalDateTime.now()
        return commentRepository.save(targetComment)
    }

    override fun deleteComment(commentId: Long) {
        val authUser = userService.getAuthenticatedUser()
        val targetComment = getCommentById(commentId)

        if (targetComment.author != authUser) {
            throw InvalidOperationException()
        }

        commentRepository.deleteById(commentId)
        notificationService.deleteNotificationByOwningComment(targetComment)
    }

    override fun likeComment(commentId: Long): Comment {
        val authUser = userService.getAuthenticatedUser()
        val targetComment = getCommentById(commentId)

        if (targetComment.likeList.contains(authUser)) {
            throw InvalidOperationException()
        }

        targetComment.likeCount += 1
        targetComment.likeList.add(authUser)
        targetComment.dateLastModified = LocalDateTime.now()

        val updated = commentRepository.save(targetComment)

        if (targetComment.author != authUser) {
            notificationService.sendNotification(
                targetComment.author!!,
                authUser,
                targetComment.post,
                targetComment,
                NotificationType.COMMENT_LIKE.name
            )
        }

        return updated
    }

    override fun unlikeComment(commentId: Long): Comment {
        val authUser = userService.getAuthenticatedUser()
        val targetComment = getCommentById(commentId)

        if (!targetComment.likeList.contains(authUser)) {
            throw InvalidOperationException()
        }

        targetComment.likeCount -= 1
        targetComment.likeList.remove(authUser)
        targetComment.dateLastModified = LocalDateTime.now()

        val updated = commentRepository.save(targetComment)

        if (targetComment.author != authUser) {
            notificationService.removeNotification(
                targetComment.author!!,
                targetComment.post,
                NotificationType.COMMENT_LIKE.name
            )
        }

        return updated
    }

    override fun getPostCommentsPaginate(
        post: Post,
        page: Int,
        size: Int
    ): List<CommentResponse> {
        val authUser = userService.getAuthenticatedUser()

        val comments = commentRepository.findByPost(
            post,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        )

        return comments.map { comment ->
            CommentResponse(
                comment = comment,
                likedByAuthUser = comment.likeList.contains(authUser)
            )
        }
    }
}