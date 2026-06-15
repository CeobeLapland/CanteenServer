package com.canteen.service.impl;

import com.canteen.mapper.Mappers;
import com.canteen.model.dto.SyncDto;

import com.canteen.repository.*;
import com.canteen.service.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements Services.SyncService {

    // region 先把要用的Repository和Mapper列一下，基本上是全部都会用，毕竟是全量/增量同步
    private final FoodRepository foodRepository;
    private final Mappers.FoodMapper foodMapper;

    private final PostRepository postRepository;
    private final Mappers.PostMapper postMapper;

    private final TagRepository tagRepository;
    private final Mappers.TagMapper tagMapper;

    private final CommentRepository commentRepository;
    private final Mappers.CommentMapper commentMapper;

    private final TypeRepository typeRepository;
    private final Mappers.TypeMapper typeMapper;

    private final UserRepository userRepository;
    private final Mappers.UserMapper userMapper;

    private final SeasoningRepository seasoningRepository;
    private final Mappers.SeasoningMapper seasoningMapper;

    private final WindowRepository windowRepository;
    private final Mappers.WindowMapper windowMapper;

    // 中间实体的Repository和Mapper
    private final FoodTagRepository foodTagRepository;
    private final Mappers.FoodTagMapper foodTagMapper;

    private final FoodPostRepository foodPostRepository;
    private final Mappers.FoodPostMapper foodPostMapper;

    private final PostTypeRepository postTypeRepository;
    private final Mappers.PostTypeMapper postTypeMapper;

    // endregion


    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.FoodSyncDto> syncFoods(String since) {
        if(since == null || since.isEmpty()) {
            // 全量同步
            return foodRepository.findAll().stream()
                    .map(foodMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            // 这里的实现比较简单，直接根据更新时间戳筛选出自指定时间以来新增或更新的菜品。实际情况可能更复杂，比如需要处理删除的菜品等。
            // 先把since转换成LocalDateTime
            var sinceTime = java.time.LocalDateTime.parse(since);
            return foodRepository.findUpdatedSince(sinceTime).stream()
                    .map(foodMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.WindowSyncDto> syncWindows(String since) {
        if(since == null || since.isEmpty()) {
            // 全量同步
            return windowRepository.findAll().stream()
                    .map(windowMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return windowRepository.findUpdatedSince(sinceTime).stream()
                    .map(windowMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.TagSyncDto> syncTags(String since) {
        if(since == null || since.isEmpty()) {
            // 全量同步
            return tagRepository.findAll().stream()
                    .map(tagMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return tagRepository.findUpdatedSince(sinceTime).stream()
                    .map(tagMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.PostSyncDto> syncPosts(String since) {
        // Post需要检查是否软删除，但到底是在这里写呢还是在Repository里写一个findUpdatedSinceIncludeDeleted方法呢？先简单实现一下，后续再优化。
        if(since == null || since.isEmpty()) {
            //全量同步，检查isDeleted（不知道这里方便这样写嘛）
            return postRepository.findAll().stream()
                    .filter(post -> !post.getIsDeleted())
                    .map(postMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return postRepository.findUpdatedSince(sinceTime).stream()
                    .filter(post -> !post.getIsDeleted())
                    .map(postMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.CommentSyncDto> syncComments(String since) {
        // comment和post一样，需要检查isDeleted
        if(since == null || since.isEmpty()) {
            //全量同步，检查isDeleted
            return commentRepository.findAll().stream()
                    .filter(comment -> !comment.getIsDeleted())
                    .map(commentMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return commentRepository.findUpdatedSince(sinceTime).stream()
                    .filter(comment -> !comment.getIsDeleted())
                    .map(commentMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.UserSyncDto> syncUsers(String since) {
        if(since == null || since.isEmpty()) {
            // 全量同步
            return userRepository.findAll().stream()
                    .map(userMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return userRepository.findUpdatedSince(sinceTime).stream()
                    .map(userMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.SeasoningSyncDto> syncSeasonings(String since) {
        if (since == null || since.isEmpty()) {
            // 全量同步
            return seasoningRepository.findAll().stream()
                    .map(seasoningMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return seasoningRepository.findUpdatedSince(sinceTime).stream()
                    .map(seasoningMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.TypeSyncDto> syncTypes(String since) {
        if (since == null || since.isEmpty()) {
            // 全量同步
            return typeRepository.findAll().stream()
                    .map(typeMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return typeRepository.findUpdatedSince(sinceTime).stream()
                    .map(typeMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.FoodTagSyncDto> syncFoodTags(String since) {
        if (since == null || since.isEmpty()) {
            // 全量同步
            return foodTagRepository.findAll().stream()
                    .map(foodTagMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return foodTagRepository.findUpdatedSince(sinceTime).stream()
                    .map(foodTagMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.FoodPostSyncDto> syncFoodPosts(String since) {
        if (since == null || since.isEmpty()) {
            // 全量同步
            return foodPostRepository.findAll().stream()
                    .map(foodPostMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return foodPostRepository.findUpdatedSince(sinceTime).stream()
                    .map(foodPostMapper::toSyncDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncDto.PostTypeSyncDto> syncPostTypes(String since) {
        if (since == null || since.isEmpty()) {
            // 全量同步
            return postTypeRepository.findAll().stream()
                    .map(postTypeMapper::toSyncDto)
                    .toList();
        } else {
            // 增量同步
            var sinceTime = java.time.LocalDateTime.parse(since);
            return postTypeRepository.findUpdatedSince(sinceTime).stream()
                    .map(postTypeMapper::toSyncDto)
                    .toList();
        }
    }
}
