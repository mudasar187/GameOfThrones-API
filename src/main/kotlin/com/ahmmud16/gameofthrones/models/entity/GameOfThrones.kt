package com.ahmmud16.gameofthrones.models.entity

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class GameOfThrones (


        @get:NotBlank
        @get:Size(max = 128)
        var characterName: String,

        @ElementCollection
        @get:NotNull
        var houseName: Set<String>? = setOf(),

        @get:NotBlank
        @get:Size(max = 2048)
        var characterImageThumb: String? = null,

        @get:NotBlank
        @get:Size(max = 2048)
        var characterImageFull: String? = null,

        @get:ElementCollection
        @get:NotBlank
        var parents: Set<String>? = setOf(),

        @get:ElementCollection
        @get:NotBlank
        var parentsOf: Set<String>? = setOf(),

        @get:NotBlank
        var royal: Boolean? = null,

        @get:ElementCollection
        @get:NotNull
        var siblings: Set<String>? = setOf(),

        @get:ElementCollection
        @get:NotNull
        var killed: Set<String>? = setOf(),

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)