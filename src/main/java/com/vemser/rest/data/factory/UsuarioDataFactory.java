package com.vemser.rest.data.factory;

import com.vemser.rest.model.UsuarioModel;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Random;

public class UsuarioDataFactory {

    private static Faker faker = new Faker(new Locale("PT-BR"));
    private static Random geradorBoolean = new Random();

    private static UsuarioModel novoUsuario () {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(faker.name().fullName());
        usuario.setEmail(faker.internet().emailAddress());
        usuario.setPassword(faker.internet().password());
        usuario.setAdministrador(String.valueOf(geradorBoolean.nextBoolean()));

        return usuario;
    }
    public static UsuarioModel usuarioValido() {
        return novoUsuario();
    }

    public static UsuarioModel usuarioNomeInexistente() {
        UsuarioModel usuarioNomeInexistente = novoUsuario();
        usuarioNomeInexistente.setNome("Nome que não existe");
        return usuarioNomeInexistente;
    }

    public static UsuarioModel usuarioEmailInvalido() {
        UsuarioModel usuarioEmailInvalido = novoUsuario();
        usuarioEmailInvalido.setEmail("emailinvalido.com");
        return usuarioEmailInvalido;
    }
    public static UsuarioModel usuarioNomeEmBranco() {
        UsuarioModel usuarioNomeEmBranco = novoUsuario();
        usuarioNomeEmBranco.setNome(StringUtils.EMPTY);

        return usuarioNomeEmBranco;
    }

    public static UsuarioModel usuarioComTodosOsDadosEmBranco() {
        UsuarioModel usuario = novoUsuario();
        usuario.setNome(StringUtils.EMPTY);
        usuario.setEmail(StringUtils.EMPTY);
        usuario.setPassword(StringUtils.EMPTY);
        usuario.setAdministrador(StringUtils.EMPTY);

        return usuario;
    }





}