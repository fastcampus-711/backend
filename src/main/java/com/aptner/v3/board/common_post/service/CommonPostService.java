package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.CategoryCode;
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
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonPostService<T extends CommonPost> {
    private final CommonPostRepository<T> commonPostRepository;

    public T getPost(long postId) {
        T domain = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        return domain;

    }

    public List<T> getPost(HttpServletRequest request) {
        String dtype = getDtype(request);

        if (dtype.equals("CommonPost"))
            return commonPostRepository.findAll();
        else
            return commonPostRepository.findByDtype(dtype);
    }

    public List<T> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        String dtype = getDtype(request);
        if (dtype.equals("CommonPost")) {
                return commonPostRepository.findByTitleContaining(keyword, pageable).getContent();
        } else {
            return commonPostRepository.findByTitleContainingAndDtype(keyword, dtype, pageable).getContent();
        }
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

    private static String getDtype(HttpServletRequest request) {

        String[] URIs = request.getRequestURI()
                .split("/");
        String target = URIs.length <= 1 ? "" : URIs[1];

        return Arrays.stream(CategoryCode.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseThrow(InvalidURIException::new)
                .getDtype();
    }
}
