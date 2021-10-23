package curso.rest.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.rest.api.model.Usuario;
import curso.rest.api.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findUserByLogin(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario não foi encontrado");
		}
		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
	}

	public void inserirAcessoPadrao(Long id) {
		/* Procurar o nome da constraint */
		String constraint = usuarioRepository.consultaConstraintRole();
		
		if (constraint != null) {
			/* Remove a constraint */
			jdbcTemplate.execute("ALTER TABLE usuario_roles DROP CONSTRAINT " + constraint);
		}
		// usuarioRepository.removerConstraintRole(constraint);
		/* REALIZAR O INSERT DOS ROLE PADRÃO */
		usuarioRepository.inserirAcessoRolePadrao(id);
	}

}
