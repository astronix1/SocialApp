package com.astronix1.socialapp.controller

import com.astronix1.socialapp.common.AppConstants
import com.astronix1.socialapp.common.UserPrincipal
import com.astronix1.socialapp.dto.*
import com.astronix1.socialapp.entity.User
import com.astronix1.socialapp.response.PostResponse
import com.astronix1.socialapp.response.UserResponse
import com.astronix1.socialapp.service.JwtTokenService
import com.astronix1.socialapp.service.PostService
import com.astronix1.socialapp.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
    private val postService: PostService,
    private val jwtTokenService: JwtTokenService,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupDto: SignupDto): ResponseEntity<User> {
        val user = userService.createNewUser(signupDto)
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): ResponseEntity<User> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.email,
                loginDto.password
            )
        )

        val user = userService.getUserByEmail(loginDto.email)
        val userPrincipal = UserPrincipal(user)

        val headers = HttpHeaders()
        headers.add(AppConstants.TOKEN_HEADER, jwtTokenService.generateToken(userPrincipal))

        return ResponseEntity(user, headers, HttpStatus.OK)
    }

    @GetMapping("/profile")
    fun showUserProfile(authentication: Authentication): ResponseEntity<User> {
        val user = userService.getUserByEmail(authentication.name)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/account/update/info")
    fun updateUserInfo(
        @RequestBody @Valid dto: UpdateUserInfoDto
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateUserInfo(dto))
    }

    @PostMapping("/account/update/email")
    fun updateEmail(
        @RequestBody @Valid dto: UpdateEmailDto
    ): ResponseEntity<Void> {
        userService.updateEmail(dto)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/account/update/password")
    fun updatePassword(
        @RequestBody @Valid dto: UpdatePasswordDto
    ): ResponseEntity<Void> {
        userService.updatePassword(dto)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/account/update/profile-photo")
    fun updateProfilePhoto(
        @RequestParam profilePhoto: MultipartFile
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateProfilePhoto(profilePhoto))
    }

    @PostMapping("/account/update/cover-photo")
    fun updateCoverPhoto(
        @RequestParam coverPhoto: MultipartFile
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateCoverPhoto(coverPhoto))
    }

    @PostMapping("/account/delete")
    fun deleteAccount(): ResponseEntity<Void> {
        userService.deleteUserAccount()
        return ResponseEntity.ok().build()
    }

    @PostMapping("/account/follow/{userId}")
    fun followUser(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.followUser(userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/account/unfollow/{userId}")
    fun unfollowUser(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.unfollowUser(userId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/users/{userId}/following")
    fun getFollowing(
        @PathVariable userId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<UserResponse>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        return ResponseEntity.ok(
            userService.getFollowingUsersPaginate(userId, safePage, safeSize)
        )
    }

    @GetMapping("/users/{userId}/follower")
    fun getFollowers(
        @PathVariable userId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<UserResponse>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        return ResponseEntity.ok(
            userService.getFollowerUsersPaginate(userId, safePage, safeSize)
        )
    }

    @GetMapping("/users/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserResponse> {
        val authUser = userService.getAuthenticatedUser()
        val targetUser = userService.getUserById(userId)

        return ResponseEntity.ok(
            UserResponse(
                user = targetUser,
                followedByAuthUser = targetUser.followerUsers.contains(authUser)
            )
        )
    }

    @GetMapping("/users/{userId}/posts")
    fun getUserPosts(
        @PathVariable userId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<PostResponse>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        val user = userService.getUserById(userId)
        return ResponseEntity.ok(
            postService.getPostsByUserPaginate(user, safePage, safeSize)
        )
    }

    @GetMapping("/users/search")
    fun searchUsers(
        @RequestParam key: String,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<UserResponse>> {

        val safePage = if (page < 0) 0 else page - 1
        val safeSize = if (size <= 0) 5 else size

        return ResponseEntity.ok(
            userService.getUserSearchResult(key, safePage, safeSize)
        )
    }

    @PostMapping("/verify-email/{token}")
    fun verifyEmail(@PathVariable token: String): ResponseEntity<Void> {
        userService.verifyEmail(token)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestParam email: String): ResponseEntity<Void> {
        userService.forgotPassword(email)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/reset-password/{token}")
    fun resetPassword(
        @PathVariable token: String,
        @RequestBody @Valid dto: ResetPasswordDto
    ): ResponseEntity<Void> {
        userService.resetPassword(token, dto)
        return ResponseEntity.ok().build()
    }
}
