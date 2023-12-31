package com.ivanzkyanto.senopi.resolver;

import com.ivanzkyanto.senopi.annotation.Authenticated;
import com.ivanzkyanto.senopi.entity.User;
import com.ivanzkyanto.senopi.model.TokenPayload;
import com.ivanzkyanto.senopi.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedArgumentResolver implements HandlerMethodArgumentResolver {

    @NonNull
    private TokenService tokenService;

    private final ResponseStatusException UNAUTHORIZED_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Anda tidak berhak mengakses resource ini");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    public User resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer")) throw UNAUTHORIZED_EXCEPTION;


        String token = Arrays.stream(bearer.split(" ")).toList().get(1);
        TokenPayload tokenPayload = tokenService.verifyAccessToken(token);

        return User.builder().id(UUID.fromString(tokenPayload.userId())).build();
    }
}
