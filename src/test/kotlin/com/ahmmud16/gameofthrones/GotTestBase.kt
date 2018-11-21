package com.ahmmud16.gameofthrones

import com.ahmmud16.gameofthrones.models.GameOfThronesResponse
import com.ahmmud16.gameofthrones.models.dto.GameOfThronesDto
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(GameofthronesApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class GotTestBase {

    @LocalServerPort
    protected var port = 0

    @Before
    @After
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/gotrest/api/gameofthrones"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        /*
           Here, we read each resource (GET), and then delete them
           one by one (DELETE)
         */
        val list = given().accept(ContentType.JSON)
                .param("limit", 400)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .`as`(GameOfThronesResponse::class.java)


        list.data!!.list.forEach {
            given()
                    .delete("/${ it.id }")
                    .then()
                    .statusCode(204)
        }

        given()
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(0))

    }

    /*
        Help methods for tests
     */

    fun createCharacter(dto: GameOfThronesDto) : GameOfThronesResponse {

        return given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .`as`(GameOfThronesResponse::class.java)
    }

    fun createMultiple(n: Int) {

        val houseName = mutableSetOf("houseName")

        val dto = GameOfThronesDto(
                characterName = "defaultName",
                houseName = houseName,
                characterImageFull = "defaultImageFull",
                characterImageThumb = "defaultImageThumb"
        )

        for(i in 1..n) {
            createCharacter(dto)
        }
    }

    fun assertResultSize(size: Int) {
        given()
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(size))
    }




}