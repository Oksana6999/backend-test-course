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

public class IngredientTest {

    private final static String Api_Key = "1a608945db2c4bc990df4bc27e96ad0a";

    @BeforeAll
    static void beforeAll() {

        baseURI = "https://api.spoonacular.com/food/ingredients/";

    }

    @Test
    void substitutesIngredientTest() throws IOException {
        String actually = given()
                .log()
                .all()
                .param("apiKey", Api_Key)
                .pathParam("id", 1001)
                .expect()
                .log()
                .body()
                .when()
                .get("{id}/substitutes")
                .prettyPrint();
        String expected = getResourceAsString("IngredientTest/substitutes.json");

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(Option.IGNORING_ARRAY_ORDER)
        );
    }

    @Test
    void convertAmountsTest() {
        given()
                .log()
                .all()
                .param("apiKey", Api_Key)
                .param("ingredientName", "flour")
                .param("sourceAmount", "3.5")
                .param("sourceUnit", "cups")
                .param("targetUnit", "grams")
                .expect()
                .log()
                .body()
                .body("sourceAmount", is(3.5F))
                .body("targetAmount", is(437.5F))
                .when()
                .get("https://api.spoonacular.com/recipes/convert")
                .prettyPrint();
    }

    @Test
    void computeGlycemicLoadTest() {
        given()
                .log()
                .all()
                .body("{ \"ingredients\":[ \"1 kiwi\", \"2 cups rice\", \"2 glasses of water\" ] }")
                .expect()
                .log()
                .body()
                .body("code", is(401))
                .body("status", is("failure"))
                .body("message", is("You are not authorized. Please read https://spoonacular.com/food-api/docs#Authentication"))
                .when()
                .post("https://api.spoonacular.com/food/ingredients/glycemicLoad")
                .prettyPrint();

    }

    @Test
    void ingredientSearchTest() {



        given()
                .log()
                .all()
                .param("apiKey", Api_Key)
                .queryParam("query", "fish")
                .queryParam("number", 3)
                .queryParam("sort", "calories")
                .expect()
                .log()
                .body()
                .body("results[0].id", is(99121))
                .body("results[1].name", is("fish seasoning"))
                .body("number", is(3))
                .when()
                .get("search")
                .prettyPrint();

    }

    @Test
    void getSimilarRecipesTest() throws IOException {
        String actually = given()
                .log()
                .all()
                .param("apiKey", Api_Key)
                .pathParam("id", "71553")
                .expect()
                .log()
                .body()
                .when()
                .get("https://api.spoonacular.com/recipes/{id}/similar")
                .prettyPrint();

        String expected = getResourceAsString("GetSimilarRecipesTest/expected.json");

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(Option.IGNORING_ARRAY_ORDER)
        );
    }


    public String getResourceAsString(String resource) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resource);
        assert stream != null;
        byte[] bytes = stream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
