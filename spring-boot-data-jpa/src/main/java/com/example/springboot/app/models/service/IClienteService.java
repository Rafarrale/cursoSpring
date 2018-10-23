package com.example.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.springboot.app.models.entity.Cliente;


public interface IClienteService {

	public List<Cliente> findall();
	
	public Page<Cliente> findall(Pageable pageable);
	
	public void save (Cliente cliente);
	
	public Cliente findOne(Long id);
	
	public void eliminar(Long id);
}
