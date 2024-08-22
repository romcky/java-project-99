package hexlet.code.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDTO {
    private long id;
    private String title;
    private int index;
    private String content;
    private String status;
    private long assignee_id;
    private LocalDate createdAt;
    private List<Long> taskLabelIds;
}
