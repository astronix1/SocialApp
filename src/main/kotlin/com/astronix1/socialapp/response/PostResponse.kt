package com.astronix1.socialapp.response

import com.astronix1.socialapp.entity.Post

data class PostResponse(
    val post: Post,
    val likedByAuthUser: Boolean
)
