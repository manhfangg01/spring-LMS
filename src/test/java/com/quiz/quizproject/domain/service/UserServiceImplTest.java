package com.quiz.quizproject.domain.service;

import com.quiz.quizproject.domain.RoleEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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




}
