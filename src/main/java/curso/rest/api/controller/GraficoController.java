package curso.rest.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.model.UsuarioGrafico;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario/v1")
public class GraficoController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping(value="/grafico", produces="application/json")
	public ResponseEntity<UsuarioGrafico> grafico(){
		
		UsuarioGrafico ug =new UsuarioGrafico();
		
		List<String> resultado = jdbcTemplate.queryForList("select array_agg(nome) from usuario where salario > 0 and nome <> '' union all select cast(array_agg(salario) as character varying[]) from usuario where salario > 0 and nome <> ''", String.class);
		
		if(!resultado.isEmpty()) {
			String nomes = resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", "");
			String salarios = resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", "");
			
			ug.setNome(nomes);
			ug.setSalario(salarios);
		}
		
		return new ResponseEntity<UsuarioGrafico>(ug, HttpStatus.OK);
	}
}
