package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FreePostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonPostService<CommonPost, CommonPostDto, CommonPostDto.CommonPostRequest, CommonPostDto.CommonPostResponse> commonPostService;

    @Autowired
    private CommonPostRepository<CommonPost> commonPostRepository;

    @Autowired
    private CommonPostRepository<FreePost> freePostRepository;


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
    void 자유_게시판_전체_조회() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(prefix + "/boards/frees"))
                .andDo(print())
                .andReturn();
        List<String> list = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data.posts.content.[*].dtype");
        Assertions.assertThat(list).hasSize(10);

        for (String str : list) {
            Assertions.assertThat(str).contains("FreePost");
        }
    }

    @Test
    void 자유게시판_게시글_생성() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test");
        jsonObject.put("content", "Spring Boot Test");
        jsonObject.put("category_id", 1);
        jsonObject.put("blind_by", "someone");
        jsonObject.put("blind_at", "2024-05-12T17:00:12");

        MvcResult mvcResult = mockMvc.perform(
                        post(prefix + "/boards/frees/")
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
    void 자유게시판_게시글_수정() throws Exception {
        long postId = postUtil.makeFreePostAndReturnId();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);
        jsonObject.put("blind_by", "someone else");
        jsonObject.put("blind_at", "2024-05-23T17:00:12");

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
    void 자유게시판_게시글_삭제() throws Exception {
        long postId = postUtil.makeFreePostAndReturnId();
        System.out.println(postId);

        mockMvc.perform(
                        delete(prefix + "/boards/frees/" + postId)
                ).andDo(print())
                .andExpect(jsonPath("$.data").value(postId));
    }

}
