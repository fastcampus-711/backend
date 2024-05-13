package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.global.exception.custom.InvalidURIException;
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

    public E getPost(long postId) {
        E domain = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        return domain;

    }

    public List<E> getPost(HttpServletRequest request) {
        String dtype = getDtype(request);

        if (dtype.equals("CommonPost"))
            return commonPostRepository.findAll();
        else
            return commonPostRepository.findByDtype(dtype);
    }

    public List<E> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        String dtype = getDtype(request);
        if (dtype.equals("CommonPost")) {
            return commonPostRepository.findByTitleContaining(keyword, pageable).getContent();
        } else {
            return commonPostRepository.findByTitleContainingAndDtype(keyword, dtype, pageable).getContent();
        }
    }

    public S createPost(Q requestDto) {
        E entity = (E) requestDto.toEntity();
        commonPostRepository.save(entity);
        return null;
    }

    public E updatePost(long postId, Q requestDto) {
        System.out.println(commonPostRepository.findById(postId).get().getClass().getTypeName());
        return commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new)
                .update(requestDto)
                ;
    }

    public void deletePost(long id) {
        commonPostRepository.deleteById(id);
    }

    private static String getDtype(HttpServletRequest request) {

        String[] URIs = request.getRequestURI()
                .split("/");
        String target = URIs.length <= 1 ? "" : URIs[1];

        return Arrays.stream(CategoryName.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseThrow(InvalidURIException::new)
                .getDtype();
    }
}
