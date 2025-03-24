package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedProductResponse {
    private List<ProductResponseDto> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
}
