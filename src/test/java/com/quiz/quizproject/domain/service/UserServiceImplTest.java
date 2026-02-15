package com.quiz.quizproject.domain.service;

import com.quiz.quizproject.domain.RoleEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import com.quiz.quizproject.domain.user.filter.UserFilter;
import com.quiz.quizproject.domain.user.mapper.UserMapper;
import com.quiz.quizproject.domain.user.repository.UserRepository;
import com.quiz.quizproject.domain.user.service.impl.UserServiceImpl;
import com.quiz.quizproject.repository.RoleRepository;
import com.quiz.quizproject.util.constant.UserStatus;
import com.quiz.quizproject.util.random.RandomHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

// Theo quy tắc thì không nên Mock cách static class hay method hãy coi nó là resources

@ExtendWith({MockitoExtension.class})
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    //================================================
    //--------------------HAPPY PATH------------------
    //================================================

    // Creating User Test
    @Test
    public void createUser_shouldReturnSavedUserResponseDto_WhenValidUserRequestSubmitted(){
        // Arrange
        UserRequest request = new UserRequest("manhchill@gmail.com", "123456", "phanvanmanh", "USER", UserStatus.ACTIVE);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("manhchill@gmail.com");
        userEntity.setUserName("phanvanmanh");

        UserResponse expectedResponse = new UserResponse(1L, "manhchill@gmail.com", "Nice Username created randomly", null, "USERNAME", UserStatus.ACTIVE, null, null);


        // Mock Mapper
        Mockito.when(userMapper.toEntity(request)).thenReturn(userEntity);
        Mockito.when(userMapper.toResponse(Mockito.any(UserEntity.class))).thenReturn(expectedResponse);

        // Mock Repo + Encoder
        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(false);

        String generatedName = "Nice Username created randomly";
        Mockito.when(userRepository.existsByUserName(generatedName)).thenReturn(false);

        Mockito.when(passwordEncoder.encode(request.password())).thenReturn("HASH_PASSWORD");


        Mockito.when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(RoleEntity.builder().name("USER").build()));

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        try (MockedStatic<RandomHelper> mockedStatic = Mockito.mockStatic(RandomHelper.class)) {
            mockedStatic.when(() -> RandomHelper.generateUniqueUserName("phanvanmanh"))
                    .thenReturn(generatedName);

            //Trigger Action
            UserResponse result = userService.createUser(request);

            // Assert
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.email()).isEqualTo(request.email());
            Assertions.assertThat(result.userName()).isEqualTo(generatedName);

            // Verify
            Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
            Mockito.verify(passwordEncoder, Mockito.times(1)).encode(request.password());
            Mockito.verify(userMapper, Mockito.times(1)).toResponse(userEntity);
        }
    }

    // getting all user test

    @Test
    public void getAllUsers_ShouldReturnPageOfResponses_WhenCallWithValidParams(){
        // Arrange
        Pageable pageable = PageRequest.of(0,10);

        UserFilter filter = new UserFilter();

        UserEntity user1= new UserEntity();
        user1.setId(1L);
        user1.setUserName("Manh 1");
        UserEntity user2= new UserEntity();
        user2.setId(2L);
        user2.setUserName("Manh 2");

        List<UserEntity> users= List.of(user1, user2);

        Page<UserEntity> expectedUserPage = new PageImpl<>(users, pageable, users.size());

        // Arrange Expected Response
        UserResponse res1 = UserResponse.builder().id(1L).userName("Manh 1").build();
        UserResponse res2 =UserResponse.builder().id(2L).userName("Manh 2").build();

        // Declare Mockito function
        Mockito.when(userRepository.findAll(Mockito.<Specification<UserEntity>>any(), Mockito.eq(pageable))).thenReturn(expectedUserPage);

        // MockMapper
        Mockito.when(userMapper.toResponse(user1)).thenReturn(res1);
        Mockito.when(userMapper.toResponse(user2)).thenReturn(res2);

        // Trigger Action

        Page<UserResponse> result = userService.getAllUsers(pageable, filter);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTotalPages()).isEqualTo(result.getTotalPages());
        Assertions.assertThat(result.getContent().size()).isEqualTo(result.getContent().size());

        // Assert internal elements
        Assertions.assertThat(result.getContent().getFirst().userName()).isEqualTo("Manh 1");
        Assertions.assertThat(result.getContent().getLast().userName()).isEqualTo("Manh 2");

        // verify execution times
        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.<Specification<UserEntity>>any(), Mockito.eq(pageable));
        Mockito.verify(userMapper, Mockito.times(2)).toResponse(Mockito.any(UserEntity.class));
    }

    // Updating User Test
    @Test
    public void updateUser_ShouldReturnUpdatedUserResponse_WhenValidIdAndRequestSubmitted(){
        // ==========================================
        // 1. ARRANGE
        // ==========================================
        Long givenId = 1L;

        UserEntity initialUser = new UserEntity();
        initialUser.setId(givenId);
        initialUser.setUserName("Phan Van Manh");
        initialUser.setEmail("phanvanmanh@gmail.com");
        initialUser.setStatus(UserStatus.ACTIVE);
        initialUser.setRole(RoleEntity.builder().name("USER").build());
        initialUser.setPassword("Initial hash password");

        UserRequest request = UserRequest.builder()
                .userName("Manh Van Phan")
                .email("manhchan@gmail.com")
                .status(UserStatus.BANNED)
                .roleName("ADMIN")
                .password("123456")
                .build();

        UserResponse expectedResponse = UserResponse.builder()
                .userName(request.userName())
                .email(request.email())
                .status(request.status())
                .build();

        // ==========================================
        // MOCK: Repository & Encoder
        // ==========================================
        Mockito.when(userRepository.findById(Mockito.eq(givenId)))
                .thenReturn(Optional.of(initialUser));

        Mockito.when(userRepository.existsByEmailAndIdNot(Mockito.eq(request.email()), Mockito.eq(givenId)))
                .thenReturn(false);

        Mockito.when(roleRepository.findByName(Mockito.eq("ADMIN")))
                .thenReturn(Optional.of(RoleEntity.builder().name("ADMIN").build())); // Fix lỗi đánh máy chữ ADMIn

        // FIX 2: Mock đúng mật khẩu mới được truyền vào
        Mockito.when(passwordEncoder.encode(request.password()))
                .thenReturn("Updated hash password");

        // FIX 1: Thêm thenReturn(false)
        Mockito.when(userRepository.existsByUserNameAndIdNot(request.userName(), givenId))
                .thenReturn(false);

        // FIX 3: Phải mock hàm save, cho nó trả về chính cái initialUser (đã được mapper cập nhật data)
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(initialUser);

        // ==========================================
        // MOCK: Mapper
        // ==========================================
        Mockito.doAnswer(invocation -> {
            UserRequest req = invocation.getArgument(0);
            UserEntity user = invocation.getArgument(1);

            // Simulate Mapping Action
            user.setUserName(req.userName());
            user.setEmail(req.email());
            user.setStatus(req.status());
            user.setPassword(req.password());
            user.setRole(RoleEntity.builder().name(req.roleName()).build());

            return null;
        }).when(userMapper).updateEntityFromRequest(Mockito.any(UserRequest.class), Mockito.any(UserEntity.class));

        Mockito.when(userMapper.toResponse(Mockito.any(UserEntity.class)))
                .thenReturn(expectedResponse);

        // ==========================================
        // 2. ACT
        // ==========================================
        UserResponse result = userService.updateUser(givenId, request);

        // ==========================================
        // 3. ASSERT & VERIFY
        // ==========================================
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.userName()).isEqualTo(expectedResponse.userName());
        Assertions.assertThat(result.email()).isEqualTo(expectedResponse.email());
        Assertions.assertThat(result.status()).isEqualTo(expectedResponse.status()); // Đã xóa dòng assert userName bị lặp

        // Verify xem có gọi đủ các hàm không
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.eq(givenId));
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmailAndIdNot(Mockito.any(String.class), Mockito.eq(givenId));
        Mockito.verify(userRepository, Mockito.times(1)).existsByUserNameAndIdNot(Mockito.any(String.class), Mockito.eq(givenId));
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(Mockito.eq("ADMIN"));
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(request.password()); // FIX: Verify đúng tham số
        Mockito.verify(userMapper, Mockito.times(1)).updateEntityFromRequest(Mockito.any(UserRequest.class), Mockito.any(UserEntity.class));
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
        Mockito.verify(userMapper, Mockito.times(1)).toResponse(Mockito.any(UserEntity.class));
    }

    @Test
    public void deleteUser_ShouldCallRepositoryDeleteById_WhenIdIsProvided() {
        Long givenId = 1L;

        userService.deleteUser(givenId);

        // ==========================================
        // 3. VERIFY (Vũ khí duy nhất để test hàm void)
        // ==========================================
        // Đảm bảo rằng hàm deleteById của Repository đã được gọi ĐÚNG 1 LẦN với đúng cái ID đó.
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.eq(givenId));
    }
}
