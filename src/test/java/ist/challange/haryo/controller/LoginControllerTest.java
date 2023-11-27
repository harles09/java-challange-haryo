package ist.challange.haryo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ist.challange.haryo.model.Role;
import ist.challange.haryo.model.User;
import ist.challange.haryo.repository.RoleRepository;
import ist.challange.haryo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    public MockMvc mockMvc;
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;
    @Test
    void authenticateUser()throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");

        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(user, null));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Sukses Login"));
    }

    @Test
    void registerUser() throws Exception{
        Role role = new Role();
        role.setId(1L);
        role.setName("admin");
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration has successfully!"));
    }

    @Test
    void logoutUser() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");

        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(user, null));

        try {
            mockMvc.perform(post("/api/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Sukses Login"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}