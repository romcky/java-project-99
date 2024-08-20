package hexlet.code.dto;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class TaskUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<Integer> index;
    private JsonNullable<String> content;
    private JsonNullable<String> status;
    private JsonNullable<String> assignee_id;
}
