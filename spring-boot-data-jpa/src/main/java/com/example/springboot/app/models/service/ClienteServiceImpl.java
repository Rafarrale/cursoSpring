package com.example.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.app.models.dao.IClienteDao;
import com.example.springboot.app.models.dao.IFacturaDao;
import com.example.springboot.app.models.dao.IProductoDao;
import com.example.springboot.app.models.entity.Cliente;
import com.example.springboot.app.models.entity.Factura;
import com.example.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService{

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Cliente> findall() {
		return (List<Cliente>)clienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly= true)
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null); 
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		clienteDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly= true)
	public Page<Cliente> findall(org.springframework.data.domain.Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly= true)
	public List<Producto> findByNombre(String term) {
		return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		return productoDao.findById(id).orElse(null);
	}


}
