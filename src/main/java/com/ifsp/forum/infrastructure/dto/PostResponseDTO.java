package com.ifsp.forum.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostResponseDTO {
    private Long id;
    private String conteudo;
    private String descricao;
    private Long topicoId;
    private Long usuarioId;
}
