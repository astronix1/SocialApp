package com.astronix1.socialapp.util

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Component
class FileUploadUtil {

    @Throws(IOException::class)
    fun saveNewFile(
        uploadDir: String,
        fileName: String,
        multipartFile: MultipartFile
    ) {
        val uploadPath: Path = Paths.get(uploadDir)

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }

        multipartFile.inputStream.use { inputStream ->
            val filePath = uploadPath.resolve(fileName)
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    @Throws(IOException::class)
    fun updateFile(
        uploadDir: String,
        oldFileName: String,
        newFileName: String,
        multipartFile: MultipartFile
    ) {
        val uploadPath: Path = Paths.get(uploadDir)

        multipartFile.inputStream.use { inputStream ->
            val oldFilePath = uploadPath.resolve(oldFileName)
            val newFilePath = uploadPath.resolve(newFileName)

            Files.deleteIfExists(oldFilePath)
            Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    @Throws(IOException::class)
    fun deleteFile(uploadDir: String, fileName: String) {
        val uploadPath = Paths.get(uploadDir)
        val filePath = uploadPath.resolve(fileName)
        Files.deleteIfExists(filePath)
    }
}
