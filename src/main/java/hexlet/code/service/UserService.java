package hexlet.code.service;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с id = " + id + " не найден"));
        return userMapper.map(user);
    }

    public UserDTO create(UserCreateDTO createDTO) {
        User user = userMapper.map(createDTO);
        user.setPasswordDigest(passwordEncoder.encode(createDTO.getPassword()));
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO update(UserUpdateDTO updateDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с id = " + id + " не найден"));
        userMapper.update(updateDTO, user);
        if (updateDTO.getPassword() != null) {
            user.setPasswordDigest(passwordEncoder.encode(updateDTO.getPassword().get()));
        }
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
