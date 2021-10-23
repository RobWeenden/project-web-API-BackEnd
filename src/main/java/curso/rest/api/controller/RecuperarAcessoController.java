package curso.rest.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.excecoes.ObjetoErro;
import curso.rest.api.model.Usuario;
import curso.rest.api.repository.UsuarioRepository;
import curso.rest.api.service.EnvioEmailService;

@RestController
@RequestMapping(value = "recuperar/v1")
public class RecuperarAcessoController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EnvioEmailService envioemailService;
	
	@ResponseBody
	@PostMapping(value = "/reset")
	public ResponseEntity<ObjetoErro> recuperarAcesso(@RequestBody Usuario login) throws Exception{
		
		ObjetoErro objetoErro = new ObjetoErro();
		
		Usuario user = usuarioRepository.findUserByLogin(login.getLogin());
		if(user == null) {
			objetoErro.setCode("404");
			objetoErro.setError("Usuário não encontrado");
		}else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			String senhaCriptografada = new BCryptPasswordEncoder().encode(senhaNova);
			
			usuarioRepository.updateSenha(senhaCriptografada, user.getId());
			
			envioemailService.enviarEmail("Recuperação de seenha",
					user.getLogin(), 
					"Sua nova senha é: "+senhaNova
							+ "\nSolicitamos ao Usuario que ao entrar no sistema, por gentileza altera sua senha temporaria");
			
			objetoErro.setCode("200");
			objetoErro.setError("Acesso enviado para o seu e-mail");
		}
		return new ResponseEntity<ObjetoErro>(objetoErro, HttpStatus.OK);
	}
 
}
