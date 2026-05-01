package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.dailymotionminer.DailymotionChannel;
import aiss.dailymotionminer.service.DailymotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dailymotion")
public class DailymotionController {
    
    private final DailymotionService dailymotionService;

    @Autowired
    public DailymotionController(DailymotionService dailymotionService){
        this.dailymotionService = dailymotionService;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public DailymotionChannel createChannel(@PathVariable String id, 
            @RequestParam(defaultValue = "10", required = false) int maxVideos, 
            @RequestParam(defaultValue = "2", required = false) Integer maxPages){
        return dailymotionService.getChannel(id);
    }
}