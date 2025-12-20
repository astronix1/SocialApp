package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Tag

interface TagService {
    fun getTagById(id: Long): Tag
    fun getTagByName(name: String): Tag
    fun createNewTag(name: String): Tag
    fun increaseTagUseCounter(name: String): Tag
    fun decreaseTagUseCounter(name: String): Tag
    fun getTimelineTags(): List<Tag>
}
