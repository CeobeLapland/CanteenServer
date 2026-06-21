package com.canteen.controller;


import com.canteen.model.dto.SyncDto;
import com.canteen.model.response.ApiResponse;
import com.canteen.service.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这个controller负责Android前端room数据缓存的同步接口，提供增量更新和全量更新两种方式。
 * 使用的DTO是SyncDto
 */
@RestController
@RequestMapping("/v1/sync")
@RequiredArgsConstructor
public class SyncDataController {
    //先把要用的Service列一下
    private final Services.SyncService syncService;


    /**
     * 测试接口，验证一下后端和前端的通信是否正常，前端可以先调用这个接口来测试一下网络请求和数据解析是否正确，等确认没问题了再调用真正的同步接口。
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> testSync() {
        return ResponseEntity.ok(ApiResponse.ok("Sync API is working!"));
    }

    /**
     * 全量同步接口
     * <p>GET /api/v1/sync/all
     * <p>返回AllSyncDto中定义的所有实体的全量信息，前端会根据这个DTO的结构来创建表和字段，并把数据插入到room数据库中。
     * <p>注意：这个接口数据量较大，前端只有在应用更新或者用户手动触发全量同步时才会调用，平时请勿频繁调用。
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<SyncDto.AllSyncDto>> syncAllData() {
        // 依次调用SyncService中目前的11个syncxxx方法，把所有实体的全量信息都拉取出来，封装成AllSyncDto返回给前端
        SyncDto.AllSyncDto result = new SyncDto.AllSyncDto();
        result.foods = syncService.syncFoods(null);
        result.tags = syncService.syncTags(null);
        result.windows = syncService.syncWindows(null);
        result.posts = syncService.syncPosts(null);
        result.types = syncService.syncTypes(null);
        result.users = syncService.syncUsers(null);
        result.seasonings = syncService.syncSeasonings(null);
        result.comments = syncService.syncComments(null);

        result.foodTags = syncService.syncFoodTags(null);
        result.foodPosts = syncService.syncFoodPosts(null);
        result.postTypes = syncService.syncPostTypes(null);


        return ResponseEntity.ok(ApiResponse.ok(result));
    }


    /**
     * 增量同步接口
     * <p>GET /api/v1/sync/incremental?since=2024-06-01T00:00:00
     * <p>参数since是一个ISO格式的时间字符串，表示只同步这个时间点之后有更新的记录。前端会定期调用这个接口来获取最新的数据变化，以保持room数据库的实时性。
     * <p>返回结构同样是AllSyncDto，但里面只包含自since以来有更新的记录，前端会根据这些记录来更新room数据库中的对应数据。
     */
    @GetMapping("/incremental")
    public ResponseEntity<ApiResponse<SyncDto.AllSyncDto>> syncIncrementalData(String since) {
        SyncDto.AllSyncDto result = new SyncDto.AllSyncDto();
        result.foods = syncService.syncFoods(since);
        result.tags = syncService.syncTags(since);
        result.windows = syncService.syncWindows(since);
        result.posts = syncService.syncPosts(since);
        result.types = syncService.syncTypes(since);
        result.users = syncService.syncUsers(since);
        result.seasonings = syncService.syncSeasonings(since);
        result.comments = syncService.syncComments(since);

        result.foodTags = syncService.syncFoodTags(since);
        result.foodPosts = syncService.syncFoodPosts(since);
        result.postTypes = syncService.syncPostTypes(since);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    //下面是分开的增量同步接口，前端可以根据需要选择调用全量接口还是分开的增量接口

    @GetMapping("/foods")
    public ResponseEntity<ApiResponse<List<SyncDto.FoodSyncDto>>> syncFoods(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncFoods(since)));
    }

    @GetMapping("/windows")
    public ResponseEntity<ApiResponse<List<SyncDto.WindowSyncDto>>> syncWindows(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncWindows(since)));
    }

    @GetMapping("/tags")
    public ResponseEntity<ApiResponse<List<SyncDto.TagSyncDto>>> syncTags(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncTags(since)));
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<SyncDto.PostSyncDto>>> syncPosts(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncPosts(since)));
    }

    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<List<SyncDto.CommentSyncDto>>> syncComments(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncComments(since)));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<SyncDto.UserSyncDto>>> syncUsers(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncUsers(since)));
    }

    @GetMapping("/seasonings")
    public ResponseEntity<ApiResponse<List<SyncDto.SeasoningSyncDto>>> syncSeasonings(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncSeasonings(since)));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<SyncDto.TypeSyncDto>>> syncTypes(String since) {
        return ResponseEntity.ok(ApiResponse.ok(syncService.syncTypes(since)));
    }
}
