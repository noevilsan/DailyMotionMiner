package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.dailymotionminer.*;
import aiss.dailymotionminer.model.videominer.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailymotionService {

    private final RestTemplate restTemplate = new RestTemplate();

    public DailymotionChannel getChannel(String userId) {
        String url = "https://api.dailymotion.com/user/" + userId + "?fields=id,screenname,description,created_time";
        return restTemplate.getForObject(url, DailymotionChannel.class);
    }

    public DailymotionVideo getVideo(String videoId) {
        String url = "https://api.dailymotion.com/video/" + videoId + "?fields=id,title,description,created_time";
        return restTemplate.getForObject(url, DailymotionVideo.class);
    }

    public DailymotionVideoList getVideosFromUser(String userId, Integer limit, Integer page) {
        // PEDIMOS LOS AI_SUBTITLE_LANGUAGES DIRECTAMENTE AQUÍ
        String url = "https://api.dailymotion.com/user/" + userId + "/videos?limit=" + limit + "&page=" + page + "&fields=id,title,description,created_time,tags";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }
    // Añade esto a tu DailymotionService.java
    public DailymotionCaptionResponse getCaptions(String videoId) {
        try {
            String url = "https://api.dailymotion.com/video/" + videoId + "/subtitles?fields=id,url,language";
            return restTemplate.getForObject(url, DailymotionCaptionResponse.class);
        } catch (Exception e) {
            System.err.println("Error al obtener subtítulos para el vídeo " + videoId + ": " + e.getMessage());
            return null;
        }
    }


}