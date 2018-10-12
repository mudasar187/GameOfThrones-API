package com.ahmmud16.gameofthrones.repository

import com.ahmmud16.gameofthrones.models.entity.GameOfThrones
import com.ahmmud16.gameofthrones.service.GameOfThronesService
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameOfThronesRepository : CrudRepository<GameOfThrones, Long> {

    fun findAllByCharacterName(characterName: String): Iterable<GameOfThrones>
}