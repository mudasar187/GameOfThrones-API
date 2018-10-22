package com.ahmmud16.gameofthrones.controller

import com.ahmmud16.gameofthrones.models.WrappedResponse
import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import com.ahmmud16.gameofthrones.models.hal.PageDto
import com.ahmmud16.gameofthrones.service.GameOfThronesService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

const val BASE_JSON = "application/json;charset=UTF-8"

@Api(value = "/gameofthrones", description = "Handling of creating and retrieving game of thrones characters's")
@RequestMapping(
        path = ["/gameofthrones"],
        produces = [BASE_JSON]
)

@RestController
class GameOfThronesController {

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath : String

    @Autowired
    private lateinit var gameOfThronesService: GameOfThronesService


    @ApiOperation("Get character's")
    @GetMapping
    fun getAll(@ApiParam("Find a character by full name")
               @RequestParam("characterName", required = false)
               characterName : String?,

               @ApiParam("Search for all character contains the string in their characternames")
               @RequestParam("search", required = false)
               search : String?,

               @ApiParam("Offset in the list of game of thrones characters")
               @RequestParam("offset", defaultValue = "0")
               offset: Int,

               @ApiParam("Limit of game of thrones characters in a single retrieved page")
               @RequestParam("limit", defaultValue = "10")
               limit: Int
    ): ResponseEntity<WrappedResponse<PageDto<GameOfThronesDto>>> {
        return gameOfThronesService.findBy(characterName, search, offset, limit)
    }

    @ApiOperation("Create a new character")
    @PostMapping
    fun post(@RequestBody gameOfThronesDto: GameOfThronesDto) : ResponseEntity<WrappedResponse<PageDto<GameOfThronesDto>>> {
        return gameOfThronesService.createCharacter(gameOfThronesDto);
    }

    @ApiOperation("Get a single character by id")
    @GetMapping(path = ["/{id}"])
    fun get(@ApiParam("The id of character")
        @PathVariable("id")
        id: String?) : ResponseEntity<WrappedResponse<GameOfThronesDto>> {
        return gameOfThronesService.findById(id)
    }

    @ApiOperation("Update all characters with new information")
    @PutMapping(path = ["/{id}"])
    fun update(@ApiParam("The id of character")
        @PathVariable("id")
        id: String?,
               @ApiParam("Character data")
               @RequestBody gameOfThronesDto: GameOfThronesDto) : ResponseEntity<WrappedResponse<GameOfThronesDto>> {
        return gameOfThronesService.update(id, gameOfThronesDto)
    }

    @ApiOperation("Update part of the characters data")
    @PatchMapping(path = ["/{id}"])
    fun patch(@ApiParam("The id of character")
        @PathVariable("id")
        id: String?,
              @ApiParam("The partial patch")
              @RequestBody jsonPatch: String) : ResponseEntity<WrappedResponse<GameOfThronesDto>> {
        return gameOfThronesService.patch(id, jsonPatch);
    }


}