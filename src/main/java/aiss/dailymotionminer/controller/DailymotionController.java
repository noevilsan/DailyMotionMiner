package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.etl.transformer;
import aiss.dailymotionminer.model.videominer.Channel; // Importante: usamos el modelo de VideoMiner
import aiss.dailymotionminer.service.DailymotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dailymotionminer") // Cambiado para que coincida con Postman
public class DailymotionController {

    private final DailymotionService dailymotionService;

    @Autowired
    public DailymotionController(DailymotionService dailymotionService){
        this.dailymotionService = dailymotionService;
    }

    // Cambiado a GET y añadida la ruta /channel
    @GetMapping("/channel/{name}")
    public Channel getChannel(
            @PathVariable String name,
            @RequestParam(defaultValue = "10", required = false) Integer maxVideos,
            @RequestParam(defaultValue = "10", required = false) Integer maxComments){

        // CORRECCIÓN: Pasamos 'dailymotionService' como cuarto parámetro
        return transformer.buildChannel(name, maxVideos, maxComments, dailymotionService);
    }
}