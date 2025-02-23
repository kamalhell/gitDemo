package com.example.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "task_priority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskPriority {

@Id
@GeneratedValue(generator = "UUID")
@UuidGenerator
private UUID taskPriorityId;

@Column(nullable = false, unique = true)
private String name;

@Column(nullable = false)
private String color;

@Column(nullable = false)
private Boolean status;
}



---------------------------------------------------------

package com.example.taskmanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskPriorityRequestDTO {
private String name;
private String color;
private Boolean status;
}

----------------------------------------------------------

package com.example.taskmanagement.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskPriorityResponseDTO {
private UUID taskPriorityId;
private String name;
private String color;
private Boolean status;
}

--------------------------------------------------------


package com.example.taskmanagement.repository;

import com.example.taskmanagement.entity.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, UUID> {
}

-------------------------------------------------------------------------------------

package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskPriorityRequestDTO;
import com.example.taskmanagement.dto.TaskPriorityResponseDTO;
import com.example.taskmanagement.entity.TaskPriority;
import com.example.taskmanagement.repository.TaskPriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskPriorityService {

private final TaskPriorityRepository taskPriorityRepository;

public List<TaskPriorityResponseDTO> getAllTaskPriorities() {
return taskPriorityRepository.findAll().stream()
.map(this::convertToResponseDTO)
.collect(Collectors.toList());
}

public TaskPriorityResponseDTO getTaskPriorityById(UUID id) {
return taskPriorityRepository.findById(id)
.map(this::convertToResponseDTO)
.orElseThrow(() -> new RuntimeException("Task Priority not found"));
}

public TaskPriorityResponseDTO createTaskPriority(TaskPriorityRequestDTO requestDTO) {
TaskPriority taskPriority = new TaskPriority();
taskPriority.setName(requestDTO.getName());
taskPriority.setColor(requestDTO.getColor());
taskPriority.setStatus(requestDTO.getStatus());

return convertToResponseDTO(taskPriorityRepository.save(taskPriority));
}

public TaskPriorityResponseDTO updateTaskPriority(UUID id, TaskPriorityRequestDTO requestDTO) {
TaskPriority taskPriority = taskPriorityRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Task Priority not found"));

taskPriority.setName(requestDTO.getName());
taskPriority.setColor(requestDTO.getColor());
taskPriority.setStatus(requestDTO.getStatus());

return convertToResponseDTO(taskPriorityRepository.save(taskPriority));
}

public void deleteTaskPriority(UUID id) {
taskPriorityRepository.deleteById(id);
}
-----------------------------------------------------------------
public void softDeleteTaskPriority(UUID id) {
TaskPriority taskPriority = taskPriorityRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Task Priority not found"));
taskPriority.setStatus(false); // Soft delete by setting status to INACTIVE
taskPriorityRepository.save(taskPriority);
}

-------------------------------------------------------------------
private TaskPriorityResponseDTO convertToResponseDTO(TaskPriority taskPriority) {
return new TaskPriorityResponseDTO(
taskPriority.getTaskPriorityId(),
taskPriority.getName(),
taskPriority.getColor(),
taskPriority.getStatus()
);
}
}

-----------------------------------------------------------------------------------------------

package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.TaskPriorityRequestDTO;
import com.example.taskmanagement.dto.TaskPriorityResponseDTO;
import com.example.taskmanagement.service.TaskPriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-priorities")
@RequiredArgsConstructor
public class TaskPriorityController {

private final TaskPriorityService taskPriorityService;

@GetMapping
public ResponseEntity<List<TaskPriorityResponseDTO>> getAllTaskPriorities() {
return ResponseEntity.ok(taskPriorityService.getAllTaskPriorities());
}

@GetMapping("/{id}")
public ResponseEntity<TaskPriorityResponseDTO> getTaskPriorityById(@PathVariable UUID id) {
return ResponseEntity.ok(taskPriorityService.getTaskPriorityById(id));
}

@PostMapping
public ResponseEntity<TaskPriorityResponseDTO> createTaskPriority(@RequestBody TaskPriorityRequestDTO requestDTO) {
return ResponseEntity.ok(taskPriorityService.createTaskPriority(requestDTO));
}

@PutMapping("/{id}")
public ResponseEntity<TaskPriorityResponseDTO> updateTaskPriority(@PathVariable UUID id, @RequestBody TaskPriorityRequestDTO requestDTO) {
return ResponseEntity.ok(taskPriorityService.updateTaskPriority(id, requestDTO));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTaskPriority(@PathVariable UUID id) {
taskPriorityService.deleteTaskPriority(id);
return ResponseEntity.noContent().build();
}
}





@Query("SELECT bt FROM BoardTask bt WHERE (:taskPriorityId IS NULL OR bt.taskPriority.taskPriorityId = :taskPriorityId) " +
"AND (:status IS NULL OR bt.status LIKE %:status%)")
List<BoardTask> searchBoardTasks(@Param("taskPriorityId") UUID taskPriorityId,
@Param("status") String status);

public List<BoardTask> searchBoardTasks(UUID taskPriorityId, String status) {
return boardTaskRepository.searchBoardTasks(taskPriorityId, status);
}

@GetMapping("/search")
public ResponseEntity<List<BoardTask>> searchBoardTasks(
@RequestParam(required = false) UUID taskPriorityId,
@RequestParam(required = false) String status) {
return ResponseEntity.ok(boardTaskService.searchBoardTasks(taskPriorityId, status));
}
