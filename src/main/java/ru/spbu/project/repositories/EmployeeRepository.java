package ru.spbu.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.enums.Stage;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query("select e from Employee e where e.surname = ?1 and e.name = ?2 and e.patronymic = ?3")
  List<Employee> findByName(String surname, String name, String patronymic);

  @Query("select e from Employee e where e.stage = ?1")
  List<Employee> findByStage(Stage stage);

  @Query("select e from Employee e where LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) or LOWER(e.surname) LIKE LOWER(CONCAT('%', :search, '%')) or LOWER(e.patronymic) LIKE LOWER(CONCAT('%', :search, '%'))")
  List<Employee> searchByName(String search);

  @Query("select e from Employee e where (LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) or LOWER(e.surname) LIKE LOWER(CONCAT('%', :search, '%')) or LOWER(e.patronymic) LIKE LOWER(CONCAT('%', :search, '%'))) and e.stage = :stage")
  List<Employee> findByStageAndName(String search, Stage stage);

  @Query("select e from Employee e where e.email = ?1")
  Optional<Employee> findByEmail(String email);
}
