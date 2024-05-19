package com.example.clientes.repository;

import com.example.clientes.entity.Cliente;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface ClienteRepository extends CassandraRepository<Cliente, UUID> {
}
