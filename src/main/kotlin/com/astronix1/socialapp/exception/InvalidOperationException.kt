package com.astronix1.socialapp.exception

class InvalidOperationException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
