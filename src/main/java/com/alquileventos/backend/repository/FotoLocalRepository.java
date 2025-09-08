package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.FotoLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoLocalRepository extends JpaRepository<FotoLocal, Integer> {
    
    List<FotoLocal> findByLocal_IdLocal(Integer idLocal);
    
    void deleteByLocal_IdLocal(Integer idLocal);
}
