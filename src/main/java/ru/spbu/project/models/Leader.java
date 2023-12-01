package ru.spbu.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leaders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leader {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;
  private String surname;
  private String patronymic;
  private String jobTitle;


  public Leader(String leaderName, String leaderSurname, String leaderPatronymic, String leaderJobTitle) {
    this.name = leaderName;
    this.surname = leaderSurname;
    this.patronymic = leaderPatronymic;
    this.jobTitle = leaderJobTitle;
  }
}
