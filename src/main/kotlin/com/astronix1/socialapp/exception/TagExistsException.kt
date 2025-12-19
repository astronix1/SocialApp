package com.astronix1.socialapp.exception

class TagExistsException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
