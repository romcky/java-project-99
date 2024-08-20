package hexlet.code.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private long id;
    private String title;
    private int index;
    private String content;
    private String status;
    private long assignee_id;
    private LocalDate createdAt;
}
