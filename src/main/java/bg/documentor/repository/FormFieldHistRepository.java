package bg.documentor.repository;

import bg.documentor.model.FormFieldHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the FormFieldHist entity.
 */
@Repository public interface FormFieldHistRepository extends JpaRepository<FormFieldHist, Long> {

	List<FormFieldHist> findByFormTemplateId(Long formTemplateId);
}
