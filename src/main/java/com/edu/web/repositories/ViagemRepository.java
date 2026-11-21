package com.edu.web.repositories;

import com.edu.web.entities.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ViagemRepository extends JpaRepository<Viagem, UUID> {}