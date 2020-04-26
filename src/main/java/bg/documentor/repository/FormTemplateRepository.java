package bg.documentor.repository;

import bg.documentor.model.FormTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FormTemplate entity.
 */
@Repository public interface FormTemplateRepository extends JpaRepository<FormTemplate, Long> {

}
