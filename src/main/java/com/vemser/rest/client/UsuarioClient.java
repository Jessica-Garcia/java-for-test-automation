package com.vemser.rest.client;

import com.vemser.rest.model.UsuarioModel;
import com.vemser.rest.model.UsuarioResp;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class UsuarioClient {

    private static final String USUARIOS = "/usuarios";
    private static final String GET_USUARIO = "/usuarios/{id}";
    public UsuarioClient() {
    }

    public Response listarUsuarios() {
        return
            given()
                .when()
                .get(USUARIOS)
            ;
    }

    public Response buscarUsuarioPorID(String id) {

        return
            given()
                .pathParam("id", id)
            .when()
                .get(GET_USUARIO)
            ;
    }

    public Response buscarUsuarioPorNome(String nome) {

        return
            given()
                .queryParam("nome", nome)
            .when()
                .get(USUARIOS)
            ;
    }

    public Response excluirUsuario(String id) {

        return
            given()
                .pathParam("id", id)
            .when()
                .delete(GET_USUARIO)
            ;
    }

    public Response cadastrarUsuario(UsuarioModel usuario) {

        return
            given()
                .contentType(ContentType.JSON)
                .body(usuario)
            .when()
                .post(USUARIOS)
            ;
    }

    public Response atualizarUsuario(String id, UsuarioModel usuario) {

        return
            given()
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .body(usuario)
            .when()
                .put(GET_USUARIO)
            ;
    }



}
