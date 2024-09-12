package es.cic.curso.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso.back.model.TipoVia;

@Repository
public interface TipoViaRepository extends JpaRepository<TipoVia, Long> {
    TipoVia findByCodigo(String codigo);
}