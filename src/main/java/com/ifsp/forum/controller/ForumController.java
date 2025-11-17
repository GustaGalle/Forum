package com.ifsp.forum.controller;

import com.ifsp.forum.business.ForumService;
import com.ifsp.forum.infrastructure.dto.ForumRequestDTO;
import com.ifsp.forum.infrastructure.dto.ForumResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @PostMapping
    public ResponseEntity<Void> salvarForum(@RequestBody ForumRequestDTO forumRequestDTO) {
        forumService.salvarForum(forumRequestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ForumResponseDTO>> listarForuns() {
        return ResponseEntity.ok(forumService.listarForuns());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ForumResponseDTO> buscarForumPorId(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.buscarForumPorId(id));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarForum(@PathVariable Long id,
                                               @RequestBody ForumRequestDTO forumRequestDTO) {
        forumService.atualizarForum(id, forumRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarForum(@PathVariable Long id) {
        forumService.deletarForum(id);
        return ResponseEntity.noContent().build();
    }
}
