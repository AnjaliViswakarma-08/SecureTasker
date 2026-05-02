package com.securetasker.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.securetasker.dto.TaskRequest;
import com.securetasker.dto.TaskResponse;
import com.securetasker.dto.TaskStatusUpdateRequest;
import com.securetasker.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
    return ResponseEntity.ok(taskService.createTask(request, currentUserEmail()));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<TaskResponse>> getTasks() {
    return ResponseEntity.ok(taskService.getTasksForUser(currentUserEmail()));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
    return ResponseEntity.ok(taskService.getTask(id, currentUserEmail()));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
      @Valid @RequestBody TaskRequest request) {
    return ResponseEntity.ok(taskService.updateTask(id, request, currentUserEmail()));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @PatchMapping("/{id}/status")
  public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id,
      @Valid @RequestBody TaskStatusUpdateRequest request) {
    return ResponseEntity.ok(taskService.updateTaskStatus(id, request, currentUserEmail()));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id, currentUserEmail());
    return ResponseEntity.noContent().build();
  }

  private String currentUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
