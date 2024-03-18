package com.app.userservice.service.impl;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.entity.user.UserEntity;
import com.app.userservice.exception.custom.EmailExistsException;
import com.app.userservice.mapper.user.CreateRequestUserMapper;
import com.app.userservice.repository.UserRepository;
import com.app.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CreateRequestUserMapper createRequestUserMapper;
    @Override
    public UserEntity createUser(CreateUserRequest request) throws EmailExistsException {
        UserEntity entity = createRequestUserMapper.toEntity(request);
        return saveUser(entity);
    }

    private UserEntity saveUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new EmailExistsException("#");
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
