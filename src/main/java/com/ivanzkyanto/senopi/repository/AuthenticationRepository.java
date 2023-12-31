package com.ivanzkyanto.senopi.repository;

import com.ivanzkyanto.senopi.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication, String> {
}
