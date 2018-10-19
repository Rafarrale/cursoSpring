package com.example.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.springboot.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{

}
