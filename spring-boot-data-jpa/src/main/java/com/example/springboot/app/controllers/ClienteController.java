package com.example.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springboot.app.models.entity.Cliente;
import com.example.springboot.app.models.service.IClienteService;
import com.example.springboot.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente") // GUarda el objeto del formulario dentro del session atributes
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String UPLOADS_FOLDER = "uploads";

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Path pathFoto = Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;
		try {
			recurso = new UrlResource(pathFoto.toUri());
			if (!recurso.exists() || !recurso.isReadable()) {
				throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(id);
		if (cliente == null) {
			flash.addAttribute("error", "El cliente no existe en la Base de Datos");
			return ("redirect/listar");
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findall(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Crear Cliente");
		return "form";
	}

	@RequestMapping(value = "form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");

		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash,
			SessionStatus status) { /* @Valid habilita la validacion en el objeto mapeado al Form */

		model.addAttribute("titulo", "Formulario de Cliente");

		if (result.hasErrors()) {
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto().length() > 0) {

				Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
				File archivo = rootPath.toFile();

				if (archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}

			}

			String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
			Path rootAbsolutePath = rootPath.toAbsolutePath();

			log.info("rootPath: " + rootPath); // Path relativo al proyecto
			log.info("rootAbsolutePath: " + rootAbsolutePath); // Path absoluto del proyecto

			try {
				Files.copy(foto.getInputStream(), rootAbsolutePath);
				flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

				cliente.setFoto(uniqueFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con exito!" : "Cliente creado con exito!";

		clienteService.save(cliente);
		status.setComplete(); // Elimina el objeto cliente de la sesion
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);

		if (id > 0) {
			clienteService.eliminar(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito!");
		}

		Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
		File archivo = rootPath.toFile();

		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminado con éxito");
			}
		}

		return "redirect:/listar";
	}
}
