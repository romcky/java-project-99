package hexlet.code.component;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskStatusService taskStatusService;

    @Override
    public void run(ApplicationArguments args) {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setEmail("hexlet@example.com");
        createDTO.setPassword("qwerty");
        createDTO.setFirstName("SomeFirstName");
        createDTO.setLastName("SomeLastName");
        userService.create(createDTO);

        taskStatusService.create(new TaskStatusCreateDTO("Draft", "draft"));
        taskStatusService.create(new TaskStatusCreateDTO("To review", "to_review"));
        taskStatusService.create(new TaskStatusCreateDTO("To be fixed", "to_be_fixed"));
        taskStatusService.create(new TaskStatusCreateDTO("To publish", "to_publish"));
        taskStatusService.create(new TaskStatusCreateDTO("Published", "published"));
    }
}
