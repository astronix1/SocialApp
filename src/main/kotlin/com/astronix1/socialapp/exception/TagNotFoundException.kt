package com.astronix1.socialapp.exception

class TagNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
