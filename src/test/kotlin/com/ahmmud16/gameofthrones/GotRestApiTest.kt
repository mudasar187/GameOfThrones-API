package com.ahmmud16.gameofthrones

import com.ahmmud16.gameofthrones.models.GameOfThronesResponse
import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import junit.framework.Assert.*
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.springframework.http.HttpStatus

class GotRestApiTest : GotTestBase() {

    @Test
    fun testCleanDB() {

            given()
                    .get().then()
                    .statusCode(200)
                    .body("data.list.size()", equalTo(0))

    }

    @Test
    fun testCreateMultipleCharacters() {

        assertResultSize(0)
        createMultiple(5)
        assertResultSize(5)
    }

    @Test
    fun testCreateCharacter() {

        assertResultSize(0)

        val houseName = mutableSetOf("houseName")

        val dto = GameOfThronesDto(
                characterName = "John Doe",
                houseName = houseName,
                characterImageFull = "imageFull",
                characterImageThumb = "imageThumb"
        )

        val id = createCharacter(dto)

        assertResultSize(1)

        val result = given().param("id", id)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getMap<String, Any>("data.list[0]")

        assertEquals(result["characterName"], "John Doe")
        assertEquals(result["characterImageFull"], "imageFull")
        assertEquals(result["characterImageThumb"], "imageThumb")

    }

    @Test
    fun testDeleteCharacter() {

        assertResultSize(0)

        val houseName = mutableSetOf("houseName")

        val dtoOne = GameOfThronesDto(
                characterName = "John Doe",
                houseName = houseName,
                characterImageFull = "imageFull",
                characterImageThumb = "imageThumb"
        )

        val dtoTwo = GameOfThronesDto(
                characterName = "Foo Bar",
                houseName = houseName,
                characterImageFull = "imageFull",
                characterImageThumb = "imageThumb"
        )

        val dtoOneId = createCharacter(dtoOne)
        createCharacter(dtoTwo)

        assertResultSize(2)

        given().delete(dtoOneId.data!!.list[0].id).then().statusCode(204)

        assertResultSize(1)

    }

    @Test
    fun testUpdateCharacter() {

        assertResultSize(0)

        val houseName = mutableSetOf("houseName")

        val dto = GameOfThronesDto(
                characterName = "John Doe",
                houseName = houseName,
                characterImageFull = "imageFull",
                characterImageThumb = "imageThumb"
        )

        val id = createCharacter(dto).data!!.list[0].id

        assertResultSize(1)

        val result = given().param("id", id)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getMap<String, Any>("data.list[0]")

        assertEquals(result["characterName"], "John Doe")
        assertEquals(result["characterImageFull"], "imageFull")
        assertEquals(result["characterImageThumb"], "imageThumb")

        // Update

        val newCharacterName = "newCharacterName"
        val newHouseName = mutableSetOf("newHouseName")
        val newCharacterImageFull = "newCharacterImageFull"
        val newCharacterImageThumb = "newCharacterImageThumb"

        val royal = true
        val parents = mutableSetOf("parents")
        val killedBy = mutableSetOf("killedBy")
        val killed = mutableSetOf("killed")
        val parentOf = mutableSetOf("parentOf")
        val siblings = mutableSetOf("siblings")

        val newDto = GameOfThronesDto(
                characterName = newCharacterName,
                houseName = newHouseName,
                royal = royal,
                parents = parents,
                killedBy = killedBy,
                characterImageFull = newCharacterImageFull,
                characterImageThumb = newCharacterImageThumb,
                killed = killed,
                parentOf = parentOf,
                siblings = siblings
        )

        val update = given()
                .contentType(ContentType.JSON)
                .body(newDto)
                .put("/${id}")
                .then()
                .statusCode(201)
                .extract()
                .`as`(GameOfThronesResponse::class.java)
                .data!!.list[0]

        assertNotSame(dto.characterName, update.characterName)
        assertNotSame(dto.houseName, update.houseName)
        assertNotSame(dto.characterImageFull, update.characterImageFull)
        assertNotSame(dto.characterImageThumb, update.characterImageThumb)

    }
}