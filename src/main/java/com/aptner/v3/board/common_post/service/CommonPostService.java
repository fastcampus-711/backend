package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.global.exception.custom.InvalidURIException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonPostService<T extends CommonPost> {
    private final CommonPostRepository<T> commonPostRepository;

    public Object getPostList(Long postId) {
        return commonPostRepository.findById(postId);

    }

    public List<T> getPostList(HttpServletRequest request) {
        CategoryName categoryName = getCategoryName(request);

        return commonPostRepository.findByDtype(categoryName.getDtype());
    }

    public <U extends CommonPostDto.CreateRequest> void createPost(U requestDto) {
        createPost((T) requestDto.toEntity());
    }

    public void createPost(T entity) {
        commonPostRepository.save(entity);
    }


    public <U extends CommonPostDto.UpdateRequest> void updatePost(U requestDto) {
        commonPostRepository.findById(requestDto.getId())
                .orElseThrow(InvalidTableIdException::new)
                .update(requestDto);
    }

    public void deletePost(long id) {
        commonPostRepository.deleteById(id);
    }

    private static CategoryName getCategoryName(HttpServletRequest request) {
        String target = Arrays.stream(request.getRequestURI()
                .split("/")).toList().get(1);

        return Arrays.stream(CategoryName.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseThrow(InvalidURIException::new);
    }
}
