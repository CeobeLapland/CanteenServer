package com.canteen.service.impl;

import com.canteen.exception.Exceptions.*;
import com.canteen.mapper.Mappers.FoodMapper;
import com.canteen.model.dto.Dtos.FoodDetailDto;
import com.canteen.model.dto.Dtos.FoodSummaryDto;
import com.canteen.model.entity.Food;
import com.canteen.model.request.Requests.CreateFoodRequest;
import com.canteen.model.response.PageResponse;
import com.canteen.repository.Repositories.FoodRepository;
import com.canteen.service.Services.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * FoodService 实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    @Override
    @Transactional(readOnly = true)
    public FoodDetailDto getFoodById(Long id) {
        Food food = findFoodOrThrow(id);
        FoodDetailDto dto = foodMapper.toDetailDto(food);
        // 手动填充帖子数量，避免加载整个 posts 集合
        dto.setPostCount(food.getPosts().size());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodSummaryDto> getAllFoods(Pageable pageable) {
        Page<FoodSummaryDto> page = foodRepository.findAll(pageable)
                .map(foodMapper::toSummaryDto);
        return PageResponse.of(page);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<FoodDetailDto> getAllFoodsNoPagination() {
        return foodRepository.findAll().stream()
                .map(foodMapper::toDetailDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<FoodDetailDto> getUpdatedFoods(String since) {
        // 解析时间字符串
        java.time.LocalDateTime sinceTime;
        try {
            sinceTime = java.time.LocalDateTime.parse(since);
        } catch (java.time.format.DateTimeParseException e) {
            throw new BadRequestException("无效的时间格式，必须为 ISO_LOCAL_DATE_TIME，例如 2024-01-01T00:00:00");
        }

        return foodRepository.findAll().stream()
                .filter(food -> food.getUpdatedAt() != null && food.getUpdatedAt().isAfter(sinceTime))
                .map(foodMapper::toDetailDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodSummaryDto> searchFoods(String keyword, Pageable pageable) {
        Page<FoodSummaryDto> page = foodRepository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(foodMapper::toSummaryDto);
        return PageResponse.of(page);
    }

    @Override
    @Transactional
    public FoodDetailDto createFood(CreateFoodRequest request) {
        Food food = Food.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .campus(request.getCampus())
                .canteen(request.getCanteen())
                .floor(request.getFloor())
                .window(request.getWindow())
                .sellTime(request.getSellTime())
                .tags(request.getTags() == null ? new ArrayList<>() : request.getTags())
                .build();

        Food saved = foodRepository.save(food);
        log.info("菜品创建成功: id={}, name={}", saved.getId(), saved.getName());

        FoodDetailDto dto = foodMapper.toDetailDto(saved);
        dto.setPostCount(0);
        return dto;
    }

    @Override
    @Transactional
    public FoodDetailDto updateFood(Long id, CreateFoodRequest request) {
        Food food = findFoodOrThrow(id);

        // 只更新非空字段（占位符：可引入专用 UpdateFoodRequest 精细控制）
        if (request.getName() != null)        food.setName(request.getName());
        if (request.getDescription() != null) food.setDescription(request.getDescription());
        if (request.getPrice() != null)       food.setPrice(request.getPrice());
        if (request.getImageUrl() != null)    food.setImageUrl(request.getImageUrl());

        // 更新新增字段
        if (request.getCampus() != null)      food.setCampus(request.getCampus());
        if (request.getCanteen() != null)     food.setCanteen(request.getCanteen());
        if (request.getFloor() != null)       food.setFloor(request.getFloor());
        if (request.getWindow() != null)      food.setWindow(request.getWindow());
        if (request.getSellTime() != null)    food.setSellTime(request.getSellTime());
        if (request.getTags() != null)        food.setTags(request.getTags());

        Food saved = foodRepository.save(food);
        log.info("菜品更新成功: id={}", saved.getId());

        FoodDetailDto dto = foodMapper.toDetailDto(saved);
        dto.setPostCount(saved.getPosts().size());
        return dto;
    }

    @Override
    @Transactional
    public void deleteFood(Long id) {
        Food food = findFoodOrThrow(id);
        // 删除前先解除与所有 Post 的关联，避免外键约束异常
        food.getPosts().forEach(post -> post.getFoods().remove(food));
        foodRepository.delete(food);
        log.info("菜品删除成功: id={}", id);
    }

    // ==================== 内部工具方法 ====================

    private Food findFoodOrThrow(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜品", id));
    }
}
