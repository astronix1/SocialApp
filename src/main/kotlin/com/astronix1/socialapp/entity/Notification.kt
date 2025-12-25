package com.astronix1.socialapp.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notifications")
open class Notification(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(nullable = false)
    open var type: String = "",

    // CHANGED: @OneToOne -> @ManyToOne
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    open var receiver: User? = null,

    // CHANGED: @OneToOne -> @ManyToOne
    @ManyToOne
    @JoinColumn(name = "sender_id")
    open var sender: User? = null,

    // CHANGED: @OneToOne -> @ManyToOne
    @ManyToOne
    @JoinColumn(name = "owning_post_id")
    open var owningPost: Post? = null,

    // CHANGED: @OneToOne -> @ManyToOne
    @ManyToOne
    @JoinColumn(name = "owning_comment_id")
    open var owningComment: Comment? = null,

    open var isSeen: Boolean = false,
    open var isRead: Boolean = false,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateCreated: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateUpdated: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateLastModified: LocalDateTime = LocalDateTime.now()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Notification) return false
        return id == other.id &&
                type == other.type &&
                receiver == other.receiver &&
                owningPost == other.owningPost
    }

    override fun hashCode(): Int {
        return listOf(id, type, receiver, owningPost).hashCode()
    }
}