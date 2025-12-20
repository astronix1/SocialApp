package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.response.CommentResponse

interface CommentService {
    fun getCommentById(commentId: Long): Comment
    fun createNewComment(content: String, post: Post): Comment
    fun updateComment(commentId: Long, content: String): Comment
    fun likeComment(commentId: Long): Comment
    fun unlikeComment(commentId: Long): Comment
    fun deleteComment(commentId: Long)
    fun getPostCommentsPaginate(
        post: Post,
        page: Int,
        size: Int
    ): List<CommentResponse>
}
