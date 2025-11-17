package com.ifsp.forum.controller;
import com.ifsp.forum.infrastructure.dto.UsuarioRequest;
import com.ifsp.forum.business.UsuarioService;
import com.ifsp.forum.infrastructure.dto.UsuarioResponse;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody UsuarioRequest usuarioRequest){
        usuarioService.salvarUsuario(usuarioRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorNome(@PathVariable String nome) {
        UsuarioResponse usuario = usuarioService.buscarUsuarioPorNome(nome);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id) {
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarUsuarioPorId(@PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest) {
        usuarioService.atualizarUsuarioPorId(id, usuarioRequest);
        return ResponseEntity.ok().build();
    }
}