package com.astronix1.socialapp.repository

import com.astronix1.socialapp.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CountryRepository : JpaRepository<Country, Long> {

    fun findByName(name: String): Optional<Country>
}
