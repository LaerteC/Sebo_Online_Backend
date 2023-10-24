package com.tcc.seboonline.controladores;

import java.util.List;

import com.tcc.seboonline.modelos.MensagensGenericas;
import com.tcc.seboonline.modelos.PerfilUsuario;
import com.tcc.seboonline.servicos.PesquisarLivroService;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.seboonline.excecoes.UsuarioNaoEncontradoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/search")
@AllowSysOut
public class PesquisarController {

    private static final Logger logger = LoggerFactory.getLogger(PesquisarController.class);

    @Autowired
    private PesquisarLivroService searchService;

    @GetMapping("/profiles")
    public ResponseEntity<Object> getProfiles(@RequestParam(name = "limit", required = false) String limit) {
        try {
            long limitResponseLength = limit != null ? Long.parseLong(limit) : 12;

            List<PerfilUsuario> profiles = searchService.getProfiles(limitResponseLength);
            logger.info("Recuperados perfis com sucesso. Número de perfis retornados: " + profiles.size());
            return ResponseEntity.ok(profiles);
        } catch (NumberFormatException e) {
            logger.error("Formato incorreto da consulta. O parâmetro limite deve ser um número.", e);
            return ResponseEntity.badRequest().body(new MensagensGenericas<>("Formato incorreto da consulta. O parâmetro limite deve ser um número.", null));
        }
    }


    @GetMapping("/specific-profiles")
    public ResponseEntity<Object> getSpecificProfiles(@RequestParam(name = "firstName") String firstName,
                                                      @RequestParam(name = "lastName") String lastName)
    {
        try {
            List<PerfilUsuario> profiles = searchService.getSpecificProfiles(firstName, lastName);
            if (profiles.isEmpty()) {
                logger.info("Nenhum perfil encontrado para os nomes: " + firstName + " " + lastName);
                return ResponseEntity.notFound().build();
            }
            logger.info("Recuperados perfis específicos com sucesso. Número de perfis retornados: " + profiles.size());
            return ResponseEntity.ok(profiles);
        } catch (UsuarioNaoEncontradoException e) {
            logger.error("Nenhum perfil encontrado para os nomes: " + firstName + " " + lastName, e);
            return ResponseEntity.notFound().build();
        }
    }
}
