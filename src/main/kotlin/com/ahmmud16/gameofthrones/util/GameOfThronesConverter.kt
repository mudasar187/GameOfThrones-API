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
                    gameOfThronesDto.characterName!!, checkJsonData("houseName", gameOfThronesDto.houseName!!),
                    gameOfThronesDto.characterImageThumb!!, gameOfThronesDto.characterImageFull!!, checkJsonData("parents", gameOfThronesDto.parents!!),
                    checkJsonData("parentsOf", gameOfThronesDto.parentsOf!!), gameOfThronesDto.royal,
                    checkJsonData("siblings", gameOfThronesDto.siblings!!), checkJsonData("killed", gameOfThronesDto.killed!!)
            )
        }

        fun convertFromDto(gameOfThronesDto: Iterable<GameOfThronesDto>): List<GameOfThrones> {
            return gameOfThronesDto.map { convertFromDto(it) }
        }

//        fun convertToDto(gameOfThrones: GameOfThrones): GameOfThronesDto {
//            return GameOfThronesDto(
//                    gameOfThrones.id.toString(),
//                    gameOfThrones.characterName,
//                    gameOfThrones.houseName,
//                    gameOfThrones.characterImageThumb,
//                    gameOfThrones.characterImageFull,
//                    gameOfThrones.parents,
//                    gameOfThrones.parentsOf,
//                    gameOfThrones.royal,
//                    gameOfThrones.siblings,
//                    gameOfThrones.killed
//            )
//        }

//        fun convertToDto(gameOfThrones: Iterable<GameOfThrones>): List<GameOfThronesDto> {
//            return gameOfThrones.map { convertToDto(it) }
//        }

        private fun checkJsonData(name: String, jsonBody: String): Set<String>? {

            val jackson = ObjectMapper()

            val jsonNode: JsonNode


            try {
                jsonNode = jackson.readValue(jsonBody, JsonNode::class.java)
            } catch (e: Exception) {
                //Invalid JSON data as input
                throw e
            }

            if (jsonNode.has(name)) {
                val name = jsonNode.get(name)

                when {
                    name.isArray -> return name.toSet().map { it.asText() }.toSet()
                    name.isTextual -> return mutableSetOf(name.asText())
                    else -> return null

                }
            } else return null
        }
    }

}