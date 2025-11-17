package com.ifsp.forum.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurtidaRequestDTO {
    private Long usuarioId;
    private Long postId;
    private Long comentarioId;
}
