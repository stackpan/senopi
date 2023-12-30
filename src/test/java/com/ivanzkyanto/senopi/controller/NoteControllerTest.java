package com.ivanzkyanto.senopi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.senopi.entity.Note;
import com.ivanzkyanto.senopi.entity.Tag;
import com.ivanzkyanto.senopi.model.response.ApiResponse;
import com.ivanzkyanto.senopi.model.response.NoteResponse;
import com.ivanzkyanto.senopi.repository.NoteRepository;
import com.ivanzkyanto.senopi.repository.TagRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private NoteRepository noteRepository;

    private void cleanUpDatabase() {
        tagRepository.deleteAll();
        noteRepository.deleteAll();
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
    void createSuccess() throws Exception {
        String json = """
                {
                    "title": "Catatan",
                    "body": "Ini adalah contoh catatan",
                    "tags": ["sample", "contoh", "dummy"]
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Catatan berhasil ditambahkan")
        ).andDo(result -> {
            ApiResponse<Map<String, String>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            Assertions.assertNotNull(response.getData());

            String noteId = response.getData().get("noteId");

            Assertions.assertNotNull(noteId);
            Assertions.assertTrue(noteRepository.existsById(UUID.fromString(noteId)));
        });
    }

    @Test
    void createWithBadPayload() throws Exception {
        String json = """
                {
                    "body": "Ini adalah contoh catatan",
                    "tags": ["sample", "contoh", "dummy"]
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.jsonPath("status").value("fail")
        );
    }

    @Test
    void getAllSuccess() throws Exception {
        for (int i = 1; i <= 3; i++) {
            Note note = Note.builder()
                    .title("Catatan " + i)
                    .body("Ini adalah catatan " + i)
                    .build();

            noteRepository.save(note);
            for (int j = 1; j <= 2; j++) {
                Tag tag = Tag.builder()
                        .note(note)
                        .body("tag-" + i + "-" + j)
                        .build();

                tagRepository.save(tag);
            }
        }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/notes")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("status").value("success")
        ).andDo(result -> {
            ApiResponse<Map<String, List<NoteResponse>>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotNull(response.getData());

            List<NoteResponse> notes = response.getData().get("notes");
            Assertions.assertNotNull(notes);
            Assertions.assertEquals(3, notes.size());
            Assertions.assertEquals(2, notes.get(0).getTags().size());
        });
    }

    @Test
    void getSuccess() throws Exception {
        Note note = Note.builder()
                .title("Catatan ")
                .body("Ini adalah catatan ")
                .build();

        noteRepository.save(note);
        for (int i = 1; i <= 3; i++) {
            Tag tag = Tag.builder()
                    .note(note)
                    .body("tag-" + i)
                    .build();

            tagRepository.save(tag);
        }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/notes/" + note.getId())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("status").value("success")
        ).andDo(result -> {
            ApiResponse<Map<String, NoteResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotNull(response.getData());

            NoteResponse noteResponse = response.getData().get("note");
            Assertions.assertNotNull(noteResponse);
            Assertions.assertEquals(note.getId().toString(), noteResponse.getId());
        });
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/notes/note-id")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Catatan tidak ditemukan")
        );
    }

    @Test
    void updateSuccess() throws Exception {
        Note note = Note.builder()
                .title("Catatan ")
                .body("Ini adalah catatan ")
                .build();

        noteRepository.save(note);
        for (int i = 1; i <= 3; i++) {
            Tag tag = Tag.builder()
                    .note(note)
                    .body("tag-" + i)
                    .build();

            tagRepository.save(tag);
        }

        String json = """
                {
                    "title": "Catatan",
                    "body": "Ini adalah contoh catatan yang sudah diubah",
                    "tags": ["sample", "contoh", "dummy"]
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/notes/" + note.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Catatan berhasil diperbarui")
        );

        Note updatedNote = noteRepository.findById(note.getId()).orElse(null);

        assert updatedNote != null;
        Assertions.assertNotEquals(note.getBody(), updatedNote.getBody());
    }

    @Test
    void updateNotFound() throws Exception {
        String json = """
                {
                    "title": "Catatan",
                    "body": "Ini adalah contoh catatan yang sudah diubah",
                    "tags": ["sample", "contoh", "dummy"]
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/notes/note-id")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Catatan tidak ditemukan")
        );
    }

    @Test
    void deleteSuccess() throws Exception {
        Note note = Note.builder()
                .title("Catatan ")
                .body("Ini adalah catatan ")
                .build();

        noteRepository.save(note);
        for (int i = 1; i <= 3; i++) {
            Tag tag = Tag.builder()
                    .note(note)
                    .body("tag-" + i)
                    .build();

            tagRepository.save(tag);
        }

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/notes/" + note.getId())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("status").value("success"),
                MockMvcResultMatchers.jsonPath("message").value("Catatan berhasil dihapus")
        );

        Assertions.assertFalse(noteRepository.existsById(note.getId()));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/notes/note-id")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("status").value("fail"),
                MockMvcResultMatchers.jsonPath("message").value("Catatan tidak ditemukan")
        );
    }
}