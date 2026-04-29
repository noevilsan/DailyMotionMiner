package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.dailymotionminer.DailymotionVideo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DailymotionService {
    private final RestTemplate restTemplate= new RestTemplate();

    public DailymotionVideo getVideo(String videoId){
        String url = "https://api.dailymotion.com/video/" + videoId + "?fields=id,name,description,releaseTime";
        return restTemplate.getForObject(url, DailymotionVideo.class);
    }
    public DailymotionVideoList getVideosFromUser(String userId){
        String url= "https://api.dailymotion.com/user/" + userId + "/videos";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }
    public DailymotionChannel getChanel(String userId){
        String url = "https://api.dailymotion.com/user/" + userId + "?fields=id,name,description";
        return restTemplate.getForObject(url, DailymotionChannel.class);
    }
}
