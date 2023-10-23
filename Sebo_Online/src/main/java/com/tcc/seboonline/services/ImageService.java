package com.tcc.seboonline.services;

import java.util.Optional;

import com.tcc.seboonline.dtos.ImagePostDTO;
import com.tcc.seboonline.models.Image;
import com.tcc.seboonline.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.exceptions.ImageNotFoundException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image getImage(int id) throws ImageNotFoundException {
        Optional<Image> optionalImage = imageRepository.findById(id);
        
        if (optionalImage.isEmpty()) 
            throw new ImageNotFoundException("Uma imagem com  " + id + " Não foi encontrado.");

        return optionalImage.get();
    }

    public Image uploadImage(ImagePostDTO imagePost) {
        Image image = new Image();

        image.setType(imagePost.getType());
        image.setContent(imagePost.getContent());

        return imageRepository.save(image);
    }
    
}
