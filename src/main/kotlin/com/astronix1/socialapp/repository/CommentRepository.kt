package com.astronix1.socialapp.repository

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {

    fun findByPost(post: Post, pageable: Pageable): List<Comment>
}
