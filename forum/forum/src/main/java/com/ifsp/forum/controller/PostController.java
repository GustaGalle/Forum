package com.ifsp.forum.controller;

import com.ifsp.forum.business.PostService;
import com.ifsp.forum.infrastructure.entidys.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Salvar post
    @PostMapping("/salvar")
    public ResponseEntity<Void> salvarPost(@RequestBody Post post,
                                           @RequestParam Long topicoId,
                                           @RequestParam Long usuarioId) {
        postService.salvarPost(post, topicoId, usuarioId);
        return ResponseEntity.ok().build();
    }
    // Listar todos os posts
    @GetMapping
    public ResponseEntity<List<Post>> listarPosts() {
        return ResponseEntity.ok(postService.listarPosts());
    }

    // Buscar post por ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> buscarPostPorId(@PathVariable Long id) {
        return ResponseEntity.ok(postService.buscarPostPorId(id));
    }

    // Atualizar post
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarPost(@PathVariable Long id, @RequestBody Post post) {
        postService.atualizarPost(id, post);
        return ResponseEntity.ok().build();
    }

    // Deletar post
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarPost(@PathVariable Long id) {
        postService.deletarPostPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar posts por tópico
    @GetMapping("/topico/{topicoId}")
    public ResponseEntity<List<Post>> buscarPostsPorTopico(@PathVariable Long topicoId) {
        return ResponseEntity.ok(postService.buscarPostsPorTopico(topicoId));
    }

    // Buscar posts por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Post>> buscarPostsPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(postService.buscarPostsPorUsuario(usuarioId));
    }
}
