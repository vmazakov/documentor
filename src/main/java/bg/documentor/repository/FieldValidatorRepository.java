package bg.documentor.repository;

import bg.documentor.model.FieldValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Validator entity.
 */
@Repository public interface FieldValidatorRepository extends JpaRepository<FieldValidator, Long> {

}
