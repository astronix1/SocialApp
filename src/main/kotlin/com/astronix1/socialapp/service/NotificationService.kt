package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Notification
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.User

interface NotificationService {
    fun getNotificationById(notificationId: Long): Notification
    fun getNotificationByReceiverAndOwningPostAndType(
        receiver: User,
        owningPost: Post,
        type: String
    ): Notification

    fun sendNotification(
        receiver: User,
        sender: User,
        owningPost: Post,
        owningComment: Comment?,
        type: String
    )

    fun removeNotification(receiver: User, owningPost: Post, type: String)
    fun getNotificationsForAuthUserPaginate(page: Int, size: Int): List<Notification>
    fun markAllSeen()
    fun markAllRead()
    fun deleteNotification(receiver: User, owningPost: Post, type: String)
    fun deleteNotificationByOwningPost(owningPost: Post)
    fun deleteNotificationByOwningComment(owningComment: Comment)
}