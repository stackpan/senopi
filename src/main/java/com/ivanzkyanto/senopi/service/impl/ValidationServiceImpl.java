package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.model.request.Request;
import com.ivanzkyanto.senopi.service.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    @NonNull
    private Validator validator;

    @NonNull
    private Tika tika;

    @Override
    public void validatePayload(Request request) {
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    public UUID validateUuid(String uuid) {
        return validateUuid(uuid, () -> null);
    }

    @Override
    public <X extends Throwable> UUID validateUuid(String uuid, Supplier<? extends X> exceptionSupplier) throws X {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            if (exceptionSupplier != null) {
                throw exceptionSupplier.get();
            }
            throw e;
        }
    }

    @Override
    public void validateMultipartContentType(MultipartFile multipartFile, String... contentTypes) {
        try {
            String multipartFileContentType = tika.detect(multipartFile.getInputStream());

            assert multipartFileContentType != null;
            for (String contentType: contentTypes) {
                if (multipartFileContentType.equals(contentType)) {
                    return;
                }
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File content type is not supported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
