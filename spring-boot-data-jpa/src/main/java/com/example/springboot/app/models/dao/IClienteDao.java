package com.example.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.springboot.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{

	@Query("select c from Cliente c left join fetch c.facturas where c.id=?1") /* left join fetch para que traiga el cliente aunque no tenga facturas */
	public Cliente fetchByIdWithFactura(Long id);
}
