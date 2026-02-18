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
        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.<Specification<UserEntity>>any(), Mockito.eq(pageable));
        Mockito.verify(userMapper, Mockito.times(2)).toResponse(Mockito.any(UserEntity.class));
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

        Mockito.verify(userRepository, Mockito.times(1)).findById(givenId);
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmailAndIdNot(request.email(), givenId);
        Mockito.verify(userRepository, Mockito.times(1)).existsByUserNameAndIdNot(request.userName(), givenId);
        Mockito.verify(roleRepository, Mockito.times(1)).findByName("USER");
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(request.password());
        Mockito.verify(userMapper, Mockito.times(1)).updateEntityFromRequest(Mockito.any(UserRequest.class), Mockito.any(UserEntity.class));
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    public void deleteUser_ShouldCallRepoDeleteMethod_WhenExistedIdSubmitted(){
        // I. Arrange
        Long givenId = 99L;

        // II. Act
        userService.deleteUser(givenId);

        // III. Verify
        // In case of 204 api, check if it calls the method of repo
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(givenId);
    }


}
