package com.astronix1.socialapp.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
open class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(length = 64, nullable = false, unique = true)
    open var email: String = "",

    @Column(length = 256, nullable = false)
    @JsonIgnore
    open var password: String = "",

    @Column(length = 64, nullable = false)
    open var firstName: String = "",

    @Column(length = 64, nullable = false)
    open var lastName: String = "",

    @Column(length = 100)
    open var intro: String? = null,

    @Column(length = 16)
    open var gender: String? = null,

    @Column(length = 128)
    open var hometown: String? = null,

    @Column(length = 128)
    open var currentCity: String? = null,

    @Column(length = 128)
    open var eduInstitution: String? = null,

    @Column(length = 128)
    open var workplace: String? = null,

    @Column(length = 256)
    open var profilePhoto: String? = null,

    @Column(length = 256)
    open var coverPhoto: String? = null,

    @Column(length = 32, nullable = false)
    open var role: String = "USER",

    open var followerCount: Int = 0,
    open var followingCount: Int = 0,

    open var enabled: Boolean = false,
    open var accountVerified: Boolean = false,
    open var emailVerified: Boolean = false,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var birthDate: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var joinDate: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateLastModified: LocalDateTime = LocalDateTime.now(),

    @OneToOne
    @JoinColumn(name = "country_id")
    open var country: Country? = null,

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "follow_users",
        joinColumns = [JoinColumn(name = "followed_id")],
        inverseJoinColumns = [JoinColumn(name = "follower_id")]
    )
    open var followerUsers: MutableList<User> = mutableListOf(),

    @JsonIgnore
    @ManyToMany(mappedBy = "followerUsers")
    open var followingUsers: MutableList<User> = mutableListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = [CascadeType.REMOVE])
    open var postList: MutableList<Post> = mutableListOf(),

    @JsonIgnore
    @ManyToMany(mappedBy = "likeList")
    open var likedPosts: MutableList<Post> = mutableListOf(),

    @JsonIgnore
    @ManyToMany(mappedBy = "likeList")
    open var likedComments: MutableList<Comment> = mutableListOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id && email == other.email
    }

    override fun hashCode(): Int {
        return 31 * (id?.hashCode() ?: 0) + email.hashCode()
    }
}
