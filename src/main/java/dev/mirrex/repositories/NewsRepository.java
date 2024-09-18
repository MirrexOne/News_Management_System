package dev.mirrex.repositories;

import dev.mirrex.entities.News;
import dev.mirrex.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByAuthor(User author, Pageable pageable);

    @Query("SELECT DISTINCT n FROM News n LEFT JOIN n.tags t WHERE " +
            "(:author IS NULL OR n.author.name = :author) AND " +
            "(:keywords IS NULL OR CAST(n.title AS string) ILIKE CONCAT('%', :keywords, '%') OR CAST(n.description AS string) ILIKE CONCAT('%', :keywords, '%')) AND " +
            "(:tags IS NULL OR t.title IN :tags)")
    Page<News> findNewsByFilters(String author, String keywords, List<String> tags, Pageable pageable);
}
