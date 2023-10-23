package com.tcc.seboonline.controllers;

import java.util.Base64;



import com.tcc.seboonline.dtos.ImagePostDTO;
import com.tcc.seboonline.models.Image;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.exceptions.ImageNotFoundException;
import com.tcc.seboonline.services.ImageService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger LOGGER = LogManager.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    /**
     * Obtém uma imagem pelo seu ID.
     *
     * @param imageId O ID da imagem.
     * @return A imagem, ou um erro 404 se a imagem não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") int imageId) {
        LOGGER.info("Recebeu uma solicitação para obter a imagem com o ID {}", imageId);

        try {
            Image image = imageService.getImage(imageId);
            byte[] byteImage = image.getContent().getBytes();
            byte[] decodedImage =  Base64.getDecoder().decode(byteImage);

            LOGGER.info("Imagem obtida com sucesso");

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(image.getType()))
                    .body(decodedImage);
        } catch (ImageNotFoundException e) {
            LOGGER.error("Imagem não encontrada", e);

            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Envia uma imagem.
     *
     * @param imagePost Os dados da imagem a ser enviada.
     * @return A imagem enviada.
     */
    @Authorized
    @PostMapping
    public ResponseEntity<?> postImage(@RequestBody ImagePostDTO imagePost, HttpSession httpSession) {
        LOGGER.info("Recebeu uma solicitação para enviar uma imagem");

        Image uploadedImage = imageService.uploadImage(imagePost);

        LOGGER.info("Imagem enviada com sucesso");

        return ResponseEntity.ok(uploadedImage);
    }
}

