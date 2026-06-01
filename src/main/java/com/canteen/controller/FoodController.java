package com.canteen.controller;

import com.canteen.model.dto.Dtos;
import com.canteen.model.dto.Dtos.FoodDetailDto;
import com.canteen.model.dto.Dtos.FoodSummaryDto;
import com.canteen.model.request.Requests;
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

import java.util.List;

/**
 * 菜品相关接口
 * <p>基础路径：/api/v1/foods
 * <table border="1">
 *   <tr><th>方法</th><th>路径</th><th>描述</th></tr>
 *   <tr><td>GET</td><td>/api/v1/foods</td><td>菜品列表（分页）</td></tr>
 *   <tr><td>GET</td><td>/api/v1/foods/search</td><td>搜索菜品</td></tr>
 *   <tr><td>GET</td><td>/api/v1/foods/{id}</td><td>菜品详情</td></tr>
 *   <tr><td>POST</td><td>/api/v1/foods</td><td>新增菜品</td></tr>
 *   <tr><td>POST</td><td>/api/v1/foods/batch</td><td>批量新增菜品</td></tr>
 *   <tr><td>PUT</td><td>/api/v1/foods/{id}</td><td>更新菜品</td></tr>
 *   <tr><td>DELETE</td><td>/api/v1/foods/{id}</td><td>删除菜品</td></tr>
 *   <tr><td>POST</td><td>/api/v1/foods/filter</td><td>按照FilterFoodRequest筛选菜品（分页）</td></tr>
 * </table>
 */
@RestController
@RequestMapping("/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    /**
     * 测试函数
     * <p>GET /api/v1/foods/test
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.ok("接口测试成功"));
    }

    /**
     * 全量拉取所有菜品（不分页）
     * <p>GET /api/v1/foods/all
     * <p>仅供内部使用，前端请勿调用（数据量大时会有性能问题）。后续可删除或改为管理员接口。
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<FoodDetailDto>>> getAllFoodsNoPagination() {
        List<FoodDetailDto> result = foodService.getAllFoodsNoPagination();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 获取已更新的菜品列表（增量更新）
     * <p>GET /api/v1/foods/updated?since=2024-01-01T00:00:00
     * <p>返回自指定时间以来新增或更新的菜品列表，供前端增量更新使用。
     */
    @GetMapping("/updated")
    public ResponseEntity<ApiResponse<List<FoodDetailDto>>> getUpdatedFoods(@RequestParam String since) {
        List<FoodDetailDto> result = foodService.getUpdatedFoods(since);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 获取所有菜品（分页）
     * <p>GET /api/v1/foods?page=0&size=10&sort=name,asc
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页数量（默认 10）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FoodSummaryDto>>> getAllFoods(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<FoodSummaryDto> result = foodService.getAllFoods(pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 搜索菜品（按名称模糊匹配，分页）
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
     * <p>GET /api/v1/foods/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodDetailDto>> getFoodById(@PathVariable Long id) {
        FoodDetailDto food = foodService.getFoodById(id);
        return ResponseEntity.ok(ApiResponse.ok(food));
    }

    /**
     * 新增菜品
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

    /** 批量新增菜品
     * <p>POST /api/v1/foods/batch
     * <p>请求体示例：
     * <pre>
     * [
     *   {
     *     "name": "红烧肉",
     *     "description": "肥而不腻，入口即化",
     *     "price": 12.00,
     *     "imageUrl": "https://example.com/img/hongshaorou.jpg"
     *   },
     *   {
     *     "name": "宫保鸡丁",
     *     "description": "香辣可口，鸡肉鲜嫩",
     *     "price": 10.00,
     *     "imageUrl": "https://example.com/img/gongbaojiding.jpg"
     *   }
     * ]
     * </pre>
     */
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<FoodDetailDto>>> createFoods(
            @Valid @RequestBody List<CreateFoodRequest> requests) {
        List<FoodDetailDto> createdList = foodService.createFoods(requests);
        return ResponseEntity.status(201).body(ApiResponse.created(createdList));
    }

    /**
     * 更新菜品信息
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
     * <p>DELETE /api/v1/foods/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 按照FilterFoodRequest筛选菜品（分页）
     * <p>POST /api/v1/foods/filter?page=0&size=10
     * <p>请求体示例：
     * <pre>
     * {
     *   "name": "肉",
     *   "campus": "主校区",
     *   "canteen": "一食堂",
     *   "floor": "二层",
     *   "window": "窗口1",
     *   "minPrice": 5,
     *   "maxPrice": 20
     * }
     * </pre>
     */
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<PageResponse<FoodSummaryDto>>> filterFoods(
            @Valid @RequestBody Requests.FilterFoodRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<FoodSummaryDto> result = foodService.filterFoods(request, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }



    // 其他的一部分先写在FoodService里，后续再根据需要添加到Controller里
    /**
     * 获取所有Tag列表（不分页）
     * <p>GET /api/v1/foods/tags
     */
    @GetMapping("/tags")
    public ResponseEntity<ApiResponse<List<Dtos.TagDto>>> getAllTags() {
        List<Dtos.TagDto> tags = foodService.getAllTags();
        return ResponseEntity.ok(ApiResponse.ok(tags));
    }

    /**
     * 获取所有window列表（不分页）
     * <p>GET /api/v1/foods/windows
     */
    @GetMapping("/windows")
    public ResponseEntity<ApiResponse<List<Dtos.WindowDto>>> getAllWindows() {
        List<Dtos.WindowDto> windows = foodService.getAllWindows();
        return ResponseEntity.ok(ApiResponse.ok(windows));
    }

    /**
     * 获取所有floor列表（不分页）
     * <p>GET /api/v1/foods/floors
     */
    /*@GetMapping("/floors")
    public ResponseEntity<ApiResponse<List<Dtos.FloorDto>>> getAllFloors() {
        List<Dtos.FloorDto> floors = foodService.getAllFloors();
        return ResponseEntity.ok(ApiResponse.ok(floors));
    }*/

    /**
     * 获取所有canteen列表（不分页）
     * <p>GET /api/v1/foods/canteens
     */
    /*@GetMapping("/canteens")
    public ResponseEntity<ApiResponse<List<Dtos.CanteenDto>>> getAllCanteens() {
        List<Dtos.CanteenDto> canteens = foodService.getAllCanteens();
        return ResponseEntity.ok(ApiResponse.ok(canteens));
    }*/

    /**
     * 获取所有campus列表（不分页）
     * <p>GET /api/v1/foods/campuses
     */
    /*@GetMapping("/campuses")
    public ResponseEntity<ApiResponse<List<Dtos.CampusDto>>> getAllCampuses() {
        List<Dtos.CampusDto> campuses = foodService.getAllCampuses();
        return ResponseEntity.ok(ApiResponse.ok(campuses));
    }*/
}