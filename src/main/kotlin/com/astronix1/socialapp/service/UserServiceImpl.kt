package com.astronix1.socialapp.service

import com.astronix1.socialapp.common.AppConstants
import com.astronix1.socialapp.common.UserPrincipal
import com.astronix1.socialapp.dto.*
import com.astronix1.socialapp.entity.*
import com.astronix1.socialapp.enumeration.Role
import com.astronix1.socialapp.exception.*
import com.astronix1.socialapp.mapper.MapStructMapper
import com.astronix1.socialapp.mapper.MapstructMapperUpdate
import com.astronix1.socialapp.repository.UserRepository
import com.astronix1.socialapp.response.UserResponse
import com.astronix1.socialapp.util.FileNamingUtil
import com.astronix1.socialapp.util.FileUploadUtil
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val countryService: CountryService,
    private val emailService: EmailService,
    private val jwtTokenService: JwtTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val mapStructMapper: MapStructMapper,
    private val mapstructMapperUpdate: MapstructMapperUpdate,
    private val environment: Environment,
    private val fileNamingUtil: FileNamingUtil,
    private val fileUploadUtil: FileUploadUtil
) : UserService {

    override fun getUserById(userId: Long): User =
        userRepository.findById(userId).orElseThrow { UserNotFoundException() }

    override fun getUserByEmail(email: String): User =
        userRepository.findByEmail(email).orElseThrow { UserNotFoundException() }

    override fun getFollowerUsersPaginate(userId: Long, page: Int, size: Int): List<UserResponse> {
        val targetUser = getUserById(userId)
        return userRepository.findUsersByFollowingUsers(
            targetUser,
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        ).map(::userToUserResponse)
    }

    override fun getFollowingUsersPaginate(userId: Long, page: Int, size: Int): List<UserResponse> {
        val targetUser = getUserById(userId)
        return userRepository.findUsersByFollowerUsers(
            targetUser,
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        ).map(::userToUserResponse)
    }

    override fun createNewUser(signupDto: SignupDto): User {
        try {
            getUserByEmail(signupDto.email)
            throw EmailExistsException()
        } catch (e: UserNotFoundException) {
            val newUser = User().apply {
                email = signupDto.email
                password = passwordEncoder.encode(signupDto.password)
                firstName = signupDto.firstName
                lastName = signupDto.lastName
                followerCount = 0
                followingCount = 0
                enabled = true
                accountVerified = false
                emailVerified = false
                joinDate = LocalDateTime.now()
                dateLastModified = LocalDateTime.now()
                role = Role.ROLE_USER.name
            }

            val savedUser = userRepository.save(newUser)
            val token = jwtTokenService.generateToken(UserPrincipal(savedUser))
            emailService.send(
                savedUser.email,
                AppConstants.VERIFY_EMAIL,
                emailService.buildEmailVerifyMail(token)
            )
            return savedUser
        }
    }

    override fun updateUserInfo(updateUserInfoDto: UpdateUserInfoDto): User {
        val authUser = getAuthenticatedUser()
        updateUserInfoDto.countryName?.let {
            authUser.country = countryService.getCountryByName(it)
        }
        mapstructMapperUpdate.updateUserFromUserUpdateDto(updateUserInfoDto, authUser)
        return userRepository.save(authUser)
    }

    override fun updateEmail(updateEmailDto: UpdateEmailDto): User {
        val authUser = getAuthenticatedUser()
        val newEmail = updateEmailDto.email

        if (newEmail.equals(authUser.email, ignoreCase = true)) {
            throw SameEmailUpdateException()
        }

        try {
            getUserByEmail(newEmail)
            throw EmailExistsException()
        } catch (e: UserNotFoundException) {
            if (!passwordEncoder.matches(updateEmailDto.password, authUser.password)) {
                throw InvalidOperationException()
            }

            authUser.email = newEmail
            authUser.emailVerified = false
            authUser.dateLastModified = LocalDateTime.now()

            val updatedUser = userRepository.save(authUser)
            val token = jwtTokenService.generateToken(UserPrincipal(updatedUser))
            emailService.send(
                updatedUser.email,
                AppConstants.VERIFY_EMAIL,
                emailService.buildEmailVerifyMail(token)
            )
            return updatedUser
        }
    }

    override fun updatePassword(updatePasswordDto: UpdatePasswordDto): User {
        val authUser = getAuthenticatedUser()
        if (!passwordEncoder.matches(updatePasswordDto.oldPassword, authUser.password)) {
            throw InvalidOperationException()
        }
        authUser.password = passwordEncoder.encode(updatePasswordDto.password)
        authUser.dateLastModified = LocalDateTime.now()
        return userRepository.save(authUser)
    }

    override fun updateProfilePhoto(photo: MultipartFile): User {
        val authUser = getAuthenticatedUser()
        if (photo.size > 0) {
            val uploadDir = environment.getProperty("upload.user.images")!!
            val oldPhotoName = getPhotoNameFromPhotoUrl(authUser.profilePhoto)
            val newPhotoName = fileNamingUtil.nameFile(photo)
            val photoUrl = "${environment.getProperty("app.root.backend")}${File.separator}$uploadDir${File.separator}$newPhotoName"

            authUser.profilePhoto = photoUrl
            if (oldPhotoName == null) {
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, photo)
            } else {
                fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, photo)
            }
            authUser.dateLastModified = LocalDateTime.now()
            return userRepository.save(authUser)
        } else {
            throw InvalidOperationException()
        }
    }

    override fun updateCoverPhoto(photo: MultipartFile): User {
        val authUser = getAuthenticatedUser()
        if (photo.size > 0) {
            val uploadDir = environment.getProperty("upload.user.images")!!
            val oldPhotoName = getPhotoNameFromPhotoUrl(authUser.coverPhoto)
            val newPhotoName = fileNamingUtil.nameFile(photo)
            val photoUrl = "${environment.getProperty("app.root.backend")}${File.separator}$uploadDir${File.separator}$newPhotoName"

            authUser.coverPhoto = photoUrl
            if (oldPhotoName == null) {
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, photo)
            } else {
                fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, photo)
            }
            authUser.dateLastModified = LocalDateTime.now()
            return userRepository.save(authUser)
        } else {
            throw InvalidOperationException()
        }
    }

    override fun verifyEmail(token: String): User {
        val email = jwtTokenService.getSubjectFromToken(token)
        val user = getUserByEmail(email)
        user.emailVerified = true
        user.accountVerified = true
        user.dateLastModified = LocalDateTime.now()
        return userRepository.save(user)
    }

    override fun forgotPassword(email: String) {
        try {
            val user = getUserByEmail(email)
            val token = jwtTokenService.generateToken(UserPrincipal(user))
            emailService.send(
                user.email,
                AppConstants.RESET_PASSWORD,
                emailService.buildResetPasswordMail(token)
            )
        } catch (_: UserNotFoundException) {}
    }

    override fun resetPassword(token: String, resetPasswordDto: ResetPasswordDto): User {
        val email = jwtTokenService.getSubjectFromToken(token)
        val user = getUserByEmail(email)
        user.password = passwordEncoder.encode(resetPasswordDto.password)
        return userRepository.save(user)
    }

    override fun deleteUserAccount() {
        val authUser = getAuthenticatedUser()
        getPhotoNameFromPhotoUrl(authUser.profilePhoto)?.let {
            fileUploadUtil.deleteFile(environment.getProperty("upload.user.images")!!, it)
        }
        userRepository.deleteByEmail(authUser.email)
    }

    override fun followUser(userId: Long) {
        val authUser = getAuthenticatedUser()
        if (authUser.id == userId) throw InvalidOperationException()

        val target = getUserById(userId)
        authUser.followingUsers.add(target)
        target.followerUsers.add(authUser)

        authUser.followingCount++
        target.followerCount++

        userRepository.save(target)
        userRepository.save(authUser)
    }

    override fun unfollowUser(userId: Long) {
        val authUser = getAuthenticatedUser()
        if (authUser.id == userId) throw InvalidOperationException()

        val target = getUserById(userId)
        authUser.followingUsers.remove(target)
        target.followerUsers.remove(authUser)

        authUser.followingCount--
        target.followerCount--

        userRepository.save(target)
        userRepository.save(authUser)
    }

    override fun getUserSearchResult(key: String, page: Int, size: Int): List<UserResponse> {
        if (key.length < 3) throw InvalidOperationException()
        return userRepository.findUsersByName(key, PageRequest.of(page, size))
            .map(::userToUserResponse)
    }

    override fun getLikesByPostPaginate(post: Post, page: Int, size: Int): List<User> =
        userRepository.findUsersByLikedPosts(
            post,
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        )

    override fun getLikesByCommentPaginate(comment: Comment, page: Int, size: Int): List<User> =
        userRepository.findUsersByLikedComments(
            comment,
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        )

    override fun getAuthenticatedUser(): User {
        val email = SecurityContextHolder.getContext().authentication.principal.toString()
        return getUserByEmail(email)
    }

    private fun getPhotoNameFromPhotoUrl(photoUrl: String?): String? {
        if (photoUrl == null) return null
        val base = environment.getProperty("app.root.backend") + File.separator +
                environment.getProperty("upload.user.images") + File.separator
        return photoUrl.removePrefix(base)
    }

    private fun userToUserResponse(user: User): UserResponse {
        val authUser = getAuthenticatedUser()
        return UserResponse(
            user = user,
            followedByAuthUser = user.followerUsers.contains(authUser)
        )
    }
}