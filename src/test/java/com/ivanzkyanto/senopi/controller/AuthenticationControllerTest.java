package com.ivanzkyanto.senopi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.LoginResponse;
import com.ivanzkyanto.senopi.repository.AuthenticationRepository;
import com.ivanzkyanto.senopi.repository.UserRepository;
import com.ivanzkyanto.senopi.security.BCrypt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private void cleanUpDatabase() {
        userRepository.deleteAll();
        authenticationRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        cleanUpDatabase();

        User user = User.builder()
                .username("user")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("User")
                .build();

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        cleanUpDatabase();
    }

    @Test
    void loginSuccess() throws Exception {
        String json = """
                {
                    "username": "user",
                    "password": "password"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Authentication berhasil ditambahkan"),
                MockMvcResultMatchers.jsonPath("data").exists(),
                MockMvcResultMatchers.jsonPath("data.accessToken").exists(),
                MockMvcResultMatchers.jsonPath("data.refreshToken").exists()
        ).andDo(result -> {
            ApiResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertTrue(authenticationRepository.existsById(response.getData().getRefreshToken()));
        });
    }

    @Test
    void loginWrongCredentials() throws Exception {
        String json = """
                {
                    "username": "user",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Kredensial yang anda berikan salah")
        );
    }

    @Test
    void refreshSuccess() throws Exception {
        String loginRequest = """
                {
                    "username": "user",
                    "password": "password"
                }
                """;

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginRequest)
        ).andReturn();

        ApiResponse<LoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        String refreshRequest = String.format("""
                {
                    "refreshToken": "%s"
                }
                """, loginResponse.getData().getRefreshToken());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(refreshRequest)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Access Token berhasil diperbarui"),
                MockMvcResultMatchers.jsonPath("data.accessToken").exists()
        ).andDo(result -> {
            ApiResponse<Map<String, String>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotSame(response.getData().get("accessToken"), loginResponse.getData().getAccessToken());
        });
    }

    @Test
    void refreshInvalidToken() throws Exception {
        String json = """
                {
                    "refreshToken": "invalidrefreshtoken"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Refresh token tidak valid")
        );
    }

    @Test
    void logoutSuccess() throws Exception {
        String loginRequest = """
                {
                    "username": "user",
                    "password": "password"
                }
                """;

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginRequest)
        ).andReturn();

        ApiResponse<LoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        String logoutRequest = String.format("""
                {
                    "refreshToken": "%s"
                }
                """, loginResponse.getData().getRefreshToken());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(logoutRequest)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Refresh token berhasil dihapus")
        );

        Assertions.assertFalse(authenticationRepository.existsById(loginResponse.getData().getRefreshToken()));
    }

    @Test
    void logoutInvalidToken() throws Exception {
        String json = """
                {
                    "refreshToken": "invalidrefreshtoken"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Refresh token tidak valid")
        );
    }

    @Test
    void authorization() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/notes")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Anda tidak berhak mengakses resource ini")
        );
    }
}