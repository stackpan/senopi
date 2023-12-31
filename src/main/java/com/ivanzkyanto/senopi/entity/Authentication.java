package com.ivanzkyanto.senopi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authentications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {

    @Id
    private String Token;

}
