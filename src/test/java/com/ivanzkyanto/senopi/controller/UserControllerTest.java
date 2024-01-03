package com.ivanzkyanto.senopi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.repository.NoteRepository;
import com.ivanzkyanto.senopi.repository.TagRepository;
import com.ivanzkyanto.senopi.repository.UserRepository;
import com.ivanzkyanto.senopi.security.BCrypt;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private void cleanUpDatabase() {
        tagRepository.deleteAll();
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        cleanUpDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanUpDatabase();
    }

    @Test
    void registerSuccess() throws Exception {
        String json = """
                {
                    "username": "johndoe",
                    "password": "password",
                    "fullname": "John Doe"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("User berhasil ditambahkan"),
                MockMvcResultMatchers.jsonPath("data.userId").exists()
        ).andDo(result -> {
            ApiResponse<Map<String, String>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            String userId = response.getData().get("userId");
            Assertions.assertTrue(userRepository.existsById(UUID.fromString(userId)));
        });
    }

    @Test
    void registerBadPayload() throws Exception {
        String json = """
                {
                    "username": "johndoe",
                    "password": "password",
                    "fullname": 0
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").exists()
        );
    }

    @Test
    void registerAlreadyExistedUsername() throws Exception {
        User user = User.builder()
                .username("johndoe")
                .password("password1")
                .fullname("John Doe 1")
                .build();

        userRepository.save(user);

        String json = """
                {
                    "username": "johndoe",
                    "password": "password2",
                    "fullname": "John Doe 2"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Gagal menambahkan user. Username sudah digunakan.")
        );
    }

    @Test
    void registerFailed() throws Exception {
        String json = """
                {
                    "password": "password",
                    "fullname": "John Doe"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value(Matchers.containsString("username"))
        );
    }

    @Test
    void search() throws Exception {
        for (int i = 1; i <= 5; i++) {
            User user = User.builder()
                    .username(((i % 2 == 0) || (i % 3 == 0) ? "fizz" : "buzz") + "-" + i)
                    .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                    .fullname("fullname-" + i)
                    .build();
            userRepository.save(user);
        }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users?username=fizz")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("data.users").isNotEmpty()
        );
    }

    @Test
    void getSuccess() throws Exception {
        User user = User.builder()
                .username("johndoe")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("John Doe")
                .build();
        userRepository.save(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("data.user").exists(),
                MockMvcResultMatchers.jsonPath("data.user.id").value(user.getId().toString()),
                MockMvcResultMatchers.jsonPath("data.user.username").value(user.getUsername())
        );
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/fictional-id")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("User tidak ditemukan")
        );
    }
}