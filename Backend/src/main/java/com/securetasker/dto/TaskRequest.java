package com.securetasker.dto;

import com.securetasker.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
  @NotBlank
  private String title;

  private String description;

  @NotNull
  private TaskStatus status;
}
