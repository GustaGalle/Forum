package com.ifsp.forum.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Judge0Request {
    private String source_code;
    private String language_id; // Python = 71, Java = 62, etc.
    private List<String> stdin; // Entrada do programa
}

