package hexlet.code.specification;

import hexlet.code.dto.TaskFilterDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskFilterDTO filterDTO) {
        return withTitleCont(filterDTO.getTitleCont())
                .and(withAssigneeId(filterDTO.getAssigneeId()))
                .and(withStatus(filterDTO.getStatus()))
                .and(withLabelId(filterDTO.getLabelId()));
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return (root, query, cb) -> titleCont == null ? cb.conjunction() :
                cb.like(cb.lower(root.get("name")), "%" + titleCont + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction() :
                cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() :
                cb.equal(root.get("taskStatus").get("slug"), status);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null ? cb.conjunction() :
                cb.equal(root.joinList("labels").get("id"), labelId);
    }
}
