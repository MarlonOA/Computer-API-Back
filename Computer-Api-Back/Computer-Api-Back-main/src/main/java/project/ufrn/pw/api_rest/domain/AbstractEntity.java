package project.ufrn.pw.api_rest.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime deletedAt;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractEntity that = (AbstractEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    public abstract void partialUpdate(AbstractEntity e);
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}