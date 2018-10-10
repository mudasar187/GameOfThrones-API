package com.ahmmud16.gameofthrones.models.dto

import io.swagger.annotations.ApiModelProperty

data class GameOfThronesDto(

        @ApiModelProperty("The id of the character")
        var id: String? = null,

        @ApiModelProperty("The name of the character")
        var characterName: String? = null,

        @ApiModelProperty("The house(s) where character belong to")
        var houseName: Any? = null,

        @ApiModelProperty("Thumb image for character")
        var characterImageThumb: String? = null,

        @ApiModelProperty("Full image for character")
        var characterImageFull: String? = null,

        @ApiModelProperty("Character's parents")
        var parents: Any? = null,

        @ApiModelProperty("Character is parents of:")
        var parentsOf: Any? = null,

        @ApiModelProperty("Is Royal?")
        var royal: Boolean? = null,

        @ApiModelProperty("Their siblings")
        var siblings: Any? = null,

        @ApiModelProperty("Who they killed")
        var killed: Any? = null

)