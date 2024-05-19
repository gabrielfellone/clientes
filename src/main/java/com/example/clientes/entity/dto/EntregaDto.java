package com.example.clientes.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EntregaDto(UUID idCliente, UUID idEntregador, String endereco, String cep) {
}
