package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.dailymotionminer.DailymotionVideo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DailymotionService {
    private final RestTemplate restTemplate= new RestTemplate();

    public DailymotionVideo getVideo(String videoId){
        String url = "https://api.dailymotion.com/video/" + videoId + "?fields=id,title,description,created_time"
    }
}
