package com.sevak.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sevak.info.model.User;
import com.sevak.info.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetAllUser() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(new User("arman@mail.ru", "Arman"));
        userList.add(new User("karen@mail.ru", "Karen"));
        userList.add(new User("artur@mail.ru", "Artur"));

        Mockito.when(userRepository.findAll()).thenReturn(userList);
        String url = "/contacts";

        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(userList);
        System.out.println(expectedJsonResponse);
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);

    }
}
