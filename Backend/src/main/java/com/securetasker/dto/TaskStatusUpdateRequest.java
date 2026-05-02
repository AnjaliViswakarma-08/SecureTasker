package com.securetasker.dto;

import com.securetasker.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateRequest {
  @NotNull
  private TaskStatus status;
}
