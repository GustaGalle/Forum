package com.ifsp.forum.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDTO {
    private String conteudo;
    private String descricao;
    private Long topicoId;
    private Long usuarioId;
}
