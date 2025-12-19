package com.astronix1.socialapp.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "countries")
open class Country(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(length = 64, nullable = false, unique = true)
    open var name: String = "",

    @Column(length = 3)
    open var countryCode: String? = null,

    @Column(length = 4)
    open var callingCode: String? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateCreated: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var dateLastModified: LocalDateTime = LocalDateTime.now()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Country) return false
        return id == other.id && name == other.name
    }

    override fun hashCode(): Int {
        return 31 * (id?.hashCode() ?: 0) + name.hashCode()
    }
}
