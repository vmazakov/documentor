package bg.documentor.repository;

import bg.documentor.model.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the FormField entity.
 */
@Repository public interface FormFieldRepository extends JpaRepository<FormField, Long> {

	List<FormField> findByFormTemplateId(Long formTemplateId);
}
