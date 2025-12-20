package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Tag
import com.astronix1.socialapp.exception.TagExistsException
import com.astronix1.socialapp.exception.TagNotFoundException
import com.astronix1.socialapp.repository.TagRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TagServiceImpl(
    private val tagRepository: TagRepository
) : TagService {

    override fun getTagById(id: Long): Tag =
        tagRepository.findById(id).orElseThrow { TagNotFoundException() }

    override fun getTagByName(name: String): Tag =
        tagRepository.findTagByName(name).orElseThrow { TagNotFoundException() }

    override fun createNewTag(name: String): Tag {
        try {
            // if exists → throw
            getTagByName(name)
            throw TagExistsException()
        } catch (e: TagNotFoundException) {
            val newTag = Tag(
                name = name,
                tagUseCounter = 1,
                dateCreated = LocalDateTime.now(),
                dateLastModified = LocalDateTime.now()
            )
            return tagRepository.save(newTag)
        }
    }

    override fun increaseTagUseCounter(name: String): Tag {
        val tag = getTagByName(name)
        tag.tagUseCounter += 1
        tag.dateLastModified = LocalDateTime.now()
        return tagRepository.save(tag)
    }

    override fun decreaseTagUseCounter(name: String): Tag {
        val tag = getTagByName(name)
        tag.tagUseCounter -= 1
        tag.dateLastModified = LocalDateTime.now()
        return tagRepository.save(tag)
    }

    override fun getTimelineTags(): List<Tag> =
        tagRepository.findAll(
            PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "tagUseCounter")
            )
        ).content
}
