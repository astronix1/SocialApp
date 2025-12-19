package com.astronix1.socialapp.exception

class CommentNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
