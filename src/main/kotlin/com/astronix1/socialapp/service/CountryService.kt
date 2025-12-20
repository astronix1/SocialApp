package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.Country

interface CountryService {
    fun getCountryById(id: Long): Country
    fun getCountryByName(name: String): Country
    fun getCountryList(): List<Country>
}
