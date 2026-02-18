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
import com.quiz.quizproject.util.exception.handler.AppException;
import com.quiz.quizproject.util.random.RandomHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.times;

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
            Mockito.verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
            Mockito.verify(passwordEncoder, times(1)).encode(request.password());
            Mockito.verify(userMapper, times(1)).toResponse(userEntity);
        }
    }

    // getting all user test

    @Test
    public void getAllUsers_ShouldReturnPageOfResponses_WhenCallWithValidParams(){
        // Arrange
        int pageNumber = 0;
        int pageSize = 10;
        Sort sortBy =Sort.by(Sort.Direction.ASC, "id");
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
        Mockito.verify(userRepository, times(1)).findAll(Mockito.<Specification<UserEntity>>any(), Mockito.eq(pageable));
        Mockito.verify(userMapper, times(2)).toResponse(Mockito.any(UserEntity.class));
    }

    // Updating User Test
    @Test
    public void updateUser_ShouldReturnUpdatedUserResponse_WhenValidIdAndRequestSubmitted(){
         // I. Arrange
        Long givenId = 1L;

        UserEntity dbUser = UserEntity.builder()
                .userName("Phan Van Manh")
                .email("manhphan@gmail.com")
                .password("Initial Hash Password")
                .status(UserStatus.ACTIVE)
                .role(RoleEntity.builder().name("ADMIN").build())
                .build();
        UserRequest request = UserRequest.builder()
                .userName("Manh Phan Van")
                .email("newmanh@gmail.com")
                .password("Updated Password")
                .status(UserStatus.BANNED)
                .roleName("USER")
                .build();

        UserResponse expectedResponse = UserResponse.builder()
                .userName(request.userName())
                .email(request.email())
                .status(request.status())
                .roleName("USER")
                .build();

        // Declare Mockito functions

        // Quy tắc sống còn khi dùng Mockito when:
        // Nếu hàm yêu cầu hơn 1 tham số nếu như 1 cái dùng any() thì bắt buộc
        // cái còn lại nếu là số thường thì phải bọc bằng eq()
        // Tham số 1 dùng any(), tham số 2 để trần trụi -> CHẾT NGAY!
        //Mockito.when(repo.existsByEmailAndIdNot(Mockito.anyString(), 1L)).thenReturn(false);

        Mockito.when(userRepository.findById(givenId)).thenReturn(Optional.of(dbUser));
        Mockito.when(userRepository.existsByEmailAndIdNot(request.email(), givenId)).thenReturn(false);
        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.of(RoleEntity.builder().name("USER").build()));
        Mockito.when(passwordEncoder.encode(request.password())).thenReturn("New Hash Password");
        Mockito.when(userRepository.existsByUserNameAndIdNot(request.userName(), givenId)).thenReturn(false );
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(dbUser);
        Mockito.when(userMapper.toResponse(Mockito.any(UserEntity.class))).thenReturn(expectedResponse);

        // Mock Mapper
        Mockito.doAnswer(invocation -> {
            UserRequest req = invocation.getArgument(0);
            UserEntity entity = invocation.getArgument(1);
            // Simulates copying objects
            entity.setUserName(req.userName());
            entity.setEmail(req.email());
            entity.setStatus(req.status());
            return null;
        }).when(userMapper).updateEntityFromRequest(Mockito.any(UserRequest.class), Mockito.any(UserEntity.class));

        // II. Act
        UserResponse result = userService.updateUser(givenId, request);

        // III. Assert & Verify
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.userName()).isEqualTo(request.userName());
        Assertions.assertThat(result.email()).isEqualTo(request.email());
        Assertions.assertThat(result.status()).isEqualTo(request.status());
        Assertions.assertThat(result.roleName()).isEqualTo("USER");

        Mockito.verify(userRepository, times(1)).findById(givenId);
        Mockito.verify(userRepository, times(1)).existsByEmailAndIdNot(request.email(), givenId);
        Mockito.verify(userRepository, times(1)).existsByUserNameAndIdNot(request.userName(), givenId);
        Mockito.verify(roleRepository, times(1)).findByName("USER");
        Mockito.verify(passwordEncoder, times(1)).encode(request.password());
        Mockito.verify(userMapper, times(1)).updateEntityFromRequest(Mockito.any(UserRequest.class), Mockito.any(UserEntity.class));
        Mockito.verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    public void deleteUser_ShouldCallRepoDeleteMethod_WhenExistedIdSubmitted(){
        // I. Arrange
        Long givenId = 99L;

        // II. Act
        userService.deleteUser(givenId);

        // III. Verify
        // In case of 204 api, check if it calls the method of repo
        Mockito.verify(userRepository, times(1)).deleteById(givenId);
    }

    //================================================
    //--------------------SAD PATH------------------
    //================================================


    // ---------------- CREATE-PATH -------
    @Test
    public void createUser_ThrowEmailAlreadyExistsException_WhenDuplicatedEmailGiven(){
        // I. Arrange
        UserRequest request = UserRequest.builder().email("manhphan@gmail.com").build();

        // Declare
        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        Assertions.assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(AppException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(AppException.class))
                .satisfies(ex ->{
                    Assertions.assertThat(ex.getName()).isEqualTo("ApiException");
                    Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(ex.getMessage()).isEqualTo("Input error");
                    Assertions.assertThat(ex.getError()).isEqualTo("Email already exists");
                });
        // Verify
        Mockito.verify(userRepository, times(1)).existsByEmail(Mockito.any(String.class));
    }

    @Test
    public void createUser_ThrowRoleNotFoundException_whenRoleNameNotExisted(){
        // I. Arrange
        UserRequest request = UserRequest.builder().email("manhphan@gmail.com").roleName("PRODUCER").build();

        // Mock methods
        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(false);
        Mockito.when(roleRepository.findByName(request.roleName())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(AppException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(AppException.class))
                .satisfies(ex ->{
                    Assertions.assertThat(ex.getName()).isEqualTo("ApiException");
                    Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(ex.getMessage()).isEqualTo("Input error");
                    Assertions.assertThat(ex.getError()).isEqualTo("Role not found");
                });
        // Verify
        Mockito.verify(userRepository, times(1)).existsByEmail(Mockito.any(String.class));
        Mockito.verify(roleRepository,times(1)).findByName(Mockito.any(String.class));
    }

    @Test
    public void createUser_ShouldRetryGeneratingUserName_WhenInitialUserNameExists(){

        // I. Arrange
        UserRequest request = UserRequest.builder().email("test@gmail.com").userName("originalName").roleName("ADMIN").password("init pass").build();
        RoleEntity adminRole =  RoleEntity.builder().name("ADMIN").build();
        UserEntity userEntity = UserEntity.builder().userName("originalName").role(adminRole).build();

        // Declare Mock Methods
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        Mockito.when(userMapper.toEntity(Mockito.any(UserRequest.class))).thenReturn(userEntity);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("hashed_password");

        // Main
        Mockito.when(userRepository.existsByUserName(Mockito.anyString()))
                .thenReturn(true)
                .thenReturn(false);

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        userService.createUser(request);
        // Chỉ kiểm tra công tắc không cần phải assert

        // Verify
        Mockito.verify(userRepository, times(2)).existsByUserName(Mockito.anyString());
        Mockito.verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
    }

}





