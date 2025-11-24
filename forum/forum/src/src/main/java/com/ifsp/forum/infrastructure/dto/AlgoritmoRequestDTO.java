package com.ifsp.forum.infrastructure.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgoritmoRequestDTO {
    private String titulo;
    private String descricao;
    private String linguagemPermitida;
    private String gabarito;
    private Integer judge0LanguageId;
    private String casosDeTeste;
}
