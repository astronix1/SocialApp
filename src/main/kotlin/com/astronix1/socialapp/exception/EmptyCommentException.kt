package com.astronix1.socialapp.exception

class EmptyCommentException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
