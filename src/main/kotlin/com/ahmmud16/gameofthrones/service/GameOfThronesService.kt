package com.ahmmud16.gameofthrones.service

import com.ahmmud16.gameofthrones.models.GameOfThronesResponses
import com.ahmmud16.gameofthrones.models.WrappedResponse
import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.models.hal.HalLink
import com.ahmmud16.gameofthrones.models.hal.PageDto
import com.ahmmud16.gameofthrones.repository.GameOfThronesRepository
import com.ahmmud16.gameofthrones.util.GameOfThronesConverter
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import kotlin.streams.toList

@Service("GameOfThronesService")
class GameOfThronesService(
        val gameOfThronesRepository: GameOfThronesRepository
) {


    fun createCharactersFromJson(gameOfThrones: List<GameOfThronesDto>): ResponseEntity<Void> {
        gameOfThronesRepository.saveAll(GameOfThronesConverter.convertFromDto(gameOfThrones))
        return ResponseEntity.ok().build()
    }

    fun findBy(characterName: String?, search: String?, offset: Int, limit: Int): ResponseEntity<WrappedResponse<PageDto<GameOfThronesDto>>> {

        if (offset < 0 || limit < 1){
            return ResponseEntity.status(400).body(
                    GameOfThronesResponses(
                            code = 400,
                            message = "offset has to be a positive number and limit har to be 1 or greater."
                    ).validated()
            )
        }

        val list = if (characterName.isNullOrBlank() && search.isNullOrBlank()) {
            gameOfThronesRepository.findAll()
        } else if (!characterName.isNullOrBlank() && search.isNullOrBlank()) {
            gameOfThronesRepository.findByCharacterName(characterName!!)
        } else if(characterName.isNullOrBlank() && !search.isNullOrBlank()) {
              gameOfThronesRepository.findAllByCharacterNameContainingIgnoreCase(search!!)
        } else {
            return ResponseEntity.status(400).body(
                    GameOfThronesResponses(
                            code = 400,
                            message = "You can only use one of the filters at a time."
                    ).validated()
            )
        }

        if (offset != 0 && offset >= list.count()){
            return ResponseEntity.status(400).body(
                    GameOfThronesResponses(
                            code = 400,
                            message = "Your offset is larger than the number of elements returned by your request."
                    )
            )
        }

        val convertedList = list.toList()
                .stream()
                .skip(offset.toLong())
                .limit(limit.toLong())
                .map { GameOfThronesConverter.convertToDto(it) }
                .toList().toMutableList()

        val dto = PageDto<GameOfThronesDto>(convertedList, offset, limit, list.count())

        var uriBuilder = UriComponentsBuilder
                .fromPath("/gameofthrones")
                .queryParam("limit", limit)

        if (characterName != null){
            uriBuilder = uriBuilder.queryParam("characterName", characterName)
        }

        dto._self = HalLink(uriBuilder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString())

        if (!convertedList.isEmpty() && offset > 0) {
            dto.previous = HalLink(uriBuilder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString())
        }

        if (offset + limit < list.count()) {
            dto.next = HalLink(uriBuilder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString())
        }

        return ResponseEntity.ok(
                GameOfThronesResponses(
                        code = 200,
                        data = dto
                ).validated()
        )
    }
}