package com.astronix1.socialapp.repository

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Notification
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface NotificationRepository : JpaRepository<Notification, Long> {

    fun findByReceiverAndOwningPostAndType(
        receiver: User,
        owningPost: Post,
        type: String
    ): Optional<Notification>

    fun findNotificationsByReceiver(
        receiver: User,
        pageable: Pageable
    ): List<Notification>

    fun findNotificationsByReceiverAndIsSeenIsFalse(receiver: User): List<Notification>

    fun findNotificationsByReceiverAndIsReadIsFalse(receiver: User): List<Notification>

    fun deleteNotificationByOwningPost(owningPost: Post)

    fun deleteNotificationByOwningComment(owningComment: Comment)
}
