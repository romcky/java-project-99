package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskMapper taskMapper;

    public List<TaskDTO> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceAccessException("Задача с id = " + id + " не найдена"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO createDTO) {
        Task task = taskMapper.map(createDTO);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO updateDTO, Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceAccessException("Задача с id = " + id + " не найдена"));
        taskMapper.update(updateDTO, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
