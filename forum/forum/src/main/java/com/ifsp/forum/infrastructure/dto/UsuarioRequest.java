package com.ifsp.forum.infrastructure.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {
    private String nome;
    private String email;
    private String password;
}
