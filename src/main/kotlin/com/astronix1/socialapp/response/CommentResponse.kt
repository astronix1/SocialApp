package com.astronix1.socialapp.response

import com.astronix1.socialapp.entity.Comment

data class CommentResponse(
    val comment: Comment,
    val likedByAuthUser: Boolean
)
