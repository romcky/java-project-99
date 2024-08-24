package hexlet.code.mapper;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.BaseEntity;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public TaskStatus toEntity(String slug) {
        TaskStatus status = taskStatusRepository.findBySlug(slug).orElseThrow(
                () -> new ResourceNotFoundException("Статус slug = " + slug + " не найден"));
        return slug != null ? status : null;
    }

    public List<Label> idsToLabels(List<Long> taskLabelIds) {
        return labelRepository.findByIdIn(taskLabelIds);
    }

    public List<Long> labelsToIds(List<Label> labels) {
        List<Long> ids = new ArrayList<>();
        if (labels != null) {
            for (Label label : labels) {
                ids.add(label.getId());
            }
        }
        return ids;
    }
}
