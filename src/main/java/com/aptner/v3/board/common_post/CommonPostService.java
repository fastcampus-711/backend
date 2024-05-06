package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
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
        List<T> list = commonPostRepository.findByDtype("NoticePost");

        return list;
    }
}
