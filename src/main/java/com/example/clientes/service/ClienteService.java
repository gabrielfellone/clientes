package com.example.clientes.service;

import com.example.clientes.controller.resources.ClienteRequest;
import com.example.clientes.controller.resources.ClienteResponse;
import com.example.clientes.entity.dto.EntregaDto;
import com.example.clientes.entity.dto.PedidoDto;
import com.example.clientes.entity.Cliente;
import com.example.clientes.exception.ClienteException;
import com.example.clientes.integration.ClienteProducer;
import com.example.clientes.repository.ClienteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ClienteProducer producer;

    public void atualizaClientePorId(ClienteRequest clienteRequest, UUID id) {
        log.info("Atualizando cliente na base");

            Optional<Cliente> getCliente = clienteRepository.findById(id);
            if (getCliente.isPresent()) {
                try {
                    clienteRepository.save(new Cliente(clienteRequest, id));
                } catch (ClienteException e){
                    throw new ClienteException("Erro ao atualizar cliente na base");
                }
            } else throw new ClienteException("Cliente nao encontrado na base!");
    }

    public void deletarClientePorId(UUID id) {
        log.info("Buscando Cliente por ID na Base");
        try {
            Optional<Cliente> getCliente = clienteRepository.findById(id);
            if (getCliente.isPresent()) {
                clienteRepository.deleteById(id);
            } else throw new ClienteException("Cliente nao encontrado na base!");
        } catch (ClienteException e) {
            throw new ClienteException("Erro ao deletar o cliente na base!");
        }
    }

    public void cadastraCliente(ClienteRequest clienteRequest) {
        log.info("Cadastrando cliente na base");
        try {
            clienteRepository.save(new Cliente(clienteRequest));
        } catch (ClienteException e) {
            throw new ClienteException("Erro ao salvar o cliente na base!");
        }
    }
    public ClienteResponse buscaClientePorId(UUID id) {
        log.info("Buscando Cliente por ID na Base");
        try {
            Optional<Cliente> getCliente = clienteRepository.findById(id);
            if (getCliente.isPresent()) {
                return new ClienteResponse(getCliente);
            }
        } catch (ClienteException e) {
            throw new ClienteException("Erro ao buscar no banco de dados o cliente!");
        }
        return null;
    }

    public List<Cliente> buscaTodosClientes() {
        log.info("Buscando Todos os Clientes na Base");
        try {
            List<Cliente> getClientes = clienteRepository.findAll();
            if (!getClientes.isEmpty()) {
                return getClientes;
            }
        } catch (ClienteException e) {
            throw new ClienteException("Erro ao buscar no banco de dados os clientes!");
        }
        return null;
    }

    public void validaCliente(String payload) throws JSONException, JsonProcessingException {

        JSONObject jsonObject = new JSONObject(payload);
        log.info("Json Message Cliente: {} ", jsonObject);

        String id = jsonObject.getString("id");
        String idCliente = jsonObject.getString("idCliente");
        String idProduto = jsonObject.getString("idProduto");

        validaClientePorMessage(UUID.fromString(idCliente),
                PedidoDto.builder().
                        id(UUID.fromString(id))
                        .idCliente(UUID.fromString(idCliente))
                        .idProduto(Long.parseLong(idProduto)).build());

    }

    public void validaClientePorMessage(UUID idCliente, PedidoDto message) throws JsonProcessingException {
        log.info("Validando Cliente se Existe na Base");
        Optional<Cliente> getCliente = clienteRepository.findById(idCliente);
        if (!getCliente.isPresent()) {
            log.info("Cliente não encontrado, postando mensagem de erro na fila de pedido");
            producer.errorClienteFilaPedido(message);
        } else {
            log.info("Cliente Encontrado... postando mensagem para pedido seguir com entrega");
            producer.enviarParaEntregaPedido(message);
        }

    }

    public void validaEntrega(String payload) throws JSONException, JsonProcessingException{

        JSONObject jsonObject = new JSONObject(payload);
        log.info("Json Message Entrega validacao: {} ", jsonObject);

        String idCliente = jsonObject.getString("idCliente");
        String cep = jsonObject.getString("cep");
        String endereco = jsonObject.getString("endereco");
        String idEntregador = jsonObject.getString("idEntregador");

        EntregaDto entregaDto = EntregaDto.builder()
                .idCliente(UUID.fromString(idCliente))
                .cep(cep)
                .idEntregador(UUID.fromString(idEntregador))
                .endereco(endereco).build();

        validaEntregaLocalizacao(entregaDto);

    }

    public void validaEntregaLocalizacao(EntregaDto entregaDto) throws JsonProcessingException {
        log.info("Validandos se a entrega esta mesmo no endereco do cliente");
        Optional<Cliente> getCliente = clienteRepository.findById(entregaDto.idCliente());
        if (getCliente.isPresent()){
            if(getCliente.get().getCep().equalsIgnoreCase(entregaDto.cep())
                && getCliente.get().getEndereco().equalsIgnoreCase(entregaDto.endereco())){
                log.info("Entrega efetuada com sucesso, enviando para fila de pedido sucesso");
                producer.pedidoEntregue(entregaDto);
            } else {
                log.info("Entrega nao bate com endereco do cliente, enviando para fila de pedido nao entregue");
                producer.pedidoNaoEntregue(entregaDto);
            }
        }

    }





}
