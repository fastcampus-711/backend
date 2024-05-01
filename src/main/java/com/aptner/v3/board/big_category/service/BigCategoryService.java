package com.aptner.v3.board.big_category.service;

import com.aptner.v3.board.big_category.domain.BigCategory;
import com.aptner.v3.board.small_category.domain.SmallCategory;
import com.aptner.v3.board.big_category.dto.CreateBigCategoryDto;
import com.aptner.v3.board.big_category.dto.DeleteBigCategoryDto;
import com.aptner.v3.board.big_category.dto.UpdateBigCategoryDto;
import com.aptner.v3.board.big_category.repository.BigCategoryRepository;
import com.aptner.v3.global.exception.custom.NotExistsBigCategoryIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BigCategoryService {
    private final BigCategoryRepository bigCategoryRepository;

    public List<BigCategory> getBigCategoryList() {
        return bigCategoryRepository.findAll();
    }

    public List<SmallCategory> getSmallCategoryList(long bigCategoryId) {
        return bigCategoryRepository.findById(bigCategoryId)
                .orElseThrow(NotExistsBigCategoryIdException::new)
                .getSmallCategories();
    }

    public void createBigCategory(CreateBigCategoryDto.Request dto) {
        bigCategoryRepository.save(dto.toEntity());
    }

    public void updateBigCategory(UpdateBigCategoryDto.Request dto) {
        bigCategoryRepository.updateName(dto.getTargetId(), dto.getTo());
    }

    public void deleteBigCategory(DeleteBigCategoryDto.Request dto) {
        bigCategoryRepository.deleteById(dto.getId());
    }
}
