package com.astronix1.socialapp.exception

class EmailNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
