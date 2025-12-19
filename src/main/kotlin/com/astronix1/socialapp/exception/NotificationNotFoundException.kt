package com.astronix1.socialapp.exception

class NotificationNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
