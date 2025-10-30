package com.ifsp.forum.infrastructure.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
}
