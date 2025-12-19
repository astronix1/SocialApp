package com.astronix1.socialapp.exception

class EmptyPostException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
