package ru.spbu.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.ProductionPractice;

import java.util.Optional;

@Repository
public interface ProductionPracticeRepository extends JpaRepository<ProductionPractice, Long> {
    @Query("SELECT p FROM ProductionPractice p where p.employee = ?1")
    Optional<ProductionPractice> findProductionPracticeByEmployee(Employee employee);
}
