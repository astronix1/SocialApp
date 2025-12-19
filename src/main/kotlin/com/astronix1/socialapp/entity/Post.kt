package com.astronix1.socialapp.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
open class Post(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(length = 4096)
    open var content: String? = null,

    open var postPhoto: String? = null,

    open var likeCount: Int = 0,
    open var commentCount: Int = 0,
    open var shareCount: Int = 0,

    @Column(nullable = false)
    open var isTypeShare: Boolean = false,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateCreated: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateLastModified: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "author_id")
    open var author: User? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = [CascadeType.REMOVE])
    open var postComments: MutableList<Comment> = mutableListOf(),

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "post_likes",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "liker_id")]
    )
    open var likeList: MutableList<User> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "shared_post_id")
    open var sharedPost: Post? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "sharedPost")
    open var shareList: MutableList<Post> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "post_tags",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    open var postTags: MutableList<Tag> = mutableListOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Post) return false
        return id == other.id && author == other.author
    }

    override fun hashCode(): Int {
        return 31 * (id?.hashCode() ?: 0) + (author?.hashCode() ?: 0)
    }
}
