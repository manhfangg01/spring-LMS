package com.quiz.quizproject.domain.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.quizproject.domain.user.controller.UserController;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import com.quiz.quizproject.domain.user.service.UserService;
import com.quiz.quizproject.service.auth.JwtService;
import com.quiz.quizproject.util.constant.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
// Nó sẽ tạo ra một không gian mini chứa:
// Controller có thể 1 hoặc nhiều
// DispatcherServlet: Điều hướng request đi đúng nơi
// Các Configurations của Security: SecurityFilterChain, WebSecurityConfigurer
// ControllerAdvice: nơi xử lý lỗi = @RestControllerAdvice
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
// Từ SpringBoot 3.4  trở đi thì @MockBean -> @MockitoBean
public class UserControllerTests {
    // Viết theo kiểu Given - When - Then thay vì AAA

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Cung cấp cho thằng jwtFilter
    private JwtService jwtService;

    @MockitoBean // Tên mới, package mới, quyền năng vẫn thế
    // Trên ControllerTest thì phải dùng MockitoBean
    // do WebMvcTest sẽ khởi tạo 1 phần của Spring Context để có thể perform được api ở tầng web
    private UserService userService;

    // ObjectMapper thuộc thư viện Jackson khác với UserMapper.. thuộc thư viện MapStruct
    // UserMapper ->  chuyển đổi entity <-> DTO
    // ObjectMapper -> chuyển đổi entity <-> JSON/String
    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest request;

    private UserResponse response;

    // Given: Giả sử tôi có một request và một response mong đợi
    @BeforeEach
    public void init(){
        request = UserRequest.builder()
                .email("manhphan@gmail.com")
                .password("password123")
                .userName("manhphan")
                .roleName("ADMIN")
                .status(UserStatus.ACTIVE)
                .build();
        response = UserResponse.builder()
                .email(request.email())
                .userName(request.userName())
                .status(request.status())
                .roleName(request.roleName())
                .build();
    }

    @Test
    public void createUser_ShouldReturn201Created_WhenRequestIsValid() throws Exception{
        // thiết lập hành vi của userService.createUser
        given(userService.createUser(any(UserRequest.class))).willReturn(response);

        // WHEN: Khi tôi thực hiện hành động gọi API gửi JSON lên endpoint
        // NHỚ:static post của MockMvcRequestBuilders
        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // THEN: Thì kết quả trả về phải có Status 201 và dữ liệu khớp
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("manhphan@gmail.com"))
                .andExpect(jsonPath("$.userName").value("manhphan"));


        // (Optional) Kiểm tra xem service có thực sự được gọi đúng 1 lần không
        then(userService).should().createUser(any(UserRequest.class));
    }
}
