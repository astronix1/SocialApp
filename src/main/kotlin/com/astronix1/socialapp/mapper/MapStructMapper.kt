package com.astronix1.socialapp.mapper

import com.astronix1.socialapp.dto.UpdateUserInfoDto
import com.astronix1.socialapp.entity.User
import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
interface MapStructMapper {
    fun userUpdateDtoToUser(updateUserInfoDto: UpdateUserInfoDto): User
}
