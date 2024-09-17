package dev.mirrex.repositories;

import dev.mirrex.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTitle(String title);

    List<Tag> findByTitleIn(Collection<String> titles);
}
