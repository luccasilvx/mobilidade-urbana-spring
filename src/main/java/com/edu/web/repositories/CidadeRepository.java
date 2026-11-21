package com.edu.web.repositories;

import com.edu.web.entities.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CidadeRepository extends JpaRepository<Cidade, UUID> {}