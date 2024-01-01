package com.ivanzkyanto.senopi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.senopi.entity.Collaboration;
import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.LoginResponse;
import com.ivanzkyanto.senopi.repository.AuthenticationRepository;
import com.ivanzkyanto.senopi.repository.CollaborationRepository;
import com.ivanzkyanto.senopi.repository.NoteRepository;
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
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class CollaborationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CollaborationRepository collaborationRepository;

    private User user;

    private Note note;

    private String token;

    private void cleanUpDatabase() {
        noteRepository.deleteAll();
        userRepository.deleteAll();
        collaborationRepository.deleteAll();
        authenticationRepository.deleteAll();
    }

    @BeforeEach
    void setUp() throws Exception {
        cleanUpDatabase();

        user = User.builder()
                .username("user")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("User")
                .build();

        userRepository.save(user);

        note = Note.builder()
                .owner(user)
                .title("Note")
                .body("Sample note")
                .build();

        noteRepository.save(note);

        String json = """
                {
                    "username": "user",
                    "password": "password"
                }
                """;

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        ApiResponse<LoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        token = loginResponse.getData().getAccessToken();
    }

    @AfterEach
    void tearDown() {
        cleanUpDatabase();
    }

    @Test
    void addSuccess() throws Exception {
        User collaborator = User.builder()
                .username("collaborator")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("Collaborator")
                .build();

        userRepository.save(collaborator);

        String json = String.format("""
                {
                    "userId": "%s",
                    "noteId": "%s"
                }
                """, collaborator.getId().toString(), note.getId().toString());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/collaborations")
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("data.collaborationId").exists()
        ).andDo(result -> {
            ApiResponse<Map<String, String>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertTrue(collaborationRepository.existsById(UUID.fromString(response.getData().get("collaborationId"))));
        });
    }

    @Test
    void addToNotOwnedNote() throws Exception {
        User collaborator = User.builder()
                .username("collaborator")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("Collaborator")
                .build();

        User otherUser = User.builder()
                .username("otheruser")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("otheruser")
                .build();

        userRepository.save(collaborator);
        userRepository.save(otherUser);

        note.setOwner(otherUser);
        noteRepository.save(note);

        String json = String.format("""
                {
                    "userId": "%s",
                    "noteId": "%s"
                }
                """, collaborator.getId().toString(), note.getId().toString());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/collaborations")
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isForbidden(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Anda tidak berhak mengakses resource ini")
        );
    }

    @Test
    void addOwner() throws Exception {
        String json = String.format("""
                {
                    "userId": "%s",
                    "noteId": "%s"
                }
                """, user.getId().toString(), note.getId().toString());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/collaborations")
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Gagal menambahkan karena user adalah pemilik catatan")
        );
    }

    @Test
    void deleteSuccess() throws Exception {
        User collaborator = User.builder()
                .username("collaborator")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("Collaborator")
                .build();

        userRepository.save(collaborator);

        Collaboration collaboration = Collaboration.builder()
                .user(collaborator)
                .note(note)
                .build();

        collaborationRepository.save(collaboration);

        String json = String.format("""
                {
                    "userId": "%s",
                    "noteId": "%s"
                }
                """, collaborator.getId().toString(), note.getId().toString());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/collaborations")
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Kolaborasi berhasil dihapus")
        );

        Assertions.assertFalse(collaborationRepository.existsById(collaboration.getId()));
    }

    @Test
    void deleteNotFound() throws Exception {
        User collaborator = User.builder()
                .username("collaborator")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .fullname("Collaborator")
                .build();

        userRepository.save(collaborator);

        String json = String.format("""
                {
                    "userId": "%s",
                    "noteId": "%s"
                }
                """, collaborator.getId().toString(), note.getId().toString());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/collaborations")
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Kolaborasi tidak ditemukan")
        );
    }
}