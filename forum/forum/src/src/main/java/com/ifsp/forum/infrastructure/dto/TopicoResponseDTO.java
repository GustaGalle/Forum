package com.ifsp.forum.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TopicoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Long forumId;
    private List<Long> postsIds;
}
