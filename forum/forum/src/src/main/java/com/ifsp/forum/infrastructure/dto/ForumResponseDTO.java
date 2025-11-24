package com.ifsp.forum.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private List<Long> usuariosIds;
    private List<Long> topicosIds;
}
