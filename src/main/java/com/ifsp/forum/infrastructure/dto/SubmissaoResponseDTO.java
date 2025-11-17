package com.ifsp.forum.infrastructure.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissaoResponseDTO {
    private Long id;
    private Long usuarioId;
    private Long algoritmoId;
    private String codigo;
    private Boolean aprovado;
    private String feedback;


}
