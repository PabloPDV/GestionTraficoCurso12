package es.cic.curso.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso.back.model.Via;

@Repository
public interface ViaRepository extends JpaRepository<Via, Long> {

}
