package com.ahmmud16.gameofthrones.models.hal

import io.swagger.annotations.ApiModelProperty

open class HalLink (

        @ApiModelProperty("URL of the link")
        var href: String = ""
)
