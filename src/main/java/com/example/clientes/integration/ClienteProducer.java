package com.example.clientes.integration;

import com.example.clientes.entity.dto.EntregaDto;
import com.example.clientes.entity.dto.PedidoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void errorClienteFilaPedido(PedidoDto message) throws JsonProcessingException {
        amqpTemplate.convertAndSend(
                "pedido-response-erro-exchange",
                "pedido-response-erro-rout-key",
                objectMapper.writeValueAsString(message)
        );
    }
    public void enviarParaEntregaPedido(PedidoDto message) throws JsonProcessingException {
        amqpTemplate.convertAndSend(
                "pedido-response-sucesso-exchange",
                "pedido-response-sucesso-rout-key",
                objectMapper.writeValueAsString(message)
        );
    }

    public void pedidoEntregue(EntregaDto message) throws JsonProcessingException {
        amqpTemplate.convertAndSend(
                "pedido-entregafinalizada-exchange",
                "pedido-entregafinalizada-rout-key",
                objectMapper.writeValueAsString(message)
        );
    }

    public void pedidoNaoEntregue(EntregaDto message) throws JsonProcessingException {
        amqpTemplate.convertAndSend(
                "pedido-entreganaofinalizada-exchange",
                "pedido-entreganaofinalizada-rout-key",
                objectMapper.writeValueAsString(message)
        );
    }
}
