package com.aptner.v3.global;

import com.aptner.v3.CommunityApplication;
import com.aptner.v3.auth.controller.AuthController;
import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.service.AuthService;
import com.aptner.v3.global.config.SecurityConfig;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@ContextConfiguration(classes = {CommunityApplication.class})
public class ApiResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("POST /auth/token 200 성공 테스트")
    public void login_success() throws Exception {
        // given
        LoginDto loginDto = LoginDto.builder()
                .username("user")
                .password("p@ssword")
                .build();

        when(authService.login(any(LoginDto.class)))
                .thenReturn(any(TokenDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("POST /auth/refresh 201 생성 테스트")
    public void reissue_success() throws Exception {
        // given
        String validToken = JwtUtil.BEARER_PREFIX + "validAccessToken";

        // when
        when(authService.reissue(anyString()))
                .thenReturn(any(TokenDto.class));
        ResultActions resultActions = mockMvc.perform(
                post("/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, validToken)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    @DisplayName("POST /auth/refresh 400 Exception 발생 테스트")
    public void reissue_exception() throws Exception {
        // given
        String invalid = "invalidToken";

        ResultActions resultActions = mockMvc.perform(
                post("/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, invalid)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_REQUEST.getDetail()));
    }

    @Test
    @DisplayName("POST /auth/revoke 200 성공 테스트")
    public void logout_success() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/auth/revoke"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

}
