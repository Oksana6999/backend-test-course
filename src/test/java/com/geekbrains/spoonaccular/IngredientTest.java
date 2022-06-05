package com.geekbrains.spoonaccular;

import com.geekbrains.IngredientResponse;
import com.geekbrains.IngredientSearchItem;
import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IngredientTest extends SpoonaccularTest {



    @Test
    void substitutesIngredientTest() throws Exception {
        String actually = given()
                .pathParam("id", 1001)
                .expect()
                .when()
                .get("food/ingredients/{id}/substitutes")
                .prettyPrint();
        String expected = getResource("substitutes.json");

       assertJson(expected, actually);
    }

    @Test
    void convertAmountsTest() {
        given()
                .param("ingredientName", "flour")
                .param("sourceAmount", "3.5")
                .param("sourceUnit", "cups")
                .param("targetUnit", "grams")
                .expect()
                .body("sourceAmount", is(3.5F))
                .body("targetAmount", is(437.5F))
                .when()
                .get("recipes/convert")
                .prettyPrint();
    }

    @Test
    void computeGlycemicLoadTest() {

        given()
                .body("{ \"ingredients\":[ \"1 kiwi\", \"2 cups rice\", \"2 glasses of water\" ] }")
                .expect()
                .body("status", is("success"))
                .when()
                .post("food/ingredients/glycemicLoad")
                .prettyPrint();

    }

    @Test
    void ingredientSearchTest() {

        given()
                .queryParam("query", "fish")
                .queryParam("number", 3)
                .queryParam("sort", "calories")
                .expect()
                .body("results[0].id", is(99121))
                .body("results[1].name", is("fish seasoning"))
                .body("number", is(3))
                .when()
                .get("food/ingredients/search")
                .prettyPrint();

    }

    @Test
    void getSimilarRecipesTest() throws Exception {
        String actually = given()
                .pathParam("id", "71553")
                .expect()
                .when()
                .get("recipes/{id}/similar")
                .prettyPrint();

        String expected = getResource("similarExpected.json");

        assertJson(expected, actually);
    }


}
