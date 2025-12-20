package com.astronix1.socialapp.controller

import com.astronix1.socialapp.entity.Notification
import com.astronix1.socialapp.service.NotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/notifications")
    fun getNotifications(
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): ResponseEntity<List<Notification>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        val notifications =
            notificationService.getNotificationsForAuthUserPaginate(safePage, safeSize)

        return ResponseEntity(notifications, HttpStatus.OK)
    }

    @PostMapping("/notifications/mark-seen")
    fun markAllSeen(): ResponseEntity<Void> {
        notificationService.markAllSeen()
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/notifications/mark-read")
    fun markAllRead(): ResponseEntity<Void> {
        notificationService.markAllRead()
        return ResponseEntity(HttpStatus.OK)
    }
}
