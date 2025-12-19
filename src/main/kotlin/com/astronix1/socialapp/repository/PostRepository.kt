package com.astronix1.socialapp.repository

import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.Tag
import com.astronix1.socialapp.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun findPostsByAuthor(author: User, pageable: Pageable): List<Post>

    fun findPostsByAuthorIdIn(
        followingUserIds: List<Long>,
        pageable: Pageable
    ): List<Post>

    fun findPostsBySharedPost(post: Post, pageable: Pageable): List<Post>

    fun findPostsByPostTags(tag: Tag, pageable: Pageable): List<Post>
}
