package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.board.qna.Qna;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
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
        JSONObject memberObject = new JSONObject();
        memberObject.put("id", 1L);
        Member member = objectMapper.readValue(memberObject.toJSONString(), Member.class);

        JSONObject categoryObject = new JSONObject();
        categoryObject.put("id", categoryId);
        Category category = objectMapper.readValue(categoryObject.toJSONString(), Category.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("category", category);
        jsonObject.put("member", member);
        jsonObject.put("visible", true);

        return objectMapper.readValue(jsonObject.toJSONString(), targetClass);
    }

    <E extends CommonPost, T extends CommonPostDto.CommonPostRequest> T makeCommonPostRequest(Class<E> targetClass, String title, String content, long categoryId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("category_id", categoryId);
        jsonObject.put("visible", true);

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
        NoticePostDto.NoticeRequest commonPostRequestDto = makeCommonPostRequest(NoticePost.class,
                "Spring Boot Test",
                "Content",
                1);
        NoticePostDto dto = NoticePostDto.of(
                BoardGroup.NOTICES,
                createMember(),
                commonPostRequestDto
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeFreePostAndReturnId() throws Exception {
        FreePostDto.FreePostRequest request = makeCommonPostRequest(FreePost.class,
                "Spring Boot Test",
                "Content",
                1);
        FreePostDto dto = FreePostDto.of(
                BoardGroup.FREES,
                createMember(),
                request
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeMarketPostAndReturnId() throws Exception {

        MarketDto.MarketRequest requst = makeCommonPostRequest(Market.class,
                "Spring Boot Test",
                "Content",
                1);

        MarketDto dto = MarketDto.of(
                BoardGroup.MARKETS,
                createMember(),
                requst
        );
        return commonPostService.createPost(dto).getId();
    }

    long makeComplainPostAndReturnId() throws Exception {
        ComplainDto.ComplainRequest request = makeCommonPostRequest(Complain.class,
                "Spring Boot Test",
                "Content",
                1);
        log.debug("makeComplainPost.request : {}", request);
        ComplainDto dto = ComplainDto.of(
                BoardGroup.COMPLAINT,
                createMember(),
                request
        );
        log.debug("makeComplainPost.dto : {}", dto);
        return commonPostService.createPost(dto).getId();
    }

    MemberDto createMember() {
        return MemberDto.of(1L, "user1", "p@ssword", "nickname1", null, null, List.of(MemberRole.ROLE_USER));
    }
}
