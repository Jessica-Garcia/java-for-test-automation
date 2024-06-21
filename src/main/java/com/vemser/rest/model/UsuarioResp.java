package com.vemser.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuarioResp extends UsuarioModel {

    private String message;
    @JsonProperty("_id")
    private String id;

}
