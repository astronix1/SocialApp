package com.astronix1.socialapp.exception

class EmailExistsException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
