package com.securetasker.dto;

import com.securetasker.entity.TaskStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponse {
  private Long id;
  private String title;
  private String description;
  private TaskStatus status;
  private Long userId;
  private String userName;
  private String userEmail;
}
