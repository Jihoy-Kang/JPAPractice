package com.courseori.server.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MultiResponseDto<T> { // <T> 제네릭 타입은 클래스, 인터페이스, 메서드 등의 타입을 파라미터로 사용할 수 있게 해주는 역할
    private List<T> data;
    private PageInfo pageInfo;

    public MultiResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1, page.getSize(),page.getTotalElements(), page.getTotalPages());
    }
}
