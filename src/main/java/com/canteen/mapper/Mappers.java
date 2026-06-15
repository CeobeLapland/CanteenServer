package com.canteen.mapper;

import com.canteen.model.dto.Dtos.*;
import com.canteen.model.dto.SyncDto;
import com.canteen.model.entity.*;
import com.canteen.model.entity.mid.FoodPost;
import com.canteen.model.entity.mid.FoodTag;
import com.canteen.model.entity.mid.PostType;
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

    
    //  UserMapper
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface UserMapper {

        UserDto toDto(User user);

        SyncDto.UserSyncDto toSyncDto(User user);
        //List<UserDto> toDtoList(List<User> users);
    }


    
    //  FoodMapper
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface FoodMapper {

        //把Window转化为String
        @Mapping(target = "window", source = "window.name")
        FoodSummaryDto toSummaryDto(Food food);

        /** 转换为详情 DTO */
        //@Mapping(target = "postCount", ignore = true)
        //@Mapping(target = "tags", source = "tags", defaultValueExpression = "java(tags.stream().map(Tag::getName).toList())")
        //@Mapping(target = "tags", expression = "java(food.getTags().stream().map(com.canteen.model.entity.Tag::getName).toList())")
        //现在已经改成了FoodTag中间表，所以Food里用的是List<FoodTag>，需要在这里转换一下
        @Mapping(target = "tags", expression = "java(food.getFoodTags().stream().map(ft -> ft.getTag().getName()).toList())")
        @Mapping(target = "window", source = "window.name")
        FoodDetailDto toDetailDto(Food food);


        /** 转化为Sync DTO，包含所有字段但不包含关联对象（如 posts） */
        @Mapping(target = "windowId", source = "window.id")
        SyncDto.FoodSyncDto toSyncDto(Food food);


        //List<FoodSummaryDto> toSummaryDtoList(List<Food> foods);
        //Set<FoodSummaryDto> toSummaryDtoSet(Set<Food> foods);
    }


    
    //  PostMapper
    @org.mapstruct.Mapper(componentModel = "spring", uses = {CommentMapper.class, UserMapper.class, FoodMapper.class})
    public interface PostMapper {

        /**
         * 转换为列表 DTO（摘要）
         * commentCount 由 Service 层手动填充
         */
        @Mapping(target = "commentCount", ignore = true)
        PostSummaryDto toSummaryDto(Post post);

        /**
         * 转换为详情 DTO（包含评论列表）
         */
        PostDetailDto toDetailDto(Post post);


        SyncDto.PostSyncDto toSyncDto(Post post);
        //List<PostSummaryDto> toSummaryDtoList(List<Post> posts);
    }


    
    //  CommentMapper
    @org.mapstruct.Mapper(componentModel = "spring", uses = {UserMapper.class})
    public interface CommentMapper {

        CommentDto toDto(Comment comment);

        SyncDto.CommentSyncDto toSyncDto(Comment comment);
        //List<CommentDto> toDtoList(List<Comment> comments);
    }


    // TagMapper
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface TagMapper {
        TagDto toDto(Tag tag);

        SyncDto.TagSyncDto toSyncDto(Tag tag);
        //List<TagDto> toDtoList(List<Tag> tags);
    }


    // WindowMapper
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface WindowMapper {
        @Mapping(target = "campus", source = "campusName")
        @Mapping(target = "canteen", source = "canteenName")
        @Mapping(target = "floor", source = "floorName")
        @Mapping(target = "name", source = "name")
        WindowSearchDto toSearchDto(Window window);

        WindowDetailDto toDetailDto(Window window);

        SyncDto.WindowSyncDto toSyncDto(Window window);
        //List<WindowDto> toDtoList(List<Window> windows);
    }


    // SeasoningMapper
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface SeasoningMapper {
        //这个只有Sync版本的
        @Mapping(target = "windowId", source = "window.id")
        SyncDto.SeasoningSyncDto toSyncDto(Seasoning seasoning);
    }


    // TypeMapper
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface TypeMapper {
        SyncDto.TypeSyncDto toSyncDto(Type type);
    }



    //一些中间实体的Mapper，不知道要不要写
    @org.mapstruct.Mapper(componentModel = "spring")
    public interface FoodPostMapper {
        @Mapping(target = "foodId", source = "food.id")
        @Mapping(target = "postId", source = "post.id")
        SyncDto.FoodPostSyncDto toSyncDto(FoodPost foodPost);
    }


    @org.mapstruct.Mapper(componentModel = "spring")
    public interface FoodTagMapper {
        @Mapping(target = "foodId", source = "food.id")
        @Mapping(target = "tagId", source = "tag.id")
        SyncDto.FoodTagSyncDto toSyncDto(FoodTag foodTag);
    }


    @org.mapstruct.Mapper(componentModel = "spring")
    public interface PostTypeMapper {
        @Mapping(target = "postId", source = "post.id")
        @Mapping(target = "typeId", source = "type.id")
        SyncDto.PostTypeSyncDto toSyncDto(PostType postType);
    }
}
