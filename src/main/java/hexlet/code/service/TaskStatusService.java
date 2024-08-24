package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        List<TaskStatus> statuses = taskStatusRepository.findAll();
        return statuses.stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    public TaskStatusDTO findById(Long id) {
        TaskStatus status = taskStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Статус с id = " + id + " не найден"));
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO findBySlug(String slug) {
        TaskStatus status = taskStatusRepository.findBySlug(slug).orElseThrow(
                () -> new ResourceNotFoundException("Статус slug = " + slug + " не найден"));
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO createDTO) {
        TaskStatus status = taskStatusMapper.map(createDTO);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO updateDTO, Long id) {
        TaskStatus status = taskStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Статус с id = " + id + " не найден"));
        taskStatusMapper.update(updateDTO, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public void deleteById(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
