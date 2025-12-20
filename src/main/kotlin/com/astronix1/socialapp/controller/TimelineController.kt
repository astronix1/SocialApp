package com.astronix1.socialapp.controller

import com.astronix1.socialapp.entity.Tag
import com.astronix1.socialapp.response.PostResponse
import com.astronix1.socialapp.service.PostService
import com.astronix1.socialapp.service.TagService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class TimelineController(
    private val postService: PostService,
    private val tagService: TagService
) {

    @GetMapping("/")
    fun getTimelinePosts(
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<PostResponse>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        return ResponseEntity(
            postService.getTimelinePostsPaginate(safePage, safeSize),
            HttpStatus.OK
        )
    }

    @GetMapping("/tags")
    fun getTimelineTags(): ResponseEntity<List<Tag>> =
        ResponseEntity.ok(tagService.getTimelineTags())
}
