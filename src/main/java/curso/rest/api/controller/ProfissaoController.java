package curso.rest.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.model.Profissao;
import curso.rest.api.repository.ProfissaoRepository;

@RestController
@RequestMapping(value = "/profissao/v1")
public class ProfissaoController {

	@Autowired
	private ProfissaoRepository repository;

	@GetMapping(value = "/listAll", produces = "application/json")
	public ResponseEntity<List<Profissao>> listAll() {
		List<Profissao> lista = repository.findAll();

		return new ResponseEntity<List<Profissao>>(lista, HttpStatus.OK);
	}

}
