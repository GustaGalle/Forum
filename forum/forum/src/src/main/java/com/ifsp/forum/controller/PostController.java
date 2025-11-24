package com.ifsp.forum.controller;

import com.ifsp.forum.business.PostService;
import com.ifsp.forum.infrastructure.dto.PostRequestDTO;
import com.ifsp.forum.infrastructure.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/salvar")
    public ResponseEntity<Void> salvarPost(@RequestBody PostRequestDTO dto) {
        postService.salvarPost(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> listarPosts() {
        return ResponseEntity.ok(postService.listarPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> buscarPostPorId(@PathVariable Long id) {
        return ResponseEntity.ok(postService.buscarPostPorId(id));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarPost(@PathVariable Long id, @RequestBody PostRequestDTO dto) {
        postService.atualizarPost(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarPost(@PathVariable Long id) {
        postService.deletarPostPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/topico/{topicoId}")
    public ResponseEntity<List<PostResponseDTO>> buscarPostsPorTopico(@PathVariable Long topicoId) {
        return ResponseEntity.ok(postService.buscarPostsPorTopico(topicoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PostResponseDTO>> buscarPostsPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(postService.buscarPostsPorUsuario(usuarioId));
    }
}
