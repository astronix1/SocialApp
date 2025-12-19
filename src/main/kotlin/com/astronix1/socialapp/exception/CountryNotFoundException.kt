package com.astronix1.socialapp.exception

class CountryNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
