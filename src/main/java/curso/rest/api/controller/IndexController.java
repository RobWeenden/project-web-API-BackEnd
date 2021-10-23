package curso.rest.api.controller;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.dto.UsuarioDTO;
import curso.rest.api.model.Usuario;
import curso.rest.api.repository.TelefoneRepository;
import curso.rest.api.repository.UsuarioRepository;
import curso.rest.api.service.ImplementacaoUserDetailsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario/v1")
public class IndexController {
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private TelefoneRepository repositoryTel;
	
	@Autowired
	private ImplementacaoUserDetailsService impUserDatilUser;
	
	@Transactional
	@PostMapping(value = "/cadastrar", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario value) throws Exception {
		for (int pos = 0; pos < value.getTelefones().size(); pos++) {
			value.getTelefones().get(pos).setUsuario(value);
		}
		
		//**CONSUMINDO API EXTERNA VIA-CEP
//		URL url = new URL("https://viacep.com.br/ws/"+value.getCep()+"/json/");
//		URLConnection openConnection = url.openConnection();
//		InputStream is =  openConnection.getInputStream();
//		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//		
//		String cep = "";
//		StringBuilder jsonCep = new StringBuilder();
//		
//		while((cep = br.readLine()) != null) {
//			jsonCep.append(cep);
//		}
		//**Convert JSON in OBJECT
//		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
//		value.setCep(userAux.getCep());
//		value.setBairro(userAux.getBairro());
//		value.setComplemento(userAux.getComplemento());
//		value.setLocalidade(userAux.getLocalidade());
//		value.setLogradouro(userAux.getLogradouro());
//		value.setUf(userAux.getUf());
//		
		//**CONSUMINDO API EXTERNA VIA-CEP
		
		
		String senhaCript = new BCryptPasswordEncoder().encode(value.getSenha());
		value.setSenha(senhaCript);
		Usuario user = repository.save(value);
		impUserDatilUser.inserirAcessoPadrao(user.getId());
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
	}


	@Transactional
	@GetMapping(value = "/listAll", produces = "application/json")
	public ResponseEntity<Page<Usuario>> listAll() throws InterruptedException {
		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		Page<Usuario> listUsuario = repository.findAll(page);
		//List<Usuario> listUser = (List<Usuario>) repository.findUserOrderByNome();
		return new ResponseEntity<Page<Usuario>>(listUsuario, HttpStatus.OK);
	}
	
	@Transactional
	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> listPage(@PathVariable("pagina") int pagina) throws InterruptedException {
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		Page<Usuario> listUsuario = repository.findAll(page);
		return new ResponseEntity<Page<Usuario>>(listUsuario, HttpStatus.OK);
	}

	@GetMapping(value = "/recuperar/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> recuperar(@PathVariable(value = "id") Long id) {
		Optional<Usuario> usuario = repository.findById(id);
		UsuarioDTO userDto = new UsuarioDTO(usuario.get());
		
		return new ResponseEntity<UsuarioDTO>(userDto, HttpStatus.OK);
	}
	@Transactional
	@GetMapping(value = "/buscar/{nome}", produces = "application/json")
	public ResponseEntity<List<Usuario>> buscarUsuario(@PathVariable(value = "nome") String nome) {
		List<Usuario> usuario = repository.findUserByNome(nome);
		
		return new ResponseEntity<List<Usuario>>(usuario, HttpStatus.OK);
	}
	
	@Transactional
	@GetMapping(value = "/buscarPorNome/{nome}/page/{page}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> buscarUsuarioByPage(@PathVariable(value = "nome") String nome, @PathVariable("page") int page) {
		
		
		PageRequest pageRequest = null;
		Page<Usuario> listUsuario = null;
		if( nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			listUsuario = repository.findAll(pageRequest);
		}else {
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			listUsuario = repository.findUserByNomePage(nome, pageRequest);
		}
		//List<Usuario> usuario = repository.findUserByNome(nomeBuscar);
		
		return new ResponseEntity<Page<Usuario>>(listUsuario, HttpStatus.OK);
	}
	
	@Transactional
	@GetMapping(value = "/buscarPorNome/{nome}")
	public ResponseEntity<Page<Usuario>> buscarUsuarioByName(@PathVariable(value = "nome") String nome) {
		
		
		PageRequest pageRequest = null;
		Page<Usuario> listUsuario = null;
		if( nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			listUsuario = repository.findAll(pageRequest);
		}else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			listUsuario = repository.findUserByNomePage(nome, pageRequest);
		}
		//List<Usuario> usuario = repository.findUserByNome(nomeBuscar);
		
		return new ResponseEntity<Page<Usuario>>(listUsuario, HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping(value = "/atualizar", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario value) {
		for (int pos = 0; pos < value.getTelefones().size(); pos++) {
			value.getTelefones().get(pos).setUsuario(value);
		}
		Usuario userTemp = repository.findById(value.getId()).get();
		if(!userTemp.getSenha().equals(value.getSenha())) {
			
			String senhaCript = new BCryptPasswordEncoder().encode(value.getSenha());
			value.setSenha(senhaCript);
		}
		Usuario user = repository.save(value);
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deletar/{id}", produces = "application/text")
	public ResponseEntity<String> deletar(@PathVariable(value = "id") Long id) throws ValidationException {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>("Deletado com Sucesso",HttpStatus.OK);
			
		} catch (Exception ex) {
			ex.getMessage();
		}
		throw new ValidationException("Usuario deletado com Sucesso");
	}
	
	@DeleteMapping(value = "/telefone/deletar/{id}", produces = "application/text")
	public  ResponseEntity<String>  deleteTelefone(@PathVariable("id") Long id) {
		repositoryTel.deleteById(id);
		
		return new ResponseEntity<>("Telefone deletado com Sucesso",HttpStatus.OK);
	}

}
