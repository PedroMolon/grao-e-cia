package com.pedromolon.catalog_service.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageResponse<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageResponse(
        @JsonProperty("content") List<T> content,
        @JsonProperty("number") int number,
        @JsonProperty("size") int size,
        @JsonProperty("totalElements") long totalElements) {
        super(content, PageRequest.of(number, size > 0 ? size : 1), totalElements);
    }

    public PageResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResponse(List<T> content) {
        super(content);
    }

}
