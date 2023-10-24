package com.tcc.seboonline.servicos;

import java.util.Optional;

import com.tcc.seboonline.DTOs.ImagemLivroDTO;
import com.tcc.seboonline.modelos.ImagensLivros;
import com.tcc.seboonline.repositorios.ImagensLivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.excecoes.ImagemNãoEncontradaException;

@Service
public class ImagemService {

    @Autowired
    private ImagensLivrosRepository imageRepository;

    public ImagensLivros getImage(int id) throws ImagemNãoEncontradaException {
        Optional<ImagensLivros> optionalImage = imageRepository.findById(id);
        
        if (optionalImage.isEmpty()) 
            throw new ImagemNãoEncontradaException("Uma imagem com  " + id + " Não foi encontrado.");

        return optionalImage.get();
    }

    public ImagensLivros uploadImage(ImagemLivroDTO imagePost) {
        ImagensLivros image = new ImagensLivros();

        image.setType(imagePost.getType());
        image.setContent(imagePost.getContent());

        return imageRepository.save(image);
    }
    
}
