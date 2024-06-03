package com.aptner.v3.board.common_post;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.dto.SearchDto;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/boards")
public class CommonPostController<E extends CommonPost,
        T extends CommonPostDto,
        Q extends CommonPostDto.CommonPostRequest,
        S extends CommonPostDto.CommonPostResponse> {

    protected final CommonPostService<E, T, Q, S> commonPostService;
    protected final PaginationService paginationService;

    @Autowired
    public CommonPostController(@Qualifier("commonPostService") CommonPostService<E, T, Q, S> commonPostService, PaginationService paginationService) {
        this.commonPostService = commonPostService;
        this.paginationService = paginationService;
    }

    @GetMapping("")
    @Operation(summary = "게시글 별 검색")
    public ApiResponse<?> getPostListByCategoryId(@RequestParam(name = "category-id", defaultValue = "0") Long categoryId,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "status", required = false) Status status,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort
    ) {
        BoardGroup boardGroup = getBoardGroup();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        Page<T> posts = commonPostService.getPostList(boardGroup, categoryId, keyword, status, null, pageable);

        return ResponseUtil.ok(SearchDto.SearchResponse.from(SearchDto.of(
                posts.map(p -> (S) p.toResponse()),
                paginationService.getPaginationBarNumbers(pageable.getPageNumber(), posts.getTotalPages())
        )));
    }

    @GetMapping("/{post-id}")
    @Operation(summary = "게시글 상세")
    public ApiResponse<?> getPost(
            @PathVariable(name = "post-id") Long postId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        T post = commonPostService.getPost(user.toDto().getId(), postId);
        post.setReactionType(commonPostService.getPostReactionType(user.toDto().getId(), postId));
        return ResponseUtil.ok(post.toResponse());
    }

    @PostMapping("/")
    @Operation(summary = "게시글 등록")
    public ApiResponse<?> createPost(
            @RequestBody Q request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        this.logGenericTypes();
        T postDto = (T) request.toDto(
                getBoardGroup(),
                user,
                request
        );

        log.debug("createPost - user :{}", user);
        log.debug("createPost - request :{}", request);
        log.debug("createPost - postDto :{}", postDto);
        T savedDto = commonPostService.createPost(postDto);
        log.debug("createPost - savedDto :{}", savedDto);
        S response = (S) savedDto.toResponse();
        log.debug("createPost - response :{}", response);
        return ResponseUtil.create(response);
    }

    @PutMapping("/{post-id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<?> updatePost(
            @PathVariable(name = "post-id") Long postId,
            @RequestBody Q request,
            @AuthenticationPrincipal CustomUserDetails user) {
        this.logGenericTypes();
        request.setId(postId);
        T postDto = (T) request.toDto(
                getBoardGroup(),
                user,
                request
        );

        log.debug("updatePost - member :{}", user);
        log.debug("updatePost - request :{}", request);
        log.debug("updatePost - postDto :{}", postDto);
        T savedDto = commonPostService.updatePost(postDto);
        log.debug("updatePost - savedDto :{}", savedDto);
        S response = (S) savedDto.toResponse();
        log.debug("updatePost - response :{}", response);
        return ResponseUtil.update(response);
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
        log.debug("deletePost - postDto.getId :{}", postDto.getId());
        long deleted = commonPostService.deletePost(postId, postDto);
        return ResponseUtil.delete(Collections.singletonMap("id", String.valueOf(deleted)));
    }

    public BoardGroup getBoardGroup() {
        return BoardGroup.ALL;
    }

    private void logGenericTypes() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
            log.debug("Generic types: (E)Entity = {}, (T)DTO = {}, (Q)Request = {}, (S)Response = {}",
                    typeArguments[0].getTypeName(),
                    typeArguments[1].getTypeName(),
                    typeArguments[2].getTypeName(),
                    typeArguments[3].getTypeName());
        } else {
            log.debug("No generic type information available.");
        }
    }
}