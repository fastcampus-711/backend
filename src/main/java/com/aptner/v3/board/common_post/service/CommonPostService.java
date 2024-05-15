package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@RequiredArgsConstructor
public class CommonPostService<E extends CommonPost,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response> {
    private final CommonPostRepository<E> commonPostRepository;

    public S getPost(long postId) {
        return (S) commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new)
                .toResponseDto();

    }

    public List<S> getPost(HttpServletRequest request) {
        String dtype = getDtype(request);

        List<E> list;
        if (dtype.equals("CommonPost"))
            list = commonPostRepository.findAll();
        else
            list = commonPostRepository.findByDtype(dtype);

        return list.stream()
                .map(e -> (S) e.toResponseDto())
                .toList();
    }

    public List<S> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        String dtype = getDtype(request);

        List<E> list;
        if (dtype.equals("CommonPost"))
            list = commonPostRepository.findByTitleContaining(keyword, pageable).getContent();
        else
            list = commonPostRepository.findByTitleContainingAndDtype(keyword, dtype, pageable).getContent();

        return list.stream()
                .map(e -> (S)e.toResponseDto())
                .toList();
    }

    public S createPost(Q requestDto) {
        E entity = (E) requestDto.toEntity();
        commonPostRepository.save(entity);
        return null;
    }

    public S updatePost(long postId, Q requestDto) {
        return (S) commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new)
                .update(requestDto)
                .toResponseDto();
    }

    public long deletePost(long id) {
        commonPostRepository.deleteById(id);
        return id;
    }

    private static String getDtype(HttpServletRequest request) {

        String[] URIs = request.getRequestURI()
                .split("/");
        String target = URIs.length <= 2 ? "" : URIs[2];

        return Arrays.stream(CategoryCode.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseGet(() -> CategoryCode.공통)
                .getDtype();
    }
}
