package com.astronix1.socialapp.exception

class ShareNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
