package com.example.clientes.entity;

import com.example.clientes.controller.resources.ClienteRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("cliente")
public class Cliente {

    @Column
    @PrimaryKey
    private UUID id;
    private String nome;
    private String endereco;
    private String cep;
    private String cpf;
    private String email;

    public Cliente(ClienteRequest request){
        this.id = UUID.randomUUID();
        this.nome = request.getNome();
        this.endereco = request.getEndereco();
        this.cep = request.getCep();
        this.email = request.getEmail();
        this.cpf = request.getCpf();
    }

    public Cliente(ClienteRequest request, UUID id){
        this.id = id;
        this.nome = request.getNome();
        this.endereco = request.getEndereco();
        this.cep = request.getCep();
        this.email = request.getEmail();
        this.cpf = request.getCpf();
    }


}
