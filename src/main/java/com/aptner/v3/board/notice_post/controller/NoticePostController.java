package com.aptner.v3.board.notice_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="공지 사항")
@RequestMapping("/boards/notices")
public class NoticePostController extends CommonPostController<
        NoticePost,
        NoticePostDto,
        NoticePostDto.NoticeRequest,
        NoticePostDto.NoticeResponse> {
    public NoticePostController(CategoryRepository categoryRepository, CommonPostService<NoticePost, NoticePostDto, NoticePostDto.NoticeRequest, NoticePostDto.NoticeResponse> commonPostService, PaginationService paginationService) {
        super(categoryRepository, commonPostService, paginationService);
    }

    public BoardGroup getBoardGroup() {
        return BoardGroup.NOTICES;
    }
}
