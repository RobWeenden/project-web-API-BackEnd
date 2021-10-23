package curso.rest.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.rest.api.model.Telefone;

@Repository
public interface TelefoneRepository extends CrudRepository<Telefone, Long>{

}
