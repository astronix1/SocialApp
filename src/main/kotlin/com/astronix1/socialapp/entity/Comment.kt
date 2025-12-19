package com.astronix1.socialapp.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comments")
open class Comment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(length = 1024)
    open var content: String? = null,

    open var likeCount: Int = 0,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateCreated: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateLastModified: LocalDateTime = LocalDateTime.now(),

    @OneToOne
    @JoinColumn(name = "author_id", nullable = false)
    open var author: User? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    open var post: Post? = null,

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "comment_likes",
        joinColumns = [JoinColumn(name = "comment_id")],
        inverseJoinColumns = [JoinColumn(name = "liker_id")]
    )
    open var likeList: MutableList<User> = mutableListOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Comment) return false
        return id == other.id && author == other.author
    }

    override fun hashCode(): Int {
        return 31 * (id?.hashCode() ?: 0) + (author?.hashCode() ?: 0)
    }
}
