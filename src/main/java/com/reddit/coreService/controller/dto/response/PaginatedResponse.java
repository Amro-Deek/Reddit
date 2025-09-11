package com.reddit.coreService.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
    private List<T> items;

    // page metadata
    private int page;            // zero-based
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    // sort metadata (optional)
    private List<SortItem> sort;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class SortItem {
        private String property;
        private String direction; // "ASC" / "DESC"
    }

    // Factory to build from Spring Page — keeps Page out of your API surface
//    public static <T, R> PaginatedResponse<R> of(Page<T> page, Function<T, R> mapper) {
//        List<R> mapped = page.getContent().stream().map(mapper).toList();
//        List<SortItem> sortItems = page.getSort().stream()
//                .map(o -> new SortItem(o.getProperty(), o.getDirection().name()))
//                .toList();
//
//        PaginatedResponse<R> resp = new PaginatedResponse<>();
//        resp.setItems(mapped);
//        resp.setPage(page.getNumber());
//        resp.setSize(page.getSize());
//        resp.setTotalElements(page.getTotalElements());
//        resp.setTotalPages(page.getTotalPages());
//        resp.setFirst(page.isFirst());
//        resp.setLast(page.isLast());
//        resp.setHasNext(page.hasNext());
//        resp.setHasPrevious(page.hasPrevious());
//        resp.setSort(sortItems);
//        return resp;
//    }
    public static <R> PaginatedResponse<R> of(Page<R> page) {
        List<R> items = page.getContent();
        List<SortItem> sortItems = page.getSort().stream()
                .map(o -> new SortItem(o.getProperty(), o.getDirection().name()))
                .toList();

        PaginatedResponse<R> resp = new PaginatedResponse<>();
        resp.setItems(items);
        resp.setPage(page.getNumber());
        resp.setSize(page.getSize());
        resp.setTotalElements(page.getTotalElements());
        resp.setTotalPages(page.getTotalPages());
        resp.setFirst(page.isFirst());
        resp.setLast(page.isLast());
        resp.setHasNext(page.hasNext());
        resp.setHasPrevious(page.hasPrevious());
        resp.setSort(sortItems);
        return resp;
    }

}
