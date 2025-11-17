package com.ifsp.forum.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComentarioResponseDTO {
    private Long id;
    private String mensagem;
    private String nomeUsuario;
    private Long postId;
}