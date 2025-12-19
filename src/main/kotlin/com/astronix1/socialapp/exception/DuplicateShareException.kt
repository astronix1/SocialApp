package com.astronix1.socialapp.exception

class DuplicateShareException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
