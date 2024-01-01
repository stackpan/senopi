package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.request.CollaborationRequest;

public interface CollaborationService {

    String add(User user, CollaborationRequest request);

    void delete(User user, CollaborationRequest request);

}
