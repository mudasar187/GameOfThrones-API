package com.ahmmud16.gameofthrones.repository

import com.ahmmud16.gameofthrones.entity.GameOfThrones
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameOfThronesRepository : CrudRepository<GameOfThrones, Long> {

}