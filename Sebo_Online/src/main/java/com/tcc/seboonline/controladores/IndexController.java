package com.tcc.seboonline.controladores;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
public class IndexController implements ErrorController {
    private static final String PATH = "/error";

    private static final Logger LOGGER = LogManager.getLogger(IndexController.class);

    @RequestMapping(value = PATH)
    public ModelAndView saveLeadQuery() {
        LOGGER.info("Recebeu uma solicitação de erro");

        return new ModelAndView("forward:/");
    }

}
