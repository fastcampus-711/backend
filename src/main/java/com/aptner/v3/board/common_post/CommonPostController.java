package com.aptner.v3.board.common_post;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.SearchDto;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class CommonPostController<E extends CommonPost,
        T extends CommonPostDto,
        Q extends CommonPostDto.CommonPostRequest,
        S extends CommonPostDto.CommonPostResponse> {

    private final CategoryRepository categoryRepository;

    protected final CommonPostService<E, T, Q, S> commonPostService;
    protected final PaginationService paginationService;

    @GetMapping("")
    @Operation(summary = "게시글 검색")
    public ApiResponse<?> getPostListByCategoryId(@RequestParam(name = "category-id", defaultValue = "0") Long categoryId,
                                                  @RequestParam(name = "withPopular", required = false) Boolean withPopular,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort
    ) {

        BoardGroup boardGroup = getBoardGroup();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        Page<T> posts = null;
        if (keyword != null) {
            log.debug("keyword search : boardGroup: {}, categoryId: {}, keyword: {}, limit: {}, page: {}, sort: {}", boardGroup, categoryId, keyword, limit, page, sort);
            posts = commonPostService.getPostListByCategoryIdAndTitle(boardGroup, categoryId, keyword, pageable);
        } else {
            log.debug("category search : boardGroup: {},  categoryId: {}, limit: {}, page: {}, sort: {}", boardGroup, categoryId, limit, page, sort);
            posts = commonPostService.getPostListByCategoryId(boardGroup, categoryId, pageable);
        }

        return ResponseUtil.ok(SearchDto.SearchResponse.from(SearchDto.of(
                posts.map(p -> (S) S.from(p)),
                paginationService.getPaginationBarNumbers(pageable.getPageNumber(), posts.getTotalPages())
        )));
    }

    @GetMapping("/{post-id}")
    @Operation(summary = "게시글 상세")
    public ApiResponse<?> getPost(@PathVariable(name = "post-id") Long postId) {
        return ResponseUtil.ok(commonPostService.getPost(postId));
    }

    @PostMapping("/")
    @Operation(summary = "게시글 등록")
    public ApiResponse<?> createPost(
            @RequestBody Q request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        T postDto = (T) T.of(
                getBoardGroup(),
                user.toDto(),
                request
        );
        log.debug("createPost - user :{}", user);
        log.debug("createPost - request :{}", request);
        log.debug("createPost - postDto :{}", postDto);
        return ResponseUtil.create(S.from(commonPostService.createPost(postDto)));
    }

    @PutMapping("/{post-id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<?> updatePost(
            @PathVariable(name = "post-id") Long postId,
            @RequestBody Q request,
            @AuthenticationPrincipal CustomUserDetails user) {

        request.setId(postId);
        T postDto = (T) T.of(
                getBoardGroup(),
                user.toDto(),
                request
        );
        log.debug("updatePost - member :{}", user);
        log.debug("updatePost - request :{}", request);
        log.debug("updatePost - postDto :{}", postDto);
        return ResponseUtil.update(S.from(commonPostService.updatePost(postDto)));
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<?> deletePost(
            @PathVariable(name = "post-id") long postId,
            @AuthenticationPrincipal CustomUserDetails user) {

        T postDto = (T) T.of(
                getBoardGroup(),
                user.toDto(),
                CommonPostDto.CommonPostRequest.of(postId, null)
        );
        log.debug("deletePost - user :{}", user);
        log.debug("deletePost - postDto :{}", postDto);
        long deleted = commonPostService.deletePost(postId, postDto);
        return ResponseUtil.delete(deleted);
    }

    public BoardGroup getBoardGroup() {
        return BoardGroup.ALL;
    }
}