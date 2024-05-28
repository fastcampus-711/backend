package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.qna.Qna;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PostUtil {

    @Autowired
    private CommonPostService<CommonPost, CommonPostDto, CommonPostDto.CommonPostRequest, CommonPostDto.CommonPostResponse> commonPostService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonPostRepository<CommonPost> commonPostRepository;
    @Autowired
    private CommonPostRepository<FreePost> freePostRepository;
    @Autowired
    private CommonPostRepository<Complain> complainPostRepository;
    @Autowired
    private CommonPostRepository<Market> marketPostRepository;
    @Autowired
    private CommonPostRepository<NoticePost> noticePostRepository;
    @Autowired
    private CommonPostRepository<Qna> qnaPostRepository;

    <E extends CommonPost> E makeCommonPost(Class<E> targetClass, String title, String content, long categoryId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("category_id", categoryId);

        return objectMapper.readValue(jsonObject.toJSONString(), targetClass);
    }

    <E extends CommonPost, T extends CommonPostDto.CommonPostRequest> T makeCommonPostRequest(Class<E> targetClass, String title, String content, long categoryId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("category_id", categoryId);

        Class<?> requestDtoClass = Arrays.stream(CategoryCode.values())
                .filter(s -> s.getDomain().getSimpleName().equals(targetClass.getSimpleName()))
                .findFirst()
                .orElseThrow()
                .getDtoForRequest();

        return (T) objectMapper.readValue(jsonObject.toJSONString(), requestDtoClass);
    }

    long makeCommonPostRequestDtoAndReturnId() throws Exception {
        CommonPostDto.CommonPostRequest commonPostRequest = makeCommonPostRequest(CommonPost.class,
                "Spring Boot Test",
                "Content",
                1);
        CommonPostDto dto = CommonPostDto.of(
                BoardGroup.ALL,
                createMember(),
                commonPostRequest
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeNoticePostAndReturnId() throws Exception {
        CommonPostDto.CommonPostRequest commonPostRequestDto = makeCommonPostRequest(NoticePost.class,
                "Spring Boot Test",
                "Content",
                1);
        CommonPostDto dto = CommonPostDto.of(
                BoardGroup.NOTICES,
                createMember(),
                commonPostRequestDto
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeFreePostAndReturnId() throws Exception {
        CommonPostDto.CommonPostRequest commonPostRequestDto = makeCommonPostRequest(FreePost.class,
                "Spring Boot Test",
                "Content",
                1);
        CommonPostDto dto = CommonPostDto.of(
                BoardGroup.FREES,
                createMember(),
                commonPostRequestDto
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeMarketPostAndReturnId() throws Exception {

        CommonPostDto.CommonPostRequest commonPostRequestDto = makeCommonPostRequest(Market.class,
                "Spring Boot Test",
                "Content",
                1);

        CommonPostDto dto = CommonPostDto.of(
                BoardGroup.MARKETS,
                createMember(),
                commonPostRequestDto
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeComplainPostAndReturnId() throws Exception {
        CommonPostDto.CommonPostRequest commonPostRequestDto = makeCommonPostRequest(Complain.class,
                "Spring Boot Test",
                "Content",
                1);
        CommonPostDto dto = CommonPostDto.of(
                BoardGroup.COMPLAINT,
                createMember(),
                commonPostRequestDto
        );
        return commonPostService.createPost(dto).getId();
    }

    MemberDto createMember() {
        return MemberDto.of(null, "user", "p@ssword", "nickname", null, null, List.of(MemberRole.ROLE_USER));
    }
}
