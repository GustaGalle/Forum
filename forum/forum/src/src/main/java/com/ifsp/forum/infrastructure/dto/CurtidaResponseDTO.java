package com.ifsp.forum.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurtidaResponseDTO {
    private Long id;
    private String nomeUsuario;
    private Long postId;
    private Long comentarioId;
}
