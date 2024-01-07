package com.ivanzkyanto.senopi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.ExportNotesMessage;
import com.ivanzkyanto.senopi.model.request.ExportNotesRequest;
import com.ivanzkyanto.senopi.service.ExportService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    @NonNull
    private RabbitTemplate rabbitTemplate;

    @NonNull
    private Queue exportNotesQueue;

    @NonNull
    private ObjectMapper objectMapper;

    @Override
    public void exportNotes(User user, ExportNotesRequest request) {
        ExportNotesMessage exportNotesMessage = new ExportNotesMessage(user.getId().toString(), request.targetEmail());

        try {
            rabbitTemplate.convertAndSend(exportNotesQueue.getName(), objectMapper.writeValueAsString(exportNotesMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
