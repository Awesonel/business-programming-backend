package ru.spbu.project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.enums.Stage;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  
  @Query("select e from Employee e where e.surname = ?1 and e.name = ?2 and e.patronymic = ?3")
  List<Employee> findByName(String surname, String name, String patronymic);

  @Query("select e from Employee e where e.stage = ?1")
  List<Employee> findByStage(Stage stage);

}
