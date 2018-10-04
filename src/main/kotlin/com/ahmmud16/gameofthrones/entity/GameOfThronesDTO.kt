package com.ahmmud16.gameofthrones.entity

import io.swagger.annotations.ApiModelProperty

data class GameOfThronesDTO(

        @ApiModelProperty("The id of the character")
        var id: String? = null,

        @ApiModelProperty("The name of the character")
        var characterName: String? = null,

        @ApiModelProperty("The house(s) where character belong to")
        var houseName: Set<String>? = null,


        @ApiModelProperty("Thumb image for character")
        var characterImageThumb: String? = null,

        @ApiModelProperty("Full image for character")
        var characterImageFull: String? = null,

        @ApiModelProperty("Character's parents")
        var parents: Set<String>? = null,

        @ApiModelProperty("Character is parents of:")
        var parentsOf: Set<String>? = null,

        @ApiModelProperty("Is Royal?")
        var royal: Boolean? = null,

        @ApiModelProperty("Their siblings")
        var siblings: Set<String>? = null,

        @ApiModelProperty("Who they killed")
        var killed: Set<String>? = null

)