package com.astronix1.socialapp.util

import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Component
class FileNamingUtil {

    fun nameFile(multipartFile: MultipartFile): String {
        val originalFileName =
            StringUtils.cleanPath(multipartFile.originalFilename ?: "")

        val dotIndex = originalFileName.lastIndexOf('.')
        val extension = if (dotIndex != -1) {
            originalFileName.substring(dotIndex)
        } else {
            ""
        }

        return "${UUID.randomUUID()}$extension"
    }
}
