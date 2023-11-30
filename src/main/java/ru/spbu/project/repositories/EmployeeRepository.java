package ru.spbu.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spbu.project.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
