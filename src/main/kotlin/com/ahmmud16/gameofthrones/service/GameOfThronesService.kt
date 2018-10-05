package com.ahmmud16.gameofthrones.service

import com.ahmmud16.gameofthrones.models.WrappedResponse
import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import org.springframework.http.ResponseEntity

interface GameOfThronesService {

    fun createGameOfThronesCharacters(gameOfThronesDto: GameOfThronesDto) : ResponseEntity<WrappedResponse<GameOfThronesDto>>
}