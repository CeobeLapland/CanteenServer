package com.canteen.model.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO（Data Transfer Object）数据传输对象
 * <p>将多个 DTO 集中在一个文件中便于管理，也可拆分为独立文件。
 * DTO 用于向客户端返回数据，不暴露 Entity 内部关联（避免循环序列化）。
 */
public class Dtos {

    // ======================================================
    //  User DTO
    // ======================================================

    /** 用户信息（对外展示） */
    @Data
    public static class UserDto {
        private Long id;
        private String name;
        private String permission; // 例如 "USER"、"ADMIN"
        private LocalDateTime createdAt;

        // TODO: 后续加字段时，在 UserMapper 中同步添加映射
        // private String avatarUrl;
    }

    // ======================================================
    //  Food DTO
    // ======================================================

    /** 菜品简要信息（列表展示用） */
    @Data
    public static class FoodSummaryDto {
        private Long id;
        private String name;
        private Integer price;
        private String imageUrl;
        // 新增简要字段，方便客户端列表展示
        private String campus;
        private String canteen;
        private Float averageRating;
    }

    /** 菜品详情（单个菜品页） */
    @Data
    public static class FoodDetailDto {
        private Long id;
        private String name;
        private String description;
        private Integer price;
        private String imageUrl;
        private LocalDateTime createdAt;

        // 新增详细信息字段
        private String campus;
        private String canteen;
        private String floor;
        private String window;
        private String sellTime;
        private List<String> tags;
        private Float averageRating;
        private Integer ratingCount;

        /** 该菜品关联的帖子数量（统计信息） */
        private int postCount;
    }

    // ======================================================
    //  Post DTO
    // ======================================================

    /** 帖子简要信息（首页/列表） */
    @Data
    public static class PostSummaryDto {
        private Long id;
        private String title;

        private Integer viewCount;
        private Integer likeCount;
        private UserDto author;
        private List<FoodSummaryDto> foods;
        private int commentCount;
        private LocalDateTime createdAt;
    }

    /** 帖子详情（帖子详情页） */
    @Data
    public static class PostDetailDto {
        private Long id;
        private String title;
        private String content;

        private Integer viewCount;
        private Integer likeCount;
        private UserDto author;
        private Set<FoodSummaryDto> foods;
        private List<CommentDto> comments;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    // ======================================================
    //  Comment DTO
    // ======================================================

    /** 评论信息 */
    @Data
    public static class CommentDto {
        private Long id;
        private String content;
        private UserDto author;
        private LocalDateTime createdAt;
        // 后续扩展（楼中楼）
        // private Long parentId;
        // private List<CommentDto> replies;
    }



    @Data
    public static class WindowDto {
        private String name;

        private String floor;
        private String canteen;
        private String campus;
    }

    @Data
    public static class TagDto {
        private String name;
    }
}
