package ru.spbu.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "production-practice")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionPractice {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ManyToOne
  private Leader leader;
  @OneToOne
  private Employee employee;
  private String project;
  private Boolean result;

  public ProductionPractice(Leader leader, Employee employee, String project) {
    this.leader = leader;
    this.employee = employee;
    this.project = project;
    this.result = false;
  }
}
