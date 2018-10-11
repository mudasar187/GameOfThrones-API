package com.ahmmud16.gameofthrones.util


import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.models.entity.GameOfThrones
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.Exception


class GameOfThronesConverter {

    companion object {



        fun convertFromDto(gameOfThronesDto: GameOfThronesDto): GameOfThrones {
            return GameOfThrones(
                    gameOfThronesDto.characterName!!, gameOfThronesDto.houseName, gameOfThronesDto.royal,
                    gameOfThronesDto.parents, gameOfThronesDto.killedBy, gameOfThronesDto.characterImageThumb, gameOfThronesDto.characterImageFull,
                    gameOfThronesDto.killed, gameOfThronesDto.parentOf, gameOfThronesDto.siblings
            )
        }

        fun convertFromDto(gameOfThronesDto: Iterable<GameOfThronesDto>): List<GameOfThrones> {
            return gameOfThronesDto.map { convertFromDto(it) }
        }

        fun convertToDto(gameOfThrones: GameOfThrones) : GameOfThronesDto {
            return GameOfThronesDto(
                    gameOfThrones.id.toString(),
                    gameOfThrones.characterName,
                    gameOfThrones.houseName,
                    gameOfThrones.royal,
                    gameOfThrones.parents,
                    gameOfThrones.killedBy,
                    gameOfThrones.characterImageThumb,
                    gameOfThrones.characterImageFull,
                    gameOfThrones.killed,
                    gameOfThrones.parentOf,
                    gameOfThrones.siblings
            )
        }

        fun covertToDto(gameOfThrones: Iterable<GameOfThrones>) : List<GameOfThronesDto> {
            return gameOfThrones.map { convertToDto(it) }
        }
    }

}