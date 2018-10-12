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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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


    @ApiOperation("Get all characters's")
    @GetMapping
    fun getAll(@ApiParam("The name of the character")
               @RequestParam("characterName", required = false)
               characterName : String?,

               @ApiParam("Offset in the list of game of thrones characters")
               @RequestParam("offset", defaultValue = "0")
               offset: Int,

               @ApiParam("Limit of game of thrones characters in a single retrieved page")
               @RequestParam("limit", defaultValue = "10")
               limit: Int
    ): ResponseEntity<WrappedResponse<PageDto<GameOfThronesDto>>> {
        return gameOfThronesService.findBy(characterName, offset, limit)
    }


}