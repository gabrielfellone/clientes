package com.example.clientes.integration;

import com.example.clientes.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
public class ClienteConsumer {

    @Autowired
    private ClienteService clienteService;

    @RabbitListener(queues = {"pedido-cliente-queue"})
    public void validaCliente(@Payload Message message) throws JSONException, JsonProcessingException {
        String payload = String.valueOf(message.getPayload());
        clienteService.validaCliente(payload);
    }

    @RabbitListener(queues = {"pedido-clienteentrega-queue"})
    public void validaEntrega(@Payload Message message) throws JSONException, JsonProcessingException {
        String payload = String.valueOf(message.getPayload());
        clienteService.validaEntrega(payload);
    }
}
