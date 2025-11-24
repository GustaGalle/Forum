package com.ifsp.forum.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Judge0Response {
    private String stdout;
    private String stderr;
    private String compile_output;
    private Integer status; // 3 = sucesso, 6 = falha, etc.
}
