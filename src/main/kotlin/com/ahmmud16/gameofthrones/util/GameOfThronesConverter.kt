package com.ahmmud16.gameofthrones.util

import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.models.entity.GameOfThrones


class GameOfThronesConverter {

    companion object {


        fun convertFromDto(gameOfThronesDto: GameOfThronesDto): GameOfThrones {
            return GameOfThrones(
                    gameOfThronesDto.characterName!!, gameOfThronesDto.houseName, gameOfThronesDto.characterImageThumb!!,
                    gameOfThronesDto.characterImageFull!!, gameOfThronesDto.parents, gameOfThronesDto.parentsOf,
                    gameOfThronesDto.royal, gameOfThronesDto.siblings, gameOfThronesDto.killed
            )
        }

        fun convertFromDto(gameOfThronesDto: Iterable<GameOfThronesDto>): List<GameOfThrones> {
            return gameOfThronesDto.map { convertFromDto(it) }
        }

        fun convertToDto(gameOfThrones: GameOfThrones): GameOfThronesDto {
            return GameOfThronesDto(
                    gameOfThrones.id.toString(),
                    gameOfThrones.characterName,
                    gameOfThrones.houseName,
                    gameOfThrones.characterImageThumb,
                    gameOfThrones.characterImageFull,
                    gameOfThrones.parents,
                    gameOfThrones.parentsOf,
                    gameOfThrones.royal,
                    gameOfThrones.siblings,
                    gameOfThrones.killed
            )
        }

        fun convertToDto(gameOfThrones: Iterable<GameOfThrones>) : List<GameOfThronesDto> {
            return gameOfThrones.map { convertToDto(it) }
        }
    }

}