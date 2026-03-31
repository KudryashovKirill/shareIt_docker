package ru.practicum.shareit.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.userDto.UserInputDto;
import ru.practicum.shareit.dto.userDto.UserOutputDto;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.UserMapper;
import ru.practicum.shareit.util.exception.EmailAlreadyExistsException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryJpa userRepositoryJpa;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepositoryJpa userRepositoryJpa, UserMapper userMapper) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public UserOutputDto create(UserInputDto userDto) {
        checkUniqueEmail(userDto);
        validateEmail(userDto);
        User user = userMapper.toEntity(userDto);
        userRepositoryJpa.save(user);
        return userMapper.toOutputDto(user);
    }

    @Transactional
    @Override
    public UserOutputDto update(Long id, UserInputDto userDto) {
        User user = checkIsInTable(id);
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            checkUniqueEmail(userDto);
            validateEmail(userDto);
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        return userMapper.toOutputDto(user);
    }

    @Override
    public UserOutputDto getById(Long id) {
        return userMapper.toOutputDto(userRepositoryJpa.findById(id).orElseThrow(() ->
                new NoSuchElementException("no user found by id")));
    }

    @Override
    public List<UserOutputDto> getAll() {
        return userRepositoryJpa.findAll()
                .stream()
                .map(userMapper::toOutputDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Map<String, Boolean> delete(Long id) {
        User userForDelete = checkIsInTable(id);
        userRepositoryJpa.delete(userForDelete);
        return Map.of("deleted", true);
    }

    private User checkIsInTable(Long id) {
        return userRepositoryJpa.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    private void checkUniqueEmail(UserInputDto userDto) {
        if (userRepositoryJpa.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    private void validateEmail(UserInputDto userDto) {
        if (!userDto.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException("illegal email");
        }
    }
}
