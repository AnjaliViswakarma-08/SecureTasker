package com.securetasker.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.securetasker.dto.TaskRequest;
import com.securetasker.dto.TaskResponse;
import com.securetasker.dto.TaskStatusUpdateRequest;
import com.securetasker.entity.Role;
import com.securetasker.entity.Task;
import com.securetasker.entity.User;
import com.securetasker.exception.ForbiddenException;
import com.securetasker.exception.ResourceNotFoundException;
import com.securetasker.repository.TaskRepository;
import com.securetasker.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
  }

  public TaskResponse createTask(TaskRequest request, String email) {
    User user = getUserByEmail(email);

    Task task = new Task();
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setStatus(request.getStatus());
    task.setUser(user);

    Task saved = taskRepository.save(task);
    return toResponse(saved);
  }

  public List<TaskResponse> getTasksForUser(String email) {
    User user = getUserByEmail(email);

    List<Task> tasks = user.getRole() == Role.ROLE_ADMIN
        ? taskRepository.findAllWithUser()
        : taskRepository.findByUserIdWithUser(user.getId());

    return tasks.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public TaskResponse getTask(Long id, String email) {
    User user = getUserByEmail(email);
    Task task = taskRepository.findByIdWithUser(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    ensureAccess(user, task);
    return toResponse(task);
  }

  public TaskResponse updateTask(Long id, TaskRequest request, String email) {
    User user = getUserByEmail(email);
    Task task = taskRepository.findByIdWithUser(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    ensureAccess(user, task);
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setStatus(request.getStatus());

    Task saved = taskRepository.save(task);
    return toResponse(saved);
  }

  public TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request, String email) {
    User user = getUserByEmail(email);
    Task task = taskRepository.findByIdWithUser(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    ensureAccess(user, task);
    task.setStatus(request.getStatus());

    Task saved = taskRepository.save(task);
    return toResponse(saved);
  }

  public void deleteTask(Long id, String email) {
    User user = getUserByEmail(email);
    Task task = taskRepository.findByIdWithUser(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    ensureAccess(user, task);
    taskRepository.delete(task);
  }

  private User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private void ensureAccess(User user, Task task) {
    if (user.getRole() == Role.ROLE_ADMIN) {
      return;
    }

    if (!task.getUser().getId().equals(user.getId())) {
      throw new ForbiddenException("Not allowed to access this task");
    }
  }

  private TaskResponse toResponse(Task task) {
    return TaskResponse.builder()
        .id(task.getId())
        .title(task.getTitle())
        .description(task.getDescription())
        .status(task.getStatus())
        .userId(task.getUser().getId())
        .userName(task.getUser().getName())
        .userEmail(task.getUser().getEmail())
        .build();
  }
}
