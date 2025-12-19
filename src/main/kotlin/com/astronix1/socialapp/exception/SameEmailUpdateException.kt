package com.astronix1.socialapp.exception

class SameEmailUpdateException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
