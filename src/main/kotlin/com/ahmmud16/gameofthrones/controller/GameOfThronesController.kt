package com.ahmmud16.gameofthrones.controller

import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val BASE_JSON = "application/json;charset=UTF-8"

@Api(value = "/gameofthrones", description = "Handling of creating and retrieving pokemon's")
@RequestMapping(
        path = ["/gameofthrones"],
        produces = [BASE_JSON]
)

@RestController
class GameOfThronesController {

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath : String



}