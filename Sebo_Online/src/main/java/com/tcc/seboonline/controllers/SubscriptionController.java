package com.tcc.seboonline.controllers;


import com.tcc.seboonline.dtos.SubscriptionDTO;
import com.tcc.seboonline.models.User;
import jakarta.servlet.http.HttpSession;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.exceptions.NoSuchSubscriberException;
import com.tcc.seboonline.exceptions.ProfileAlreadySubscribedException;
import com.tcc.seboonline.exceptions.ProfileNotFoundException;
import com.tcc.seboonline.exceptions.SelfSubscriptionException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.services.SubscriptionService;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/subscription")
@AllowSysOut
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriptionService subscriptionService;

    @Authorized
    @PatchMapping
    public ResponseEntity<Object> patchSubscription(@RequestBody SubscriptionDTO subscriptionDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            var result = subscriptionService.patchSubscription(subscriptionDTO, user);
            logger.info("Assinatura atualizada com sucesso: " + result.getClass());
            return ResponseEntity.ok(result);
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileAlreadySubscribedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SelfSubscriptionException e) {
            logger.error("Erro ao atualizar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PatchMapping("/unsubscribe")
    public ResponseEntity<Object> patchUnsubscribe(@RequestBody SubscriptionDTO subscriptionDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            var result = subscriptionService.patchUnsubscribe(subscriptionDTO, user);
            logger.info("Assinatura cancelada com sucesso: " + result.getClass());
            return ResponseEntity.ok(result);
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao cancelar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NoSuchSubscriberException e) {
            logger.error("Erro ao cancelar a assinatura: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
