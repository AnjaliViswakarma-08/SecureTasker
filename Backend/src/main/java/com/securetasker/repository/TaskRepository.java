package com.securetasker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.securetasker.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByUserId(Long userId);

  @Query("select t from Task t join fetch t.user")
  List<Task> findAllWithUser();

  @Query("select t from Task t join fetch t.user where t.user.id = :userId")
  List<Task> findByUserIdWithUser(@Param("userId") Long userId);

  @Query("select t from Task t join fetch t.user where t.id = :id")
  Optional<Task> findByIdWithUser(@Param("id") Long id);
}
