package hexlet.code.component;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setEmail("hexlet@example.com");
        createDTO.setPassword("qwerty");
        createDTO.setFirstName("SomeFirstName");
        createDTO.setLastName("SomeLastName");
        userService.create(createDTO);
    }
}
