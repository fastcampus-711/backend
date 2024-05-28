package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NoticePostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonPostService<CommonPost, CommonPostDto, CommonPostDto.CommonPostRequest, CommonPostDto.CommonPostResponse> commonPostService;

    @Autowired
    private CommonPostRepository<CommonPost> commonPostRepository;

    @Autowired
    private CommonPostRepository<NoticePost> noticePostRepository;


    @LocalServerPort
    private int port;

    @Autowired
    WebApplicationContext webApplicationContext;

    private String prefix;

    @Autowired
    PostUtil postUtil;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        prefix = "http://localhost:" + port;
    }

    @Test
    void 공지사항_전체_조회() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(prefix + "/boards/notices"))
                .andDo(print())
                .andReturn();
        List<String> list = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data.posts.content.[*].dtype");
        Assertions.assertThat(list).hasSize(10);

        for (String str : list) {
            Assertions.assertThat(str).contains("NoticePost");
        }
    }

    @WithMockUser(username = "user", password = "p@ssword", roles = "USER")
    @Test
    void 공지사항_게시글_생성() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test");
        jsonObject.put("content", "Spring Boot Test");
        jsonObject.put("category_id", 1);
        jsonObject.put("post_at", "2024-05-12T17:00:12");
        CommonPostDto.CommonPostRequest request = postUtil.makeCommonPostRequest(NoticePost.class,
                "Spring Boot Test",
                "Content",
                1L
        );


        MvcResult mvcResult = mockMvc.perform(
                        post(prefix + "/boards/notices/")
                                .content(jsonObject.toJSONString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(print())
                .andReturn();

        DocumentContext parsed = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        for (String key : jsonObject.keySet()) {
            assertEquals(
                    jsonObject.get(key),
                    parsed.read("$.data." + key)
            );
        }
    }

    @Test
    void 자유게시판_게시글_수정() throws Exception {
        long postId = postUtil.makeNoticePostAndReturnId();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);

        MvcResult mvcResult = mockMvc.perform(
                        put(prefix + "/boards/frees/" + postId)
                                .content(jsonObject.toJSONString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andReturn();

        for (String key : jsonObject.keySet()) {
            assertEquals(
                    jsonObject.get(key),
                    JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data." + key)
            );
        }
    }

    @Test
    void 공지사항_게시글_삭제() throws Exception {
        long postId = postUtil.makeNoticePostAndReturnId();

        mockMvc.perform(
                        delete(prefix + "/boards/notices/" + postId)
                ).andDo(print())
                .andExpect(jsonPath("$.data").value(postId));
    }

}
