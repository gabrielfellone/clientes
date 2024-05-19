package com.example.clientes.controller;

import com.example.clientes.controller.resources.ClienteRequest;
import com.example.clientes.controller.resources.ClienteResponse;
import com.example.clientes.entity.Cliente;
import com.example.clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/v1/cliente")
@RequiredArgsConstructor
public class ClientesController {

    private final ClienteService clienteService;

    @PostMapping("cadastraCliente")
    public ResponseEntity<String> cadastraCliente(@RequestBody ClienteRequest request) {
        log.info("Cadastrando um cliente {}", request);
        clienteService.cadastraCliente(request);
        return ResponseEntity.status(CREATED).body("Cadastro do cliente efetuado com sucesso!");
    }

    @GetMapping("buscaClientePorId")
    public ResponseEntity<ClienteResponse> buscaClientePorId(@RequestParam(value = "id", required = true) UUID id) {
        log.info("Buscando cliente por id {}", id);
        return ResponseEntity.ok(clienteService.buscaClientePorId(id));
    }

    @GetMapping("buscaTodosClientes")
    public ResponseEntity<List<Cliente>> buscaTodosClientes() {
        log.info("Buscando todos os clientes na base");
        return ResponseEntity.ok(clienteService.buscaTodosClientes());
    }

    @PutMapping("atualizaClientePorId")
    public ResponseEntity<String> atualizaClientePorId(@RequestBody ClienteRequest request,
                                                             @RequestParam(value = "id", required = true) UUID id) {
        log.info("Atualizando cliente por id {}", id);
        clienteService.atualizaClientePorId(request, id);
        return ResponseEntity.status(ACCEPTED).body("Update do cliente efetuado com sucesso!");
    }

    @DeleteMapping("deletarClientePorId")
    public ResponseEntity<String> atualizaClientePorId(@RequestParam(value = "id", required = true) UUID id) {
        log.info("Deletando cliente por id {}", id);
        clienteService.deletarClientePorId(id);
        return ResponseEntity.status(OK).body("Cliente deletado com sucesso!");
    }
}
