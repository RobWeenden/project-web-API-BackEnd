package curso.rest.api.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.rest.api.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	public Usuario findUserByLogin(String login);

	@Query("select u from Usuario u where lower(u.nome) like lower(concat('%',:nome,'%'))")
	public List<Usuario> findUserByNome(@Param("nome") String nome);

	@Query("select u from Usuario u order by u.nome")
	public List<Usuario> findUserOrderByNome();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update Usuario set token = ?1 where login = ?2")
	void atualizaTokenUser(String token, String login);

	@Query(value = "SELECT constraint_name from information_schema.constraint_column_usage WHERE table_name = 'usuario_roles' and column_name = 'role_id' and constraint_name <> 'unique_role_user';", nativeQuery = true)
	String consultaConstraintRole();
//	
//	@Modifying
//	@Query(value="ALTER TABLE usuario_roles DROP CONSTRAINT ?1;", nativeQuery=true)
//	void removerConstraintRole(String constraint);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO usuario_roles (usuario_id, role_id) VALUES (?1, (SELECT id FROM role WHERE  nome_role = 'ROLE_USER'));")
	void inserirAcessoRolePadrao(Long id);

	default Page<Usuario> findUserByNomePage(String nome, PageRequest pageRequest){
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		
		ExampleMatcher exampleMatcher  = ExampleMatcher.matchingAny().withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Usuario> example = Example.of(usuario, exampleMatcher);
		//Page<Usuario> retorno = findAll(example, pageRequest);
		
		return findAll(example, pageRequest);
		
	}
	
	@Transactional
	@Modifying
	@Query(value="update usuario set senha = ?1 where id = ?2", nativeQuery=true)
	void updateSenha(String senha, Long codUser);

}
