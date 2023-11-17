package pl.empik.rest.userrestservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.empik.rest.userrestservice.dto.UserResponseDto;
import pl.empik.rest.userrestservice.service.UserService;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;

    @Test
    void getUserByLogin() throws Exception {
        Optional<UserResponseDto> userResponseDto = Optional.of(UserResponseDto.builder().login("roland").build());
        given(userService.getUserByLogin("roland")).willReturn(userResponseDto);

        mockMvc.perform(get("/users/{user}","roland")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
