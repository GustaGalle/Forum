package com.ifsp.forum.infrastructure.dto;

import com.ifsp.forum.infrastructure.entidys.TipoUsuario;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {
    private String nome;
    private String email;
    private String password;
    private TipoUsuario tipo;
    private Long usuarioCriadorId; // ID do usuário que está criando (deve ser PROFESSOR ou ALUNO_VETERANO)
}
