package com.aptner.v3.board.small_category.controller;

import com.aptner.v3.board.small_category.dto.CreateSmallCategoryDto;
import com.aptner.v3.board.small_category.dto.DeleteSmallCategoryDto;
import com.aptner.v3.board.small_category.dto.UpdateSmallCategoryDto;
import com.aptner.v3.board.small_category.service.SmallCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/big-category/{big-category-id}/small-category")
public class SmallCategoryController {
    private final SmallCategoryService smallCategoryService;

    @GetMapping("/{small-category-id}")
    public ResponseEntity<?> getPostList(@PathVariable("small-category-id") long smallCategoryId) {
        return new ResponseEntity<>(smallCategoryService.getPostList(smallCategoryId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSmallCategory(@PathVariable("big-category-id") long bigCategoryId, @RequestBody CreateSmallCategoryDto.Request dto) {
        smallCategoryService.createSmallCategory(bigCategoryId, dto);
        return new ResponseEntity<>("add " + dto + " to small category success", HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateSmallCategory(@RequestBody UpdateSmallCategoryDto.Request dto) {
        smallCategoryService.updateSmallCategory(dto);
        return new ResponseEntity<>("update " + dto.getId() + " to " + dto.getName() + " success", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSmallCategory(@RequestBody DeleteSmallCategoryDto.Request dto) {
        smallCategoryService.deleteSmallCategory(dto);
        return new ResponseEntity<>("delete " + dto.getId() + " from small category success)", HttpStatus.OK);
    }
}
