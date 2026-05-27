package com.canteen.service;

import com.canteen.model.dto.Dtos.*;
import com.canteen.model.request.Requests.*;
import com.canteen.model.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service 接口层
 * <p>定义业务逻辑的契约，实现类在 service/impl 包中。
 * 面向接口编程便于后续替换实现（例如引入缓存层）和单元测试（Mock）。
 */
public class Services
{

    //  UserService
    public interface UserService
    {

        /** 根据 ID 获取用户信息 */
        UserDto getUserById(Long id);

        /** 创建新用户 */
        UserDto createUser(CreateUserRequest request);

        // TODO: updateUser、deleteUser、getUserByName 等
    }



    //  FoodService
    public interface FoodService
    {

        /** 获取所有菜品（不分页，内部使用） */
        List<FoodDetailDto> getAllFoodsNoPagination();

        /** 获取自指定时间以来新增或更新的菜品列表（增量更新） */
        List<FoodDetailDto> getUpdatedFoods(String since);

        /** 获取菜品详情 */
        FoodDetailDto getFoodById(Long id);

        /** 获取所有菜品（分页） */
        PageResponse<FoodSummaryDto> getAllFoods(Pageable pageable);

        /** 按关键词搜索菜品（分页） */
        PageResponse<FoodSummaryDto> searchFoods(String keyword, Pageable pageable);

        /** 创建菜品 */
        FoodDetailDto createFood(CreateFoodRequest request);

        /** 更新菜品 */
        FoodDetailDto updateFood(Long id, CreateFoodRequest request);

        /** 删除菜品 */
        void deleteFood(Long id);
    }



    //  PostService
    public interface PostService
    {

        /** 获取帖子详情 */
        PostDetailDto getPostById(Long id);

        /** 获取所有帖子（首页 Feed，按时间倒序，分页） */
        PageResponse<PostSummaryDto> getAllPosts(Pageable pageable);

        /** 获取某菜品下的所有帖子（分页） */
        PageResponse<PostSummaryDto> getPostsByFood(Long foodId, Pageable pageable);

        /** 获取某用户的所有帖子（分页） */
        PageResponse<PostSummaryDto> getPostsByUser(Long userId, Pageable pageable);

        /** 按标题搜索帖子（分页） */
        PageResponse<PostSummaryDto> searchPosts(String keyword, Pageable pageable);

        /** 发布帖子 */
        PostDetailDto createPost(CreatePostRequest request);

        /** 更新帖子 */
        PostDetailDto updatePost(Long id, UpdatePostRequest request);

        /** 删除帖子 */
        void deletePost(Long id);
    }



    //  CommentService
    public interface CommentService
    {

        /** 获取某帖子下的评论列表（分页） */
        PageResponse<CommentDto> getCommentsByPost(Long postId, Pageable pageable);

        /** 发布评论 */
        CommentDto createComment(Long postId, CreateCommentRequest request);

        /** 删除评论 */
        void deleteComment(Long commentId);
    }
}
