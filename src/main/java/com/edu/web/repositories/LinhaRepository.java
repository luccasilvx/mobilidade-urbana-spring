package com.edu.web.repositories;

import com.edu.web.entities.Linha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LinhaRepository extends JpaRepository<Linha, UUID> {
}