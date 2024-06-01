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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        prefix = "http://localhost:" + port;
    }

    @WithUserDetails(value="user1")
    @Test
    void 공지사항_전체_조회() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(prefix + "/boards/notices"))
                .andDo(print())
                .andReturn();
        List<String> list = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data.posts.content.[*].board_group");
        Assertions.assertThat(list).hasSize(10);

//        for (String str : list) {
//            Assertions.assertThat(str).contains(BoardGroup.NOTICES.name());
//        }
    }

    @WithUserDetails(value="user")
    @Test
    void 공지사항_게시글_생성() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test");
        jsonObject.put("content", "Spring Boot Test");
        jsonObject.put("category_id", 1);
        jsonObject.put("visible", true);

        MvcResult mvcResult = mockMvc.perform(
                        post(prefix + "/boards/notices/")
                                .content(jsonObject.toJSONString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                ).andDo(print())
                .andReturn();

        DocumentContext parsed = JsonPath.parse(mvcResult.getResponse().getContentAsString());

        assertEquals("Spring Boot Test", parsed.read("$.data.title"));
        assertEquals("Spring Boot Test", parsed.read("$.data.content"));
        assertTrue(parsed.read("$.data.category_name") != null);
    }

    @WithUserDetails(value="user1")
    @Test
    void 공지사항_게시글_삭제() throws Exception {
        long postId = postUtil.makeNoticePostAndReturnId();

        mockMvc.perform(
                        delete(prefix + "/boards/notices/" + postId)
                ).andDo(print())
                .andExpect(jsonPath("$.data.id").value(postId));
    }

}
