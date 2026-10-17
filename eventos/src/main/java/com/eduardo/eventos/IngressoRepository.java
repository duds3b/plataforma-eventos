package com.eduardo.eventos;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    List<Ingresso> findByEvento_Id(Long id);

    long countByEvento_Id(Long id);

}