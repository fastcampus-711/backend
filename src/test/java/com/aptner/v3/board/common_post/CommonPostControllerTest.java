package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.qna.Qna;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.ServletException;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class CommonPostControllerTest {
    @LocalServerPort
    private int port;

    private String prefix;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private CommonPostService<CommonPost, CommonPostDto.Request, CommonPostDto.Response> commonPostService;

    @Autowired
    private CommonPostRepository<CommonPost> commonPostRepository;

    @Autowired
    private CommonPostRepository<Complain> complainPostRepository;

    @Autowired
    private CommonPostRepository<FreePost> freePostRepository;

    @Autowired
    private CommonPostRepository<Market> marketPostRepository;

    @Autowired
    private CommonPostRepository<NoticePost> noticePostRepository;

    @Autowired
    private CommonPostRepository<Qna> qnaPostRepository;


    private SizeValidatorForArray sizeValidatorForArray = new SizeValidatorForArray();


    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        prefix = "http://localhost:" + port;
    }

    long makeCommonPostRequestDtoAndReturnId() throws Exception {
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(CommonPost.class,
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

    long makeNoticePostAndReturnId() throws Exception {
        CommonPostDto.Request requestDto = makeCommonPostRequestDto(NoticePost.class,
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


    @Test
    void 게시판_전체_조회() throws Exception {
        mockMvc.perform(get(prefix + "/boards"))
                .andDo(print())
                .andExpect(jsonPath("$.data.length()").value(10));
    }

    @Test
    void 자유_게시판_전체_조회() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(prefix + "/boards/frees"))
                .andDo(print())
                .andReturn();
        List<String> list = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data[*].dtype");
        Assertions.assertThat(list).hasSize(10);

        for (String str : list) {
            Assertions.assertThat(str).contains("FreePost");
        }
    }

    @Test
    void 공지사항_전체_조회() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(prefix + "/boards/notices"))
                .andDo(print())
                .andReturn();
        List<String> list = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data[*].dtype");
        Assertions.assertThat(list).hasSize(10);

        for (String str : list) {
            Assertions.assertThat(str).contains("NoticePost");
        }
    }

    @Test
    void 게시판_통합_검색() throws Exception {
        String keyword = "a";
        MvcResult mvcResult = mockMvc.perform(get(prefix + "/boards?keyword=" + keyword))
                .andDo(print())
                .andReturn();
        mvcResult.getResponse().getContentAsString();
        List<String> list = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data[?(@.title =~ /.*" + keyword + ".*/)].title");
        for (String str : list) {
            Assertions.assertThat(str).contains(keyword);
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
    void 공지사항_게시글_생성() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test");
        jsonObject.put("content", "Spring Boot Test");
        jsonObject.put("category_id", 1);
        jsonObject.put("post_at", "2024-05-12T17:00:12");

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
        long postId = makeFreePostAndReturnId();

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
    void 공지사항_게시글_수정() throws Exception {
        long postId = makeNoticePostAndReturnId();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);
        jsonObject.put("post_at", "2024-05-23T17:00:12");

        MvcResult mvcResult = mockMvc.perform(
                        put(prefix + "/boards/notices/" + postId)
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

    //ServletException -> PostException으로 수정 필요. GlobalException issue
    @Test
    void 타인의_게시글_수정시_error() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);
        jsonObject.put("post_at", "2024-05-23T17:00:12");

        assertThrows(ServletException.class, () -> mockMvc.perform(
                put(prefix + "/boards/notices/" + 1)
                        .content(jsonObject.toJSONString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ));
    }

    @Test
    void 자유게시판_게시글_삭제() throws Exception {
        long postId = makeFreePostAndReturnId();

        mockMvc.perform(
                        delete(prefix + "/boards/frees/" + postId)
                ).andDo(print())
                .andExpect(jsonPath("$.data").value(postId));
    }

    @Test
    void 공지사항_게시글_삭제() throws Exception {
        long postId = makeNoticePostAndReturnId();

        mockMvc.perform(
                        delete(prefix + "/boards/notices/" + postId)
                ).andDo(print())
                .andExpect(jsonPath("$.data").value(postId));
    }

    //ServletException -> PostException으로 수정 필요. GlobalException issue
    @Test
    void 타인의_게시글_삭제시_error() throws Exception {
        assertThrows(ServletException.class, () -> mockMvc.perform(delete(prefix + "/boards/notices/1")));
    }

    static Stream<Class<? extends CommonPost>> postProvider() {
        return Stream.of(CommonPost.class, NoticePost.class, Market.class);
    }

    @Test
    void 수정_시_URI와_post_id가_다른_게시판일_때_error() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);
        jsonObject.put("post_at", "2024-05-23T17:00:12");

        assertThrows(ServletException.class, () -> mockMvc.perform(
                put(prefix + "/boards/notices/" + 1)
                        .content(jsonObject.toJSONString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ));
    }

    @Test
    void 삭제_시_URI와_post_id가_다른_게시판일_때_error() throws Exception {
        assertThrows(ServletException.class, () -> mockMvc.perform(
                delete(prefix + "/boards/notices/" + 1)
        ));
    }

    @Transactional
    @DisplayName("Test Save and Find By Id with Various Post Types")
    @ParameterizedTest(name = "{index} => postClass={0}")
    @MethodSource("postProvider")
    <T extends CommonPost> void testSaveAndFindById(Class<T> targetClass) throws Exception {
        T commonPost = makeCommonPost(targetClass,
                targetClass.getSimpleName() + " Title",
                targetClass.getSimpleName() + " Content",
                1L);
        commonPostRepository.save(commonPost);

        Optional<? extends CommonPost> foundedPost = commonPostRepository.findById(commonPost.getId());
        assertTrue(foundedPost.isPresent());
        org.assertj.core.api.Assertions.assertThat(commonPost).usingRecursiveComparison().isEqualTo(foundedPost.get());
    }

    @Test
    void testFindByDtype() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(SortType.RECENT.getColumnName()).descending());
        //        List<?> foundPosts = commonPostRepository.findByDtype("CommonPost");
//        assertFalse(foundPosts.isEmpty());

        makeComplainPostAndReturnId();
        makeMarketPostAndReturnId();
        List<?> foundPosts = complainPostRepository.findByDtype("Complain", pageable).getContent();
        assertFalse(foundPosts.isEmpty());

        foundPosts = marketPostRepository.findByDtype("Market", pageable).getContent();
        assertFalse(foundPosts.isEmpty());
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

    <E extends CommonPost> E makeCommonPost(Class<E> targetClass, String title, String content, long categoryId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("category_id", categoryId);

        return objectMapper.readValue(jsonObject.toJSONString(), targetClass);
    }
}