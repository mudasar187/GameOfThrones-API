package com.ahmmud16.gameofthrones.models.dto

import io.swagger.annotations.ApiModelProperty

data class GameOfThronesDto(

        @ApiModelProperty("The id of the character")
        var id: String? = null,

        @ApiModelProperty("The name of the character")
        var characterName: String? = null,

        @ApiModelProperty("The house(s) where character belong to")
        var houseName: MutableSet<String>? = null,

        @ApiModelProperty("Royal")
        var royal: Boolean? = null,

        @ApiModelProperty("Character's parents")
        var parents: MutableSet<String>? = null,

        @ApiModelProperty("Killed by")
        var killedBy: MutableSet<String>? = null,

        @ApiModelProperty("Thumb image for character")
        var characterImageThumb: String? = null,

        @ApiModelProperty("Full image for character")
        var characterImageFull: String? = null,

        @ApiModelProperty("Who they killed")
        var killed: MutableSet<String>? = null,

        @ApiModelProperty("Parent of")
        var parentOf: MutableSet<String>? = null,

        @ApiModelProperty("Their siblings")
        var siblings: MutableSet<String>? = null

)