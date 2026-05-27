package com.canteen.model.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页响应包装类
 * <p>从 Spring Data 的 {@link Page} 转换而来，隐藏内部实现细节，
 * 只向客户端暴露必要的分页信息。
 * @param <T> 列表元素类型
 */
@Getter
public class PageResponse<T> {

    /** 当前页数据列表 */
    private final List<T> content;

    /** 当前页码（从 0 开始） */
    private final int page;

    /** 每页大小 */
    private final int size;

    /** 总记录数 */
    private final long totalElements;

    /** 总页数 */
    private final int totalPages;

    /** 是否为最后一页 */
    private final boolean last;

    private PageResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    /**
     * 从 Spring Data {@link Page} 对象构建
     * @param springPage Spring Data 分页结果
     * @param <T>        元素类型
     */
    public static <T> PageResponse<T> of(Page<T> springPage) {
        return new PageResponse<>(
                springPage.getContent(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.isLast()
        );
    }
}
