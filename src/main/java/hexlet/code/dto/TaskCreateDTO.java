package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TaskCreateDTO {
    @NotBlank
    private String title;

    private int index;

    private String content;

    @NotBlank
    private String status;

    private Long assignee_id;

    private List<Long> taskLabelIds;
}
