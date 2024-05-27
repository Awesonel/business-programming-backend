package ru.spbu.project.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
  @Schema(description = "Идентификатор лидера", example = "1")
  private Long id;

  @Schema(description = "Имя лидера", example = "Иван")
  private String name;

  @Schema(description = "Фамилия лидера", example = "Иванов")
  private String surname;

  @Schema(description = "Отчество лидера", example = "Иванович")
  private String patronymic;

  @Schema(description = "Должность лидера", example = "Менеджер")
  private String jobTitle;

  @Email
  @Schema(description = "Email лидера", example = "leader@example.com")
  private String email;

  public Leader(String leaderName, String leaderSurname, String leaderPatronymic, String leaderJobTitle) {
    this.name = leaderName;
    this.surname = leaderSurname;
    this.patronymic = leaderPatronymic;
    this.jobTitle = leaderJobTitle;
  }
}