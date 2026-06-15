package com.canteen.service.impl;

import com.canteen.exception.Exceptions.*;
import com.canteen.mapper.Mappers;
import com.canteen.mapper.Mappers.FoodMapper;
import com.canteen.model.dto.Dtos;
import com.canteen.model.dto.Dtos.FoodDetailDto;
import com.canteen.model.dto.Dtos.FoodSummaryDto;
import com.canteen.model.entity.Food;
import com.canteen.model.entity.Tag;
import com.canteen.model.entity.Window;
import com.canteen.model.request.Requests;
import com.canteen.model.request.Requests.FilterFoodRequest;
import com.canteen.model.request.Requests.CreateFoodRequest;
import com.canteen.model.response.PageResponse;

import com.canteen.repository.TagRepository;
import com.canteen.repository.WindowRepository;
import com.canteen.repository.FoodRepository;
import com.canteen.service.Services.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * FoodService 实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    private final TagRepository tagRepository;
    private final Mappers.TagMapper tagMapper;

    // 先这样凑合，能跑就行
    private final WindowRepository windowRepository;
    private final Mappers.WindowMapper windowMapper;

    @Override
    @Transactional(readOnly = true)
    public FoodDetailDto getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜品", id));
        FoodDetailDto dto = foodMapper.toDetailDto(food);

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
    public PageResponse<FoodDetailDto> getAllFoodDetails(Pageable pageable) {
        Page<FoodDetailDto> page = foodRepository.findAll(pageable)
                .map(foodMapper::toDetailDto);
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

        //先用window的名称查出对象
        // 目前正在填充示例数据，为了方便起见，如果窗口不存在就创建一个新的窗口对象并保存到数据库里，后续可以改成严格要求窗口必须存在的逻辑
        Window window = windowRepository.findByName(request.getWindowName())
                .orElseGet(() -> {
                    Window newWindow = Window.builder()
                            .name(request.getWindowName())
                            .campusName(request.getCampusName())
                            .canteenName(request.getCanteenName())
                            .floorName(request.getFloorName())
                            .build();
                    return windowRepository.save(newWindow);
                });
        //return new BadRequestException("窗口不存在: " + request.getWindowName())

        //先构建除了tags以外的Food实体
        Food food = Food.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                //.imageUrl(request.getImageUrl())
                .campusName(request.getCampusName())
                .canteenName(request.getCanteenName())
                .floorName(request.getFloorName())
                .window(window)
                .sellTime(request.getSellTime())
                .build();

        //再把request里的List<String> tags转换成List<Tag>，查不到就创建一个新的Tag，并且添加中间实体FoodTag关系
        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = Tag.builder()
                                    .name(tagName)
                                    .build();
                            return tagRepository.save(newTag);
                        });
                //tags.add(tag);
                food.addTag(tag);
            }
        }
        //至于和Post的关系留到PostServiceImpl里处理，FoodServiceImpl只负责Food的增删改查，不涉及Post的业务逻辑

        Food saved = foodRepository.save(food);//这个saved和food是同一个对象，save方法会把id等自动生成的字段填充到原对象里，所以直接返回food也行
        FoodDetailDto dto = foodMapper.toDetailDto(saved);
        log.info("菜品创建成功: id={}", saved.getId());

        return dto;
    }

    @Override
    @Transactional
    public List<FoodDetailDto> createFoods(List<CreateFoodRequest> requests) {
        // 这里简单地逐条调用 createFood 方法，实际项目中可以优化为批量处理以提高性能
        List<FoodDetailDto> createdList = new ArrayList<>();
        for (CreateFoodRequest request : requests) {
            createdList.add(createFood(request));
        }
        return createdList;
    }

    @Override
    @Transactional
    public FoodDetailDto updateFood(Long id, Requests.UpdateFoodRequest request) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜品", id));

        // 只更新非空字段
        if (request.getName() != null)
            food.setName(request.getName());
        if (request.getDescription() != null)
            food.setDescription(request.getDescription());
        if (request.getPrice() != null)
            food.setPrice(request.getPrice());
        //if (request.getImageUrl() != null)
        //    food.setImageUrl(request.getImageUrl());
        if (request.getCampusName() != null)
            food.setCampusName(request.getCampusName());
        if (request.getCanteenName() != null)
            food.setCanteenName(request.getCanteenName());
        if (request.getFloorName() != null)
            food.setFloorName(request.getFloorName());
        if (request.getWindowName() != null) {
            Window window = windowRepository.findByName(request.getWindowName())
                    .orElseThrow(() -> new BadRequestException("窗口不存在: " + request.getWindowName()));
            food.setWindow(window);
        }
        if (request.getSellTime() != null)
            food.setSellTime(request.getSellTime());

        // 更新标签关系，先清除原有关系再添加新关系
        if (request.getTags() != null) {
            // 先移除所有旧标签关系
            food.getFoodTags().forEach(ft -> {
                Tag tag = ft.getTag();
                food.removeTag(tag);
            });
            // 再添加新标签关系
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = Tag.builder()
                                    .name(tagName)
                                    .build();
                            return tagRepository.save(newTag);
                        });
                food.addTag(tag);
            }
        }

        //至于和Post的关系留到PostServiceImpl里处理，FoodServiceImpl只负责Food的增删改查，不涉及Post的业务逻辑

        Food updated = foodRepository.save(food);
        FoodDetailDto dto = foodMapper.toDetailDto(updated);
        log.info("菜品更新成功: id={}", id);
        return dto;
    }

    @Override
    @Transactional
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜品", id));
        // 删除前先解除与所有 Tag 的关系，避免外键约束问题
        food.getFoodTags().forEach(ft -> {
            Tag tag = ft.getTag();
            food.removeTag(tag);
        });
        // 删除前先解除与所有 Post 的关系，避免外键约束问题（虽然现在FoodPost中间表的级联设置是ALL，但为了安全起见，还是先手动解除关系）
        food.getFoodPosts().forEach(fp -> {
            // 这里不需要删除Post实体，只需要解除Food和Post的关系，FoodPost中间实体会被级联删除
            fp.getPost().getFoodPosts().remove(fp);
            food.getFoodPosts().remove(fp);
        });
        // 和window的关系不用解除，因为Food持有window_id外键，删除Food时不会影响Window实体，也不会违反外键约束
        // 删除Food实体
        foodRepository.delete(food);
        log.info("菜品删除成功: id={}", id);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodSummaryDto> filterFoods(FilterFoodRequest request, Pageable pageable) {
        Page<FoodSummaryDto> page = foodRepository.
                filterFoods(request.getName(),
                        request.getCampus(),
                        request.getCanteen(),
                        request.getFloor(),
                        request.getWindow(),
                        request.getMinPrice(),// != null ? request.getMinPrice() : 0,
                        request.getMaxPrice(),// != null ? request.getMaxPrice() : Integer.MAX_VALUE,
                        pageable)
                .map(foodMapper::toSummaryDto);
        // 为空判断写在了repository的SQL里了，避免了在这里的复杂判断
        return PageResponse.of(page);
    }

    //上面那个没写Tag，这里补一版查询函数
    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodSummaryDto> filterFoodsWithTags(FilterFoodRequest request, Pageable pageable) {
        Page<FoodSummaryDto> page = foodRepository.
                filterFoodsWithTags(request.getName(),
                        request.getCampus(),
                        request.getCanteen(),
                        request.getFloor(),
                        request.getWindow(),
                        request.getMinPrice(),// != null ? request.getMinPrice() : 0,
                        request.getMaxPrice(),// != null ? request.getMaxPrice() : Integer.MAX_VALUE,
                        request.getTags(), // 新增标签过滤条件
                        pageable)
                .map(foodMapper::toSummaryDto);
        return PageResponse.of(page);
    }



    @Override
    @Transactional(readOnly = true)
    public List<Dtos.TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dtos.WindowSearchDto> getAllWindows() {
        return windowRepository.findAll().stream()
                .map(windowMapper::toSearchDto)
                .toList();
    }

}
