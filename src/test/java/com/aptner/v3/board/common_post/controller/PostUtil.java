package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.qna.Qna;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PostUtil {

    @Autowired
    private CommonPostService<CommonPost, CommonPostDto, CommonPostDto.Request, CommonPostDto.Response> commonPostService;

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

    <E extends CommonPost, T extends CommonPostDto.Request> T makeCommonPostRequestDto(Class<E> targetClass, String title, String content, long categoryId) throws Exception {
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
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(CommonPost.class,
                "Spring Boot Test",
                "Content",
                1);
        return commonPostService.createPost(requestDto).getId();
    }

    long makeNoticePostAndReturnId() throws Exception {
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(NoticePost.class,
                "Spring Boot Test",
                "Content",
                1);
        return commonPostService.createPost(requestDto).getId();
    }

    long makeFreePostAndReturnId() throws Exception {
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(FreePost.class,
                "Spring Boot Test",
                "Content",
                1);
        return commonPostService.createPost(requestDto).getId();
    }

    long makeMarketPostAndReturnId() throws Exception {
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(Market.class,
                "Spring Boot Test",
                "Content",
                1);
        return commonPostService.createPost(requestDto).getId();
    }

    long makeComplainPostAndReturnId() throws Exception {
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(Complain.class,
                "Spring Boot Test",
                "Content",
                1);
        return commonPostService.createPost(requestDto).getId();
    }
}
