package com.astronix1.socialapp.controller

import com.astronix1.socialapp.entity.Country
import com.astronix1.socialapp.service.CountryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CountryController(
    private val countryService: CountryService
) {

    @GetMapping("/countries")
    fun getCountryList(): ResponseEntity<List<Country>> {
        val countryList = countryService.getCountryList()
        return ResponseEntity(countryList, HttpStatus.OK)
    }
}
