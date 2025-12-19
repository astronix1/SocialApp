package com.astronix1.socialapp.exception

class ShareExistsException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
