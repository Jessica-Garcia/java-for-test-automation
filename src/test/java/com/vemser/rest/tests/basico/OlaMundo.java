package com.vemser.rest.tests.basico;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
public class OlaMundo {

    @Test
    public void deveBuscarUsuarioPorId(){

        baseURI = "https://reqres.in";
        given()
                .log().all()
                .pathParam("id", 1)
        .when()
                .get("/api/users/{id}")
        .then()
                .log().all()
                .statusCode(200);
    }
}
