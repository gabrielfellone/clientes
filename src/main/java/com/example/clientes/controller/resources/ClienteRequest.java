package com.example.clientes.controller.resources;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ClienteRequest {

    private String nome;
    private String endereco;
    private String cep;
    private String cpf;
    private String email;

}
