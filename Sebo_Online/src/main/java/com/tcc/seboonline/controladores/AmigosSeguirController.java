package com.tcc.seboonline.controladores;


import com.tcc.seboonline.DTOs.AmigosSeguirDTO;
import com.tcc.seboonline.modelos.Usuario;
import jakarta.servlet.http.HttpSession;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.tcc.seboonline.annotations.AutorizacaoUsuario;
import com.tcc.seboonline.excecoes.NemAmigoException;
import com.tcc.seboonline.excecoes.AmigoSeguidoException;
import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.AutoInscricaoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.servicos.InscricaoService;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/amigosinscricao")
@AllowSysOut
public class AmigosSeguirController {

    private static final Logger logger = LoggerFactory.getLogger(AmigosSeguirController.class);

    @Autowired
    private InscricaoService subscriptionService;

    @AutorizacaoUsuario
    @PatchMapping
    public ResponseEntity<Object> patchSubscription(@RequestBody AmigosSeguirDTO subscriptionDTO, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            var result = subscriptionService.patchSubscription(subscriptionDTO, user);
            logger.info("Assinatura atualizada com sucesso: " + result.getClass());
            return ResponseEntity.ok(result);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UsuarioNEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AmigoSeguidoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AutoInscricaoException e) {
            logger.error("Erro ao atualizar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PatchMapping("/unsubscribe")
    public ResponseEntity<Object> patchUnsubscribe(@RequestBody AmigosSeguirDTO subscriptionDTO, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            var result = subscriptionService.patchUnsubscribe(subscriptionDTO, user);
            logger.info("Assinatura cancelada com sucesso: " + result.getClass());
            return ResponseEntity.ok(result);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao cancelar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UsuarioNEncontradoException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NemAmigoException e) {
            logger.error("Erro ao cancelar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
