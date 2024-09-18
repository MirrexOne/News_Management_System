package dev.mirrex.repositories;

import dev.mirrex.entities.News;
import dev.mirrex.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByAuthor(User author, Pageable pageable);
}
