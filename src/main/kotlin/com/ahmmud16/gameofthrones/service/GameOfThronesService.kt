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
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Throwables
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.validation.ConstraintViolationException
import org.springframework.web.util.UriComponentsBuilder
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

        if (offset < 0 || limit < 1) {
            return ResponseEntity.status(400).body(
                    GameOfThronesResponses(
                            code = 400,
                            message = "Offset has to be a positive number and limit har to be 1 or greater."
                    ).validated()
            )
        }

        val list = if (characterName.isNullOrBlank() && search.isNullOrBlank()) {
            gameOfThronesRepository.findAll()
        } else if (!characterName.isNullOrBlank() && search.isNullOrBlank()) {
            gameOfThronesRepository.findByCharacterName(characterName!!)
        } else if (characterName.isNullOrBlank() && !search.isNullOrBlank()) {
            gameOfThronesRepository.findAllByCharacterNameContainingIgnoreCase(search!!)
        } else {
            return ResponseEntity.status(400).body(
                    GameOfThronesResponses(
                            code = 400,
                            message = "You can only use one of the filters at a time."
                    ).validated()
            )
        }

        if (offset != 0 && offset >= list.count()) {
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

        if (characterName != null) {
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

    fun createCharacter(gameOfThronesDto: GameOfThronesDto): ResponseEntity<WrappedResponse<PageDto<GameOfThronesDto>>> {
        if (gameOfThronesDto.characterName == null)
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
            if (Throwables.getRootCause(e) is ConstraintViolationException) {
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

    fun findById(idNumber: String?): ResponseEntity<WrappedResponse<GameOfThronesDto>> {
        val id: Long

        try {
            id = idNumber!!.toLong()
        } catch (e: Exception) {
            val message: String = if (idNumber.equals("undefined")) {
                "Missing required field: id"
            } else {
                "Invalid id parameter, This should be a numeric string"
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
                                message = "Character with id $id is not found"
                        ).validated()
                )

        return ResponseEntity.ok(
                GameOfThronesResponse(
                        code = 200,
                        message = "Character with id $id was successfully found",
                        data = convertToDto(dto)
                ).validated()
        )
    }

    fun update(idNumber: String?, gameOfThronesDto: GameOfThronesDto): ResponseEntity<WrappedResponse<GameOfThronesDto>> {
        val id: Long

        try {
            id = idNumber!!.toLong()
        } catch (e: Exception) {
            val message: String = if (idNumber.equals("undefined")) {
                "Missing required field id"
            } else {
                "Invalid id parameter, This should be a numeric string"
            }
            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = message
                    ).validated()
            )
        }

        if (!gameOfThronesRepository.existsById(id)) {
            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = "Character with id $id is not found"
                    ).validated()
            )
        }

        if (gameOfThronesDto.characterName == null || gameOfThronesDto.houseName == null
                || gameOfThronesDto.royal == null || gameOfThronesDto.parents == null
                || gameOfThronesDto.killedBy == null || gameOfThronesDto.characterImageThumb == null
                || gameOfThronesDto.characterImageFull == null || gameOfThronesDto.killed == null
                || gameOfThronesDto.parentOf == null || gameOfThronesDto.siblings == null) {

            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = "You are missing one or more required fields"
                    ).validated()
            )
        }

        val gameOfThrones = gameOfThronesRepository.findById(id).get()

        gameOfThrones.characterName = gameOfThronesDto.characterName!!
        gameOfThrones.houseName = gameOfThronesDto.houseName!!
        gameOfThrones.royal = gameOfThronesDto.royal!!
        gameOfThrones.parents = gameOfThronesDto.parents!!
        gameOfThrones.killedBy = gameOfThronesDto.killedBy!!
        gameOfThrones.characterImageThumb = gameOfThronesDto.characterImageThumb!!
        gameOfThrones.characterImageFull = gameOfThronesDto.characterImageFull!!
        gameOfThrones.killed = gameOfThronesDto.killed!!
        gameOfThrones.parentOf = gameOfThronesDto.parentOf!!
        gameOfThrones.siblings = gameOfThronesDto.siblings!!


        gameOfThronesRepository.save(gameOfThrones).id

        return ResponseEntity.status(204).body(
                GameOfThronesResponse(
                        code = 204,
                        data = convertToDto(gameOfThrones)
                ).validated()
        )

    }

    fun patch(idNumber: String?, jsonBody: String): ResponseEntity<WrappedResponse<GameOfThronesDto>> {

        val id: Long

        try {
            id = idNumber!!.toLong()
        } catch (e: Exception) {
            val message: String = if (idNumber.equals("undefined")) {
                "Missing required field id"
            } else {
                "Invalid id parameter, This should be a numeric string"
            }
            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = message
                    ).validated()
            )
        }

        if (!gameOfThronesRepository.existsById(id)) {
            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = "Character with id $id is not found"
                    ).validated()
            )
        }

        val jackson = ObjectMapper()

        val jsonNode: JsonNode

        try {
            jsonNode = jackson.readValue(jsonBody, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).body(
                    GameOfThronesResponse(
                            code = 400,
                            message = "Invalid JSON object"
                    ).validated()
            )
        }

        if (jsonNode.has("id")) {
            return ResponseEntity.status(409).body(
                    GameOfThronesResponse(
                            code = 409,
                            message = "You are not allowed to change the id"
                    ).validated()
            )
        }


        val gameOfThrones = gameOfThronesRepository.findById(id).get()

        if (jsonNode.has("characterName")) {
            val characterName = jsonNode.get("characterName")
            if (characterName.isTextual) {
                gameOfThrones.characterName = characterName.asText()
            } else {
                return jsonFieldErrorMessage("characterName", "String")
            }
        }



        if (jsonNode.has("houseName")) {
            val houseName = jsonNode.get("houseName")
            when {
                houseName.isNull -> gameOfThrones.houseName = null
                houseName.isArray -> gameOfThrones.houseName = houseName.toSet().map { it.asText() }.toSet()
                else -> return jsonFieldErrorMessage("houseName", "Array")
            }
        }

        if (jsonNode.has("royal")) {
            val royal = jsonNode.get("royal")
            if (royal.isBoolean) {
                gameOfThrones.royal = royal.asBoolean()
            } else {
                return jsonFieldErrorMessage("royal", "Boolean")
            }
        }

        if (jsonNode.has("parents")) {
            val parents = jsonNode.get("parents")
            when {
                parents.isNull -> gameOfThrones.parents = null
                parents.isArray -> gameOfThrones.parents = parents.toSet().map { it.asText() }.toSet()
                else -> return jsonFieldErrorMessage("parents", "Array")
            }
        }

        if (jsonNode.has("killedBy")) {
            val killedBy = jsonNode.get("killedBy")
            when {
                killedBy.isNull -> gameOfThrones.killedBy = null
                killedBy.isArray -> gameOfThrones.killedBy = killedBy.toSet().map { it.asText() }.toSet()
                else -> return jsonFieldErrorMessage("killedBy", "Array")
            }
        }

        if (jsonNode.has("characterImageThumb")) {
            val characterImageThumb = jsonNode.get("characterImageThumb")
            if (characterImageThumb.isTextual) {
                gameOfThrones.characterImageThumb = characterImageThumb.asText()
            } else {
                return jsonFieldErrorMessage("characterImageThumb", "String")
            }
        }

        if (jsonNode.has("characterImageFull")) {
            val characterImageFull = jsonNode.get("characterImageFull")
            if (characterImageFull.isTextual) {
                gameOfThrones.characterImageFull = characterImageFull.asText()
            } else {
                return jsonFieldErrorMessage("characterImageFull", "String")
            }
        }

        if (jsonNode.has("killed")) {
            val killed = jsonNode.get("killed")
            when {
                killed.isNull -> gameOfThrones.killed = null
                killed.isArray -> gameOfThrones.killed = killed.toSet().map { it.asText() }.toSet()
                else -> return jsonFieldErrorMessage("killed", "Array")
            }
        }

        if (jsonNode.has("parentOf")) {
            val parentOf = jsonNode.get("parentOf")
            when {
                parentOf.isNull -> gameOfThrones.parentOf = null
                parentOf.isArray -> gameOfThrones.parentOf = parentOf.toSet().map { it.asText() }.toSet()
                else -> return jsonFieldErrorMessage("parentOf", "Array")
            }
        }

        if (jsonNode.has("siblings")) {
            val siblings = jsonNode.get("siblings")
            when {
                siblings.isNull -> gameOfThrones.siblings = null
                siblings.isArray -> gameOfThrones.siblings = siblings.toSet().map { it.asText() }.toSet()
                else -> return jsonFieldErrorMessage("siblings", "Array")
            }
        }


        gameOfThronesRepository.save(gameOfThrones).id

        return ResponseEntity.status(204).body(
                GameOfThronesResponse(
                        code = 204,
                        data = convertToDto(gameOfThrones)
                ).validated()
        )
    }

}

private fun jsonFieldErrorMessage(field: String, type: String): ResponseEntity<WrappedResponse<GameOfThronesDto>> {
    return ResponseEntity.status(400).body(
            GameOfThronesResponse(
                    code = 400,
                    message = "Invalid field type of $field, this should be a valid JSON field of type $type"
            ).validated()
    )
}