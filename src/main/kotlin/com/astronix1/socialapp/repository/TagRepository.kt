package com.astronix1.socialapp.repository

import com.astronix1.socialapp.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TagRepository : JpaRepository<Tag, Long> {

    fun findTagByName(name: String): Optional<Tag>
}
