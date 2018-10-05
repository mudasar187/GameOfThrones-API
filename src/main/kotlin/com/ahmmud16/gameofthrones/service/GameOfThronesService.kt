package com.ahmmud16.gameofthrones.service

import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import org.springframework.http.ResponseEntity

interface GameOfThronesService {

    fun createCharactersFromJson(gameOfThrones: List<GameOfThronesDto>) : ResponseEntity<Void>
}