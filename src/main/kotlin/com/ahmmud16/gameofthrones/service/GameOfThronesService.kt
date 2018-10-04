package com.ahmmud16.gameofthrones.service

import com.ahmmud16.gameofthrones.entity.GameOfThronesDTO
import org.springframework.http.ResponseEntity

interface GameOfThronesService {

    fun createGameOfThronesCharacters(gameOfThronesDTO: GameOfThronesDTO) : ResponseEntity<>
}