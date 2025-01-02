package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    public Map<Long, User> findAll() {
        return users;
    }

    public Optional<User> findById(@PathVariable int userId) {
        return users.values().stream()
                .filter(x -> x.getId() == userId)
                .findFirst();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }
        if (isEmailExists(user.getEmail())) {
            throw  new DuplicatedDataException("Этот email уже используется");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        if (user.getEmail() != null) {
            if (isEmailExists(user.getEmail()) && !existingUser.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Этот email уже используется");
            }
            existingUser.setEmail(user.getEmail());
        }
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }

        return  existingUser;
    }

    private Long getNextId() {
        return nextId++;
    }

    private boolean isEmailExists(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
