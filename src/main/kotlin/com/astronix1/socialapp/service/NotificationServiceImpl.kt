package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Notification
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.User
import com.astronix1.socialapp.exception.NotificationNotFoundException
import com.astronix1.socialapp.repository.NotificationRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val userService: UserService
) : NotificationService {

    override fun getNotificationById(notificationId: Long): Notification =
        notificationRepository.findById(notificationId)
            .orElseThrow { NotificationNotFoundException() }

    override fun getNotificationByReceiverAndOwningPostAndType(
        receiver: User,
        owningPost: Post,
        type: String
    ): Notification =
        notificationRepository
            .findByReceiverAndOwningPostAndType(receiver, owningPost, type)
            .orElseThrow { NotificationNotFoundException() }

    override fun sendNotification(
        receiver: User,
        sender: User,
        owningPost: Post,
        owningComment: Comment?,
        type: String
    ) {
        try {
            val notification =
                getNotificationByReceiverAndOwningPostAndType(receiver, owningPost, type)

            notification.sender = sender
            notification.isSeen = false
            notification.isRead = false
            notification.dateUpdated = LocalDateTime.now()
            notification.dateLastModified = LocalDateTime.now()
            notificationRepository.save(notification)

        } catch (e: NotificationNotFoundException) {
            val notification = Notification(
                type = type,
                receiver = receiver,
                sender = sender,
                owningPost = owningPost,
                owningComment = owningComment,
                isSeen = false,
                isRead = false,
                dateCreated = LocalDateTime.now(),
                dateUpdated = LocalDateTime.now(),
                dateLastModified = LocalDateTime.now()
            )
            notificationRepository.save(notification)
        }
    }

    override fun removeNotification(receiver: User, owningPost: Post, type: String) {
        val authUser = userService.getAuthenticatedUser()
        val notification =
            getNotificationByReceiverAndOwningPostAndType(receiver, owningPost, type)

        if (notification.sender == authUser) {
            notification.sender = null
            notification.dateLastModified = LocalDateTime.now()
            notificationRepository.save(notification)
        }
    }

    override fun getNotificationsForAuthUserPaginate(page: Int, size: Int): List<Notification> {
        val authUser = userService.getAuthenticatedUser()
        return notificationRepository.findNotificationsByReceiver(
            authUser,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateUpdated"))
        )
    }

    override fun markAllSeen() {
        val authUser = userService.getAuthenticatedUser()
        notificationRepository
            .findNotificationsByReceiverAndIsSeenIsFalse(authUser)
            .forEach {
                it.isSeen = true
                it.dateLastModified = LocalDateTime.now()
                notificationRepository.save(it)
            }
    }

    override fun markAllRead() {
        val authUser = userService.getAuthenticatedUser()
        notificationRepository
            .findNotificationsByReceiverAndIsReadIsFalse(authUser)
            .forEach {
                it.isSeen = true
                it.isRead = true
                it.dateLastModified = LocalDateTime.now()
                notificationRepository.save(it)
            }
    }

    override fun deleteNotification(receiver: User, owningPost: Post, type: String) {
        val notification =
            getNotificationByReceiverAndOwningPostAndType(receiver, owningPost, type)
        notificationRepository.deleteById(notification.id!!)
    }

    override fun deleteNotificationByOwningPost(owningPost: Post) {
        notificationRepository.deleteNotificationByOwningPost(owningPost)
    }

    override fun deleteNotificationByOwningComment(owningComment: Comment) {
        notificationRepository.deleteNotificationByOwningComment(owningComment)
    }
}