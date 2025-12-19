package com.astronix1.socialapp.repository

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun deleteByEmail(email: String)

    fun findUsersByFollowerUsers(user: User, pageable: Pageable): List<User>

    fun findUsersByFollowingUsers(user: User, pageable: Pageable): List<User>

    fun findUsersByLikedPosts(post: Post, pageable: Pageable): List<User>

    fun findUsersByLikedComments(comment: Comment, pageable: Pageable): List<User>

    @Query(
        value = """
            SELECT * FROM users u
            WHERE (u.first_name || ' ' || u.last_name) ILIKE %:name%
            ORDER BY u.first_name ASC, u.last_name ASC
        """,
        nativeQuery = true
    )
    fun findUsersByName(
        @Param("name") name: String,
        pageable: Pageable
    ): List<User>
}
