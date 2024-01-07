package com.ivanzkyanto.senopi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.ExportNotesRequest;

public interface ExportService {

    void exportNotes(User user, ExportNotesRequest request) throws JsonProcessingException;

}
