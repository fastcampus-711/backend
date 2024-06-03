package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.qna.Qna;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.aptner.v3.global.error.ErrorCode.INSUFFICIENT_AUTHORITY;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CommonPostControllerTest {
    @LocalServerPort
    private int port;

    private String prefix;

    @Autowired
    PostUtil postUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private CommonPostService<CommonPost, CommonPostDto, CommonPostDto.CommonPostRequest, CommonPostDto.CommonPostResponse> commonPostService;

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
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 한글 깨짐 필터 추가
                .alwaysDo(print())
                .build();

        prefix = "http://localhost:" + port;
    }

    @WithUserDetails(value = "user1")
    @Test
    void 게시판_전체_조회() throws Exception {
        mockMvc.perform(get(prefix + "/boards"))
                .andDo(print())
                .andExpect(jsonPath("$.data.posts.content.length()").value(10));
    }

//    @WithUserDetails(value = "user1")
    @Test
    @Disabled("검색 개선")
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

    @WithUserDetails(value = "user1")
    @Test
    void 타인의_게시글_수정시_error() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);

        mockMvc.perform(
                        put(prefix + "/boards/notices/" + 1)
                                .content(jsonObject.toJSONString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(INSUFFICIENT_AUTHORITY.getDetail()))
        ;
    }

    @WithUserDetails(value = "user1")
    @Test
    void 타인의_게시글_삭제시_error() throws Exception {

        mockMvc.perform(
                        delete(prefix + "/boards/notices/" + 1)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(INSUFFICIENT_AUTHORITY.getDetail()))
        ;
    }

    @WithUserDetails(value = "user1")
    @Test
    void 수정_시_URI와_post_id가_다른_게시판일_때_error() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Spring Boot Test updated");
        jsonObject.put("content", "Spring Boot Test updated");
        jsonObject.put("category_id", 2);

        mockMvc.perform(
                        put(prefix + "/boards/notices/" + 1)
                                .content(jsonObject.toJSONString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(INSUFFICIENT_AUTHORITY.getDetail()))
        ;
    }

    @WithUserDetails(value = "user1")
    @Test
    void 삭제_시_URI와_post_id가_다른_게시판일_때_error() throws Exception {
        //@Warning dummy data : 32 -> Notice
        mockMvc.perform(
                        delete(prefix + "/boards/notices/" + 32)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(INSUFFICIENT_AUTHORITY.getDetail()))
        ;
    }

    static Stream<Class<? extends CommonPost>> postProvider() {
        return Stream.of(CommonPost.class, NoticePost.class, Market.class);
    }

    @Transactional
    @DisplayName("Test Save and Find By Id with Various Post Types")
    @ParameterizedTest(name = "{index} => postClass={0}")
    @MethodSource("postProvider")
    <T extends CommonPost> void testSaveAndFindById(Class<T> targetClass) throws Exception {
        T commonPost = postUtil.makeCommonPost(targetClass,
                targetClass.getSimpleName() + " Title",
                targetClass.getSimpleName() + " Content",
                1L);
        commonPostRepository.save(commonPost);

        Optional<? extends CommonPost> foundedPost = commonPostRepository.findById(commonPost.getId());
        assertTrue(foundedPost.isPresent());
        org.assertj.core.api.Assertions.assertThat(commonPost).usingRecursiveComparison().isEqualTo(foundedPost.get());
    }

    @DisplayName("검색 조건 테스트")
    @Test
    void testFindByDtype() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(SortType.RECENT.getColumnName()).descending());

        // when
        postUtil.makeComplainPostAndReturnId();
        postUtil.makeMarketPostAndReturnId();

        // then
        List<?> foundPosts = complainPostRepository.findByDtype(BoardGroup.COMPLAINT.getTable(), pageable).getContent();
        assertFalse(foundPosts.isEmpty());

        foundPosts = marketPostRepository.findByDtype(BoardGroup.MARKETS.getTable(), pageable).getContent();
        assertFalse(foundPosts.isEmpty());
    }

}