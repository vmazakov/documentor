package bg.documentor.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created date, created by, last modified by,
 * last modified date.
 */
@Data @MappedSuperclass @EntityListeners(AuditingEntityListener.class) abstract class AbstractAuditingEntity
        implements Serializable {

    @CreatedBy @Column(name = "created_by", nullable = false, updatable = false) private Long createdBy;

    @CreatedDate @Column(name = "created_at", nullable = false) private Instant createdAt;

    @LastModifiedBy @Column(name = "updated_by") private Long lastModifiedBy;

    @LastModifiedDate @Column(name = "updated_at", nullable = false) private Instant lastUpdatedBy;

}
