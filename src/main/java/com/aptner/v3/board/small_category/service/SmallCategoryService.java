package com.aptner.v3.board.small_category.service;

import com.aptner.v3.board.SmallCategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.big_category.domain.BigCategory;
import com.aptner.v3.board.big_category.repository.BigCategoryRepository;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.small_category.domain.SmallCategory;
import com.aptner.v3.board.small_category.dto.CreateSmallCategoryDto;
import com.aptner.v3.board.small_category.dto.DeleteSmallCategoryDto;
import com.aptner.v3.board.small_category.dto.UpdateSmallCategoryDto;
import com.aptner.v3.board.small_category.repository.SmallCategoryRepository;
import com.aptner.v3.global.exception.custom.AlreadyExistsSmallCategoryNameException;
import com.aptner.v3.global.exception.custom.NotExistsBigCategoryIdException;
import com.aptner.v3.global.exception.custom.NotExistsSmallCategoryIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SmallCategoryService {
    private final BigCategoryRepository bigCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;
    private final CommonPostRepository commonPostRepository;

    public List<CommonPost> getPostList(long smallCategoryId) {
        SmallCategory smallCategory = getSmallCategory(smallCategoryId);

        return commonPostRepository.findBySmallCategoryName(smallCategory.getSmallCategoryName().getTableName());
    }

    public void createSmallCategory(long bigCategoryId, CreateSmallCategoryDto.Request dto) {
        BigCategory bigCategory = getBigCategory(bigCategoryId);

        checkNameIsExistsOrNot(dto.getSmallCategoryName(), bigCategory);

        smallCategoryRepository.save(SmallCategory.of(dto.getSmallCategoryName(), bigCategory));
    }

    public void updateSmallCategory(UpdateSmallCategoryDto.Request dto) {
        smallCategoryRepository.updateName(dto.getId(), dto.getSmallCategoryName().getTableName());
    }

    public void deleteSmallCategory(DeleteSmallCategoryDto.Request dto) {
        smallCategoryRepository.deleteById(dto.getId());
    }

    private SmallCategory getSmallCategory(long smallCategoryId) {
        return smallCategoryRepository.findById(smallCategoryId)
                .orElseThrow(NotExistsSmallCategoryIdException::new);
    }

    private BigCategory getBigCategory(long bigCategoryId) {
        return bigCategoryRepository.findById(bigCategoryId)
                .orElseThrow(NotExistsBigCategoryIdException::new);
    }

    private static void checkNameIsExistsOrNot(SmallCategoryName name, BigCategory bigCategory) {
        if (bigCategory.getSmallCategories()
                .stream().parallel()
                .anyMatch(smallCategory -> smallCategory.getSmallCategoryName().equals(name)))
            throw new AlreadyExistsSmallCategoryNameException();
    }
}
