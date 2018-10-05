package com.ahmmud16.gameofthrones.service

import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.repository.GameOfThronesRepository
import com.ahmmud16.gameofthrones.util.GameOfThronesConverter.Companion.convertFromDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service("GameOfThronesService")
class GameOfThronesServiceImp : GameOfThronesService {

    @Autowired
    private lateinit var gameOfThronesRepository: GameOfThronesRepository

    override fun createCharactersFromJson(gameOfThrones: List<GameOfThronesDto>): ResponseEntity<Void> {
        gameOfThronesRepository.saveAll(convertFromDto(gameOfThrones))
        return ResponseEntity.ok().build()
    }

}