package com.astronix1.socialapp.exception

class PostNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
