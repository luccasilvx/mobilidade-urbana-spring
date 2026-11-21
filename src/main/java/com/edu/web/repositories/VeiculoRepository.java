package com.edu.web.repositories;

import com.edu.web.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {}