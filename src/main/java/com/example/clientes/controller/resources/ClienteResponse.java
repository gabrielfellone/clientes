package com.example.clientes.controller.resources;

import com.example.clientes.entity.Cliente;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;


@Data
@NoArgsConstructor
public class ClienteResponse {

    private String nome;
    private String endereco;
    private String cep;
    private String cpf;
    private String email;

    public ClienteResponse(Optional<Cliente> cliente){
        this.nome = cliente.get().getNome();
        this.endereco = cliente.get().getEndereco();
        this.cep = cliente.get().getCep();
        this.cpf = cliente.get().getCpf();
        this.email = cliente.get().getEmail();

    }



}
