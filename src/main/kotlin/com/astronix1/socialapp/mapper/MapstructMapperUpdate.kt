package com.astronix1.socialapp.mapper

import com.astronix1.socialapp.dto.UpdateUserInfoDto
import com.astronix1.socialapp.entity.User
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface MapstructMapperUpdate {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "country", ignore = true)
    fun updateUserFromUserUpdateDto(
        dto: UpdateUserInfoDto,
        @MappingTarget user: User
    )
}
