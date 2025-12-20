package com.astronix1.socialapp.service

import com.astronix1.socialapp.dto.*
import com.astronix1.socialapp.entity.Comment
import com.astronix1.socialapp.entity.Post
import com.astronix1.socialapp.entity.User
import com.astronix1.socialapp.response.UserResponse
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun getUserById(userId: Long): User
    fun getUserByEmail(email: String): User

    fun getFollowerUsersPaginate(userId: Long, page: Int, size: Int): List<UserResponse>
    fun getFollowingUsersPaginate(userId: Long, page: Int, size: Int): List<UserResponse>

    fun createNewUser(signupDto: SignupDto): User
    fun updateUserInfo(updateUserInfoDto: UpdateUserInfoDto): User
    fun updateEmail(updateEmailDto: UpdateEmailDto): User
    fun updatePassword(updatePasswordDto: UpdatePasswordDto): User

    fun updateProfilePhoto(photo: MultipartFile): User
    fun updateCoverPhoto(photo: MultipartFile): User

    fun verifyEmail(token: String): User
    fun forgotPassword(email: String)
    fun resetPassword(token: String, resetPasswordDto: ResetPasswordDto): User

    fun deleteUserAccount()
    fun followUser(userId: Long)
    fun unfollowUser(userId: Long)

    fun getAuthenticatedUser(): User

    fun getUserSearchResult(key: String, page: Int, size: Int): List<UserResponse>
    fun getLikesByPostPaginate(post: Post, page: Int, size: Int): List<User>
    fun getLikesByCommentPaginate(comment: Comment, page: Int, size: Int): List<User>
}
