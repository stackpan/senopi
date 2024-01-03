package com.ivanzkyanto.senopi.service;

import com.ivanzkyanto.senopi.model.request.Request;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.function.Supplier;

public interface ValidationService {

    void validatePayload(Request request);

    UUID validateUuid(String uuid);

    <X extends Throwable> UUID validateUuid(String uuid, Supplier<? extends X> exceptionSupplier) throws X;

    void validateMultipartContentType(MultipartFile multipartFile, String... contentTypes);

}
