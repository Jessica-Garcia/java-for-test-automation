package com.vemser.rest.tests.usuarios;

import net.datafaker.Faker;
import java.util.Locale;
import java.util.Random;
import org.hamcrest.Matchers;

import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.UsuarioModel;
import com.vemser.rest.model.UsuarioResp;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UsuarioTest {

    private final UsuarioClient usuarioClient = new UsuarioClient();

    @BeforeEach
    public void setUp() {
        baseURI = "http://localhost:3000";
    }

    public UsuarioResp cadastrarUsuarioAcessorio() {

        UsuarioResp result = usuarioClient.cadastrarUsuario(UsuarioDataFactory.usuarioValido())
                .then()
                .extract()
                .as(UsuarioResp.class);

        return result;
    }

    public UsuarioResp buscarUsuarioPorIDAcessorio() {

        UsuarioResp result = usuarioClient.buscarUsuarioPorID(cadastrarUsuarioAcessorio().getId())
                .then()
                .extract()
                .as(UsuarioResp.class);

        return result;
    }

    @Test
    public void ListarTodosUsuariosComSucesso() {

        usuarioClient.listarUsuarios()
                .then()
                .statusCode(200);
    }

    @Test
    public void buscarUsuarioPorIdComSucesso() {
        UsuarioResp usuarioCadastrado = buscarUsuarioPorIDAcessorio();
        UsuarioResp result = usuarioClient.buscarUsuarioPorID(usuarioCadastrado.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UsuarioResp.class);

        Assertions.assertAll("result",
                () -> Assertions.assertEquals(usuarioCadastrado.getNome(), result.getNome()),
                () -> Assertions.assertEquals(usuarioCadastrado.getEmail(), result.getEmail()));
    }

    @Test
    public void tentarBuscarUsuarioComIdInexistente() {

        UsuarioResp usuarioCadastrado = cadastrarUsuarioAcessorio();
        String id = usuarioCadastrado.getId();

        usuarioClient.excluirUsuario(id);

        UsuarioResp result = usuarioClient.buscarUsuarioPorID(id)
                .then()
                .statusCode(400)
                .extract()
                .as(UsuarioResp.class);

        Assertions.assertEquals(result.getMessage(), "Usuário não encontrado");
    }

    @Test
    public void tentarBuscarUsuarioComNomeInexistente() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioNomeInexistente();

        usuarioClient.buscarUsuarioPorNome(usuario.getNome())
                .then()
                .statusCode(200)
                .body("quantidade", Matchers.equalTo(0));
    }

    @Test
    public void cadastrarUsuarioComSucesso() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioValido();

        usuarioClient.cadastrarUsuario(usuario)
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }

    @Test
    public void tentarCadastrarUsuarioComNomeEmBranco() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioNomeEmBranco();

        usuarioClient.cadastrarUsuario(usuario)
                .then()
                .log().all()
                .statusCode(400)
                .body("nome", equalTo("nome não pode ficar em branco"));
    }

    @Test
    public void tentarCadastrarUsuarioComDadosEmBranco() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioComTodosOsDadosEmBranco();

        UsuarioResp result = usuarioClient.cadastrarUsuario(usuario)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(UsuarioResp.class);

        Assertions.assertAll("result",
                () -> Assertions.assertEquals("nome não pode ficar em branco", result.getNome()),
                () -> Assertions.assertEquals("email não pode ficar em branco", result.getEmail()),
                () -> Assertions.assertEquals("password não pode ficar em branco", result.getPassword()),
                () -> Assertions.assertEquals("administrador deve ser 'true' ou 'false'", result.getAdministrador()));
    }

    @Test
    public void tentarCadastrarUsuarioComEmailInvalido() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioEmailInvalido();

        usuarioClient.cadastrarUsuario(usuario)
                .then()
                .log().all()
                .statusCode(400)
                .body("email", Matchers.equalTo("email deve ser um email válido"));
    }

    @Test
    public void tentarCadastrarUsuarioJaCadastrado() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioValido();
        usuarioClient.cadastrarUsuario(usuario);
        String email = usuario.getEmail();

        UsuarioModel novoUsuario = UsuarioDataFactory.usuarioValido();
        novoUsuario.setEmail(email);

        UsuarioResp result = usuarioClient.cadastrarUsuario(novoUsuario)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(UsuarioResp.class);

        Assertions.assertEquals("Este email já está sendo usado", result.getMessage());

    }

    @Test
    public void atualizarUsuarioComSucesso() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioValido();
        String idUsuarioCadastrado = usuarioClient.cadastrarUsuario(usuario).then().extract().as(UsuarioResp.class)
                .getId();

        usuario.setNome("Outro Nome");

        usuarioClient.atualizarUsuario(idUsuarioCadastrado, usuario)
                .then()
                .statusCode(200)
                .body("message", Matchers.equalTo("Registro alterado com sucesso"));

    }

    @Test
    public void tentarAtualizarUsuarioComEmailInvalido() {

        UsuarioModel usuario = UsuarioDataFactory.usuarioValido();
        String idUsuarioCadastrado = usuarioClient.cadastrarUsuario(usuario).then().extract().as(UsuarioResp.class)
                .getId();

        usuario.setEmail("emailinvalido");

        usuarioClient.atualizarUsuario(idUsuarioCadastrado, usuario)
                .then()
                .statusCode(400)
                .body("email", Matchers.equalTo("email deve ser um email válido"));

    }

    @Test
    public void atualizarUsuarioComIdInexistente() {

    }

    @Test
    public void excluirUsuarioComSucesso() {

        UsuarioResp usuarioCadastrado = cadastrarUsuarioAcessorio();

        String id = usuarioCadastrado.getId();

        UsuarioResp result = usuarioClient.excluirUsuario(id)
                .then()
                .statusCode(200)
                .extract()
                .as(UsuarioResp.class);
        Assertions.assertEquals("Registro excluído com sucesso", result.getMessage());
    }

    @Test
    public void tentarExcluirUsuarioComIdInexistente() {
        String id = "6oRO4lZkIlEi6hcQ";

        usuarioClient.excluirUsuario(id)
                .then()
                .statusCode(200)
                .body("message", Matchers.equalTo("Nenhum registro excluído"));
    }

    @Test
    public void tentarExcluirUsuarioComCarrinho() {
        String id = "09BEUbEl2SnzuKBk";

        given()
                .pathParam("id", id)
                .when()
                .delete("/usuarios/{id}")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Não é permitido excluir usuário com carrinho cadastrado"))
                .body("idCarrinho", Matchers.notNullValue());
    }

}
