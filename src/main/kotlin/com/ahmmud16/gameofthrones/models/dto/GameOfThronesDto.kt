package com.ahmmud16.gameofthrones.models.dto

import io.swagger.annotations.ApiModelProperty
import javax.persistence.Column

data class GameOfThronesDto(

        @Column(updatable = false)
        @ApiModelProperty("The id of the character")
        var id: String? = null,
        
        @ApiModelProperty("The name of the character")
        var characterName: String? = null,

        @ApiModelProperty("The house(s) where character belong to")
        var houseName: Set<String>? = null,

        @ApiModelProperty("Royal")
        var royal: Boolean? = null,

        @ApiModelProperty("Character's parents")
        var parents: Set<String>? = null,

        @ApiModelProperty("Killed by")
        var killedBy: Set<String>? = null,

        @ApiModelProperty("Thumb image for character")
        var characterImageThumb: String? = null,

        @ApiModelProperty("Full image for character")
        var characterImageFull: String? = null,

        @ApiModelProperty("Who they killed")
        var killed: Set<String>? = null,

        @ApiModelProperty("Parent of")
        var parentOf: Set<String>? = null,

        @ApiModelProperty("Their siblings")
        var siblings: Set<String>? = null

)