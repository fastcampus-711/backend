package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidURIException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonPostService<T extends CommonPost> {
    private final CommonPostRepository<T> commonPostRepository;

    public List<T> getPostList(HttpServletRequest request) {
        String target = Arrays.stream(request.getRequestURI()
                .split("/")).toList().get(5);

        CategoryName categoryName = Arrays.stream(CategoryName.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseThrow(InvalidURIException::new);

        return commonPostRepository.findByDtype(categoryName.getDtype());
    }
}
