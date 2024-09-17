package dev.mirrex.repositories;

import dev.mirrex.entities.News;
import dev.mirrex.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByAuthor(User author);
}
