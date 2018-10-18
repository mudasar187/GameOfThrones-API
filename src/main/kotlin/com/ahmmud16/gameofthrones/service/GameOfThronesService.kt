package com.ahmmud16.gameofthrones.service

import com.ahmmud16.gameofthrones.models.GameOfThronesResponse
import com.ahmmud16.gameofthrones.models.GameOfThronesResponses
import com.ahmmud16.gameofthrones.models.WrappedResponse
import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.models.hal.HalLink
import com.ahmmud16.gameofthrones.models.hal.PageDto
import com.ahmmud16.gameofthrones.repository.GameOfThronesRepository
import com.ahmmud16.gameofthrones.util.GameOfThronesConverter
import com.ahmmud16.gameofthrones.util.GameOfThronesConverter.Companion.convertToDto
import com.google.common.base.Throwables
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Exception
import javax.validation.ConstraintViolationException
import kotlin.streams.toList

@Service("GameOfThronesService")
class GameOfThronesService(
        val gameOfThronesRepository: GameOfThronesRepository
) {


    fun createCharactersFromJson(gameOfThrones: List<GameOfThronesDto>): ResponseEntity<Void> {
        gameOfThronesRepository.saveAll(GameOfThronesConverter.convertFromDtoMap(gameOfThrones))
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
                .map { convertToDto(it) }
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

    fun createCharacter(gameOfThronesDto: GameOfThronesDto) : ResponseEntity<WrappedResponse<PageDto<GameOfThronesDto>>> {
        if(gameOfThronesDto.characterName == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GameOfThronesResponses(
                            code = HttpStatus.BAD_REQUEST.value(),
                            message = "You must fill full name of the character"
                    ).validated()
            )

        val id: Long?

        try {
            id = gameOfThronesRepository.save(GameOfThronesConverter.convertFromDto(gameOfThronesDto)).id
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        GameOfThronesResponses(
                                code = HttpStatus.BAD_REQUEST.value(),
                                message = "Unable to create a new character due to constraint violation in the submitted DTO"
                        ).validated()
                )
            }
            throw e
        }

        val dto = PageDto(
                list = mutableListOf(GameOfThronesDto(id = id.toString())),
                totalSize = 1
        )

        val uriBuilder = UriComponentsBuilder
                .fromPath("/gotrest/api/${id.toString()}")

        dto._self = HalLink(uriBuilder.cloneBuilder().build().toString())

        return ResponseEntity.status(HttpStatus.CREATED).body(
                GameOfThronesResponses(
                        code = HttpStatus.CREATED.value(),
                        message = "Successfully created new character",
                        data = dto
                ).validated()
        )
    }

    fun findById(idNumber: String?) : ResponseEntity<WrappedResponse<GameOfThronesDto>> {
        val id: Long

        try {
            id = idNumber!!.toLong()
        } catch (e: Exception) {
            val message: String = if(idNumber.equals("undefined")) {
                "Missing required field: idNumber"
            } else {
                "Invalid idNumber parameter, This should be a numeric string"
            }
            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = message
                    ).validated()
            )
        }

        val dto = gameOfThronesRepository
                .findById(id)
                .orElse(null) ?: return ResponseEntity
                .status(404)
                .body(
                        GameOfThronesResponse(
                                code = 400,
                                message = "Character with id -> $id is not found"
                        ).validated()
                )

        return ResponseEntity.ok(
                GameOfThronesResponse(
                        code = 200,
                        message = "Character with id: $id was successfully found",
                        data = convertToDto(dto)
                ).validated()
        )
    }
}