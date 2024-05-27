package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class passResultDTO {
    @Schema(description = "Результат прохождения", example = "true")
    Boolean res;

    @Schema(description = "Дата результата", example = "2023-01-01")
    LocalDate date;
}