package com.astronix1.socialapp.exception

class CountryExistsException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
