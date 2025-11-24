package com.ifsp.forum.infrastructure.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissaoDTO {
    private Long usuarioId;
    private Long algoritmoId;
    private String codigo;
}
