package ru.spbu.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {

  @Query("select e from Leader e where e.surname = ?1 and e.name = ?2 and e.patronymic = ?3 and e.jobTitle = ?4")
  List<Leader> findByData(String surname, String name, String patronymic, String jobTitle);

  @Query("select l from Leader l where LOWER(l.name) LIKE LOWER(CONCAT('%', :search, '%')) or LOWER(l.surname) LIKE LOWER(CONCAT('%', :search, '%')) or LOWER(l.patronymic) LIKE LOWER(CONCAT('%', :search, '%'))")
  List<Leader> searchByName(String search);

  @Query("select l from Leader l where l.email = ?1")
  Optional<Leader> findByEmail(String email);
}
