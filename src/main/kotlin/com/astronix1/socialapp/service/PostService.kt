package com.astronix1.socialapp.service

import com.astronix1.socialapp.dto.TagDto
import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.Tag
import com.astronix1.socialapp.entity.User
import com.astronix1.socialapp.response.PostResponse
import org.springframework.web.multipart.MultipartFile

interface PostService {
    fun getPostById(postId: Long): Post
    fun getPostResponseById(postId: Long): PostResponse
    fun getPostsByUserPaginate(author: User, page: Int, size: Int): List<PostResponse>
    fun getTimelinePostsPaginate(page: Int, size: Int): List<PostResponse>
    fun getPostSharesPaginate(sharedPost: Post, page: Int, size: Int): List<PostResponse>
    fun getPostByTagPaginate(tag: Tag, page: Int, size: Int): List<PostResponse>

    fun createNewPost(
        content: String?,
        postPhoto: MultipartFile?,
        postTags: List<TagDto>?
    ): Post

    fun updatePost(
        postId: Long,
        content: String?,
        postPhoto: MultipartFile?,
        postTags: List<TagDto>?
    ): Post

    fun deletePost(postId: Long)
    fun deletePostPhoto(postId: Long)

    fun likePost(postId: Long)
    fun unlikePost(postId: Long)

    fun createPostComment(postId: Long, content: String): Comment
    fun updatePostComment(commentId: Long, postId: Long, content: String): Comment
    fun deletePostComment(commentId: Long, postId: Long)

    fun createPostShare(content: String?, postShareId: Long): Post
    fun updatePostShare(content: String?, postShareId: Long): Post
    fun deletePostShare(postShareId: Long)
}
