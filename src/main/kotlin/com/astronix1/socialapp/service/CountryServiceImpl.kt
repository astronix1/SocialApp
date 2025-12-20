package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Country
import com.astronix1.socialapp.exception.CountryNotFoundException
import com.astronix1.socialapp.repository.CountryRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CountryServiceImpl(
    private val countryRepository: CountryRepository
) : CountryService {

    override fun getCountryById(id: Long): Country =
        countryRepository.findById(id)
            .orElseThrow { CountryNotFoundException() }

    override fun getCountryByName(name: String): Country =
        countryRepository.findByName(name)
            .orElseThrow { CountryNotFoundException() }

    override fun getCountryList(): List<Country> =
        countryRepository.findAll(
            Sort.by(Sort.Direction.ASC, "name")
        )
}
