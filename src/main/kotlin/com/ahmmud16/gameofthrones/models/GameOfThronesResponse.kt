package com.ahmmud16.gameofthrones.models

import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto

class GameOfThronesResponse(
        code: Int? = null,
        data: GameOfThronesDto? = null,
        message: String? = null,
        status: WrappedResponse.ResponseStatus? = null
) : WrappedResponse<GameOfThronesDto>(code, data, message, status)