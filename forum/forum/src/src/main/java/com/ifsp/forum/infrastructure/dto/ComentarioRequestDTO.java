package com.ifsp.forum.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComentarioRequestDTO {
    private String mensagem;
    private Long postId;
    private Long usuarioId;
}
