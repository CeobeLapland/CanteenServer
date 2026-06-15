package com.canteen.model.request;

import com.canteen.model.entity.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
import lombok.Data;

//import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 请求体对象（Request Body）
 * <p>用于接收客户端 POST/PUT 请求的 JSON 数据，配合 {@code @Valid} 进行参数校验。
 * 可将各类请求拆分为独立文件，此处集中管理便于初期快速开发。
 */
public class Requests {

    // ======================================================
    //  User 请求
    // ======================================================

    /** 创建用户请求 */
    @Data
    public static class CreateUserRequest {

        @NotBlank(message = "用户名不能为空")
        @Size(min = 2, max = 50, message = "用户名长度须在 2~50 之间")
        private String name;

        // TODO: 后续添加 email、password 等字段
    }


    // ======================================================
    //  Food 请求
    // ======================================================

    /** 创建/更新菜品请求 */
    @Data
    public static class CreateFoodRequest {


        @NotBlank(message = "菜品名称不能为空")
        @Size(max = 100, message = "菜品名称不超过 100 字")
        private String name;

        @Size(max = 1000, message = "描述不超过 1000 字")
        private String description;

        @NotNull(message = "价格不能为空")
        @Min(value = 0, message = "价格必须大于或等于 0")
        private Integer price;

        //private String imageUrl;

        // 新增字段：地理/销售信息
        private String campusName;
        private String canteenName;
        private String floorName;

        //private Long windowId;
        private String windowName;//创建请求时不知道ID

        private String sellTime;

        /** 标签列表，允许为空 */
        private List<String> tags;

    }


    /** 更新菜品请求（允许部分字段为空，表示不更新） */
    @Data
    public static class UpdateFoodRequest {
        @Size(max = 100, message = "菜品名称不超过 100 字")
        private String name;

        @Size(max = 1000, message = "描述不超过 1000 字")
        private String description;

        @DecimalMin(value = "0.01", message = "价格必须大于 0")
        private Integer price;

        //private String imageUrl;

        // 新增字段：地理/销售信息
        private String campusName;
        private String canteenName;
        private String floorName;

        //private Long windowid;
        private String windowName;//创建请求时不知道ID

        private String sellTime;

        /** 标签列表，允许为空 */
        private List<String> tags;
    }


    /** 筛选菜品请求（GET 请求的查询参数） */
    @Data
    public static class FilterFoodRequest {

        private String name;    // 按名称模糊匹配

        private String campus;  // 按校区精确匹配

        private String canteen; // 按食堂精确匹配

        private String floor;   // 按楼层精确匹配

        private String window;  // 按窗口精确匹配

        private List<String> tags;     // 按标签模糊匹配（至少包含一个标签）

        private Integer minPrice;   // 价格区间下限

        private Integer maxPrice;   // 价格区间上限
    }

    // ======================================================
    //  Post 请求
    // ======================================================

    /** 发布帖子请求 */
    @Data
    public static class CreatePostRequest {

        @NotBlank(message = "标题不能为空")
        @Size(max = 200, message = "标题不超过 200 字")
        private String title;

        @NotBlank(message = "内容不能为空")
        private String content;

        private Integer viewCount = 0;   // 可选，默认 0

        private Integer likeCount = 0;   // 可选，默认 0

        /** 帖子作者 ID */
        @NotNull(message = "作者 ID 不能为空")
        private Long userId;

        /**
         * 关联的菜品 ID 集合（至少关联一道菜）
         * 若不强制要求，可去掉 @NotEmpty
         */
        @NotEmpty(message = "请至少关联一道菜品")
        private Set<Long> foodIds;
    }

    /** 更新帖子请求（允许部分字段为空，表示不更新） */
    @Data
    public static class UpdatePostRequest {

        @Size(max = 200, message = "标题不超过 200 字")
        private String title;

        private String content;

        private Integer viewCount;

        private Integer likeCount;

        private Set<Long> foodIds;
    }

    // ======================================================
    //  Comment 请求
    // ======================================================

    /** 发布评论请求 */
    @Data
    public static class CreateCommentRequest {

        @NotBlank(message = "评论内容不能为空")
        @Size(max = 500, message = "评论内容不超过 500 字")
        private String content;

        @NotNull(message = "评论者 ID 不能为空")
        private Long userId;

        // postId 从路径参数 @PathVariable 获取，无需在请求体中传递
    }
}
