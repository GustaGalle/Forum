package com.ifsp.forum.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicoRequestDTO {
    private String titulo;
    private String descricao;
    private Long forumId; // referência ao fórum
    private Long usuarioId; // ID do usuário que está criando o tópico
}
