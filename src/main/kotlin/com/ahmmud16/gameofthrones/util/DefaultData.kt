package com.ahmmud16.gameofthrones.util

import com.ahmmud16.gameofthrones.entity.GameOfThronesDTO
import com.ahmmud16.gameofthrones.service.GameOfThronesService
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.FileReader
import javax.annotation.PostConstruct

@Component
class DefaultData {

    @Autowired
    private lateinit var gameOfThronesService: GameOfThronesService

    @PostConstruct
    fun initializeDefault() {
        val reader = JsonReader(FileReader("game-of-thrones.json"))
        val gameofthronesCharacters: List<GameOfThronesDTO> = Gson().fromJson(reader, object : TypeToken<List<GameOfThronesDTO>>() {}.type)

        gameofthronesCharacters.forEach{ println(it) }


    }
}