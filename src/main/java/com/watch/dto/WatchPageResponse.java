package com.watch.dto;

import java.util.List;

public record WatchPageResponse(
        List<WatchResponse> items,
        long total,
        Integer page,
        Integer perPage,
        Integer totalPages
) {
}
