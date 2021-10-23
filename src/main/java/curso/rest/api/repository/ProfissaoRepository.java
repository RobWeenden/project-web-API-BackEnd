package curso.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.rest.api.model.Profissao;

@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long>{

}
