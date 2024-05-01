package com.aptner.v3.board.big_category.controller;

import com.aptner.v3.board.big_category.dto.CreateBigCategoryDto;
import com.aptner.v3.board.big_category.dto.DeleteBigCategoryDto;
import com.aptner.v3.board.big_category.dto.UpdateBigCategoryDto;
import com.aptner.v3.board.big_category.service.BigCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/big-category")
public class BigCategoryController {
    private final BigCategoryService bigCategoryService;

    @GetMapping
    public ResponseEntity<?> getBigCategoryList() {
        return new ResponseEntity<>(bigCategoryService.getBigCategoryList(), HttpStatus.OK);
    }

    @GetMapping("/{big-category-id}")
    public ResponseEntity<?> getSmallCategoryList(@PathVariable("big-category-id") long bigCategoryId) {
        return new ResponseEntity<>(bigCategoryService.getSmallCategoryList(bigCategoryId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createBigCategory(@RequestBody CreateBigCategoryDto.Request dto) {
        bigCategoryService.createBigCategory(dto);
        return new ResponseEntity<>("add " + dto + " to big category success", HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateBigCategory(@RequestBody UpdateBigCategoryDto.Request dto) {
        bigCategoryService.updateBigCategory(dto);
        return new ResponseEntity<>("update " + dto.getTargetId() + " to " + dto.getTo() + " success", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBigCategory(@RequestBody DeleteBigCategoryDto.Request dto) {
        bigCategoryService.deleteBigCategory(dto);
        return new ResponseEntity<>("delete " + dto.getId() + " from big category success)", HttpStatus.OK);
    }
}
