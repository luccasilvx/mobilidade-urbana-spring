package com.edu.web.repositories;

import com.edu.web.entities.EstacaoPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EstacaoPontoRepository extends JpaRepository<EstacaoPonto, UUID> {}