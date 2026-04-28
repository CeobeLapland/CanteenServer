package com.canteen.controller;

import com.canteen.model.dto.Dtos.FoodDetailDto;
import com.canteen.model.dto.Dtos.FoodSummaryDto;
import com.canteen.model.request.Requests.CreateFoodRequest;
import com.canteen.model.response.ApiResponse;
import com.canteen.model.response.PageResponse;
import com.canteen.service.Services.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品相关接口
 *
 * <p>基础路径：/api/v1/foods
 *
 * <table border="1">
 *   <tr><th>方法</th><th>路径</th><th>描述</th></tr>
 *   <tr><td>GET</td><td>/api/v1/foods</td><td>菜品列表（分页）</td></tr>
 *   <tr><td>GET</td><td>/api/v1/foods/search</td><td>搜索菜品</td></tr>
 *   <tr><td>GET</td><td>/api/v1/foods/{id}</td><td>菜品详情</td></tr>
 *   <tr><td>POST</td><td>/api/v1/foods</td><td>新增菜品</td></tr>
 *   <tr><td>PUT</td><td>/api/v1/foods/{id}</td><td>更新菜品</td></tr>
 *   <tr><td>DELETE</td><td>/api/v1/foods/{id}</td><td>删除菜品</td></tr>
 * </table>
 */
@RestController
@RequestMapping("/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    /**
     * 获取所有菜品（分页）
     *
     * <p>GET /api/v1/foods?page=0&size=10&sort=name,asc
     *
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页数量（默认 10）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FoodSummaryDto>>> getAllFoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<FoodSummaryDto> result = foodService.getAllFoods(pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 搜索菜品（按名称模糊匹配，分页）
     *
     * <p>GET /api/v1/foods/search?keyword=红烧&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<FoodSummaryDto>>> searchFoods(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<FoodSummaryDto> result = foodService.searchFoods(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 获取菜品详情
     *
     * <p>GET /api/v1/foods/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodDetailDto>> getFoodById(@PathVariable Long id) {
        FoodDetailDto food = foodService.getFoodById(id);
        return ResponseEntity.ok(ApiResponse.ok(food));
    }

    /**
     * 新增菜品
     *
     * <p>POST /api/v1/foods
     * <p>请求体示例：
     * <pre>
     * {
     *   "name": "红烧肉",
     *   "description": "肥而不腻，入口即化",
     *   "price": 12.00,
     *   "imageUrl": "https://example.com/img/hongshaorou.jpg"
     * }
     * </pre>
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FoodDetailDto>> createFood(
            @Valid @RequestBody CreateFoodRequest request) {
        FoodDetailDto created = foodService.createFood(request);
        return ResponseEntity.status(201).body(ApiResponse.created(created));
    }

    /**
     * 更新菜品信息
     *
     * <p>PUT /api/v1/foods/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodDetailDto>> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody CreateFoodRequest request) {
        FoodDetailDto updated = foodService.updateFood(id, request);
        return ResponseEntity.ok(ApiResponse.ok("更新成功", updated));
    }

    /**
     * 删除菜品
     *
     * <p>DELETE /api/v1/foods/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
