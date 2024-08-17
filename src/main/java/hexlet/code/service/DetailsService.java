package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailsService implements UserDetailsManager {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден: " + email));
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void createUser(UserDetails userDetails) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void deleteUser(String email) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean userExists(String email) {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}
