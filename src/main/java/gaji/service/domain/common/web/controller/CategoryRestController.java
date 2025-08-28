package gaji.service.domain.common.web.controller;


import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.common.web.dto.CategoryResponseDTO;
import gaji.service.global.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    // TODO: 관리자만 요청 가능하도록 API 권한 설정
    @GetMapping
    @Operation(summary = "전체 카테고리 조회 API", description = "카테고리 전체 목록을 조회하는 API입니다.")
    public BaseResponse<List<CategoryResponseDTO.BaseDTO>> getCategoryList() {
        return BaseResponse.onSuccess(categoryService.findAllCategory());
    }
}
