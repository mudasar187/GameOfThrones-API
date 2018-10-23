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
        @get:NotNull
        var houseName: MutableSet<String>? = mutableSetOf(),

        var royal: Boolean? = null,

        @get:ElementCollection
        var parents: MutableSet<String>? = mutableSetOf(),

        @get:ElementCollection
        var killedBy: MutableSet<String>? = mutableSetOf(),

        @get:Size(max = 2048)
        var characterImageThumb: String? = null,

        @get:Size(max = 2048)
        var characterImageFull: String? = null,

        @get:ElementCollection
        var killed: MutableSet<String>? = mutableSetOf(),

        @get:ElementCollection
        var parentOf: MutableSet<String>? = mutableSetOf(),

        @get:ElementCollection
        var siblings: MutableSet<String>? = mutableSetOf(),

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)