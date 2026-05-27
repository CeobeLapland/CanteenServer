package com.canteen.mapper;

import com.canteen.model.dto.Dtos.*;
import com.canteen.model.entity.Comment;
import com.canteen.model.entity.Food;
import com.canteen.model.entity.Post;
import com.canteen.model.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

/**
 * MapStruct Entity ↔ DTO 转换器
 * <p>MapStruct 在编译期生成实现类，无运行时反射，性能优异。
 * 所有 Mapper 集中在此文件，若业务复杂可拆分为独立接口。
 * <p>Spring 会将生成的实现类注册为 Bean，直接 {@code @Autowired} 即可使用。
 */
public class Mappers {

    // ======================================================
    //  UserMapper
    // ======================================================
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface UserMapper {

        UserDto toDto(User user);

        List<UserDto> toDtoList(List<User> users);
    }


    // ======================================================
    //  FoodMapper
    // ======================================================
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface FoodMapper {

        FoodSummaryDto toSummaryDto(Food food);

        /**
         * 转换为详情 DTO
         * postCount 由 Service 层手动填充（避免 N+1 查询）
         */
        @Mapping(target = "postCount", ignore = true)
        FoodDetailDto toDetailDto(Food food);

        List<FoodSummaryDto> toSummaryDtoList(List<Food> foods);

        Set<FoodSummaryDto> toSummaryDtoSet(Set<Food> foods);
    }


    // ======================================================
    //  PostMapper
    // ======================================================
    @org.mapstruct.Mapper(componentModel = "spring", uses = {UserMapper.class, FoodMapper.class})
    public interface PostMapper {

        /**
         * 转换为列表 DTO（摘要）
         * commentCount 由 Service 层手动填充
         */
        @Mapping(target = "commentCount", ignore = true)
        PostSummaryDto toSummaryDto(Post post);

        /** 转换为详情 DTO（包含评论列表） */
        PostDetailDto toDetailDto(Post post);

        List<PostSummaryDto> toSummaryDtoList(List<Post> posts);
    }


    // ======================================================
    //  CommentMapper
    // ======================================================
    @org.mapstruct.Mapper(componentModel = "spring", uses = {UserMapper.class})
    public interface CommentMapper {

        CommentDto toDto(Comment comment);

        List<CommentDto> toDtoList(List<Comment> comments);
    }
}
