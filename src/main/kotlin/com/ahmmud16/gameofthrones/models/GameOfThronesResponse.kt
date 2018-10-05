package com.ahmmud16.gameofthrones.models

import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.models.hal.PageDto

class GameOfThronesResponse (
        code: Int? = null,
        data: PageDto<GameOfThronesDto>? = null,
        message: String? = null,
        status: ResponseStatus? = null

) : WrappedResponse<PageDto<GameOfThronesDto>>(code, data, message, status)