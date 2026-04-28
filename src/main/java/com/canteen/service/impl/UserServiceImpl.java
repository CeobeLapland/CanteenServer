package com.canteen.service.impl;

import com.canteen.exception.Exceptions.*;
import com.canteen.mapper.Mappers.UserMapper;
import com.canteen.model.dto.Dtos.UserDto;
import com.canteen.model.entity.User;
import com.canteen.model.request.Requests.CreateUserRequest;
import com.canteen.repository.Repositories.UserRepository;
import com.canteen.service.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService 实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = findUserOrThrow(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        // 检查用户名是否已被占用
        if (userRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("用户名 '" + request.getName() + "' 已被占用");
        }

        User user = User.builder()
                .name(request.getName())
                .build();

        User saved = userRepository.save(user);
        log.info("新用户创建成功: id={}, name={}", saved.getId(), saved.getName());
        return userMapper.toDto(saved);
    }

    // ==================== 内部工具方法 ====================

    /** 按 ID 查找用户，不存在则抛出 404 */
    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户", id));
    }
}
