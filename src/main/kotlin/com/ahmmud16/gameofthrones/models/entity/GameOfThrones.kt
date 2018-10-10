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


        @get:Size(max = 128)
        var characterName: String,

        @get:ElementCollection
        var houseName: Set<String>? = setOf(),

        @get:Size(max = 2048)
        var characterImageThumb: String? = null,

        @get:Size(max = 2048)
        var characterImageFull: String? = null,

        @get:ElementCollection
        var parents: Set<String>? = setOf(),

        @get:ElementCollection
        var parentsOf: Set<String>? = setOf(),

        var royal: Boolean? = null,

        @get:ElementCollection
        var siblings: Set<String>? = setOf(),

        @get:ElementCollection
        var killed: Set<String>? = setOf(),

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)