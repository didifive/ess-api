package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
}
