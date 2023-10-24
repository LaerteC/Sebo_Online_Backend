package com.tcc.seboonline.utils;


import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class FiltroPalavras {
    private static final Logger logger = LoggerFactory.getLogger(FiltroPalavras.class);
    private static final Set<String> whitelist = new HashSet<>(Arrays.asList(
            "Cacete", "Mariquinha", "Tchola", "Fresco"
    ));
    private static final Set<String> blacklist = new HashSet<>(Arrays.asList(
            "filha da puta", "buceta", "bunda", "penis", "puta", "merda",
            "caralho", "cuzão", "vadia", "viado", "foda-se"
    ));

    public boolean hasProfanity(String text) {
        String[] words = text.split("\\s+"); // Split on whitespace

        for (String word : words) {
            if (whitelist.contains(word)) {
                continue;
            }
            for (String badWord : blacklist) {
                if (word.toLowerCase().contains(badWord)) {
                    logger.warn("Palavra de linguagem imprópria detectada: {}", word);
                    return true;
                }
            }
        }
        return false;
    }
}
