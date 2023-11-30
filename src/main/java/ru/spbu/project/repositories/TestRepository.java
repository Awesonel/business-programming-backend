package ru.spbu.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbu.project.models.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

}
