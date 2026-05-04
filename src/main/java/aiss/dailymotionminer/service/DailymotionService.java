package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.DailymotionCaption;
import aiss.dailymotionminer.model.dailymotionminer.DailymotionChannel;
import aiss.dailymotionminer.model.dailymotionminer.DailymotionVideo;
import aiss.dailymotionminer.model.dailymotionminer.DailymotionVideoList;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DailymotionService {
    private final RestTemplate restTemplate= new RestTemplate();

    public DailymotionVideo getVideo(String videoId){
        String url = "https://api.dailymotion.com/video/" + videoId + "?fields=id,title,description,created_time";
        return restTemplate.getForObject(url, DailymotionVideo.class);
    }

    public DailymotionVideoList getRawVideos(String userId, int maxVideos, int page) {
        String url = "https://api.dailymotion.com/user/" + userId + "/videos"
                + "?limit=" + maxVideos + "&page=" + page
                + "&fields=id,title,description,created_time,tags";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }
    public DailymotionChannel getChannel(String userId){
        String url = "https://api.dailymotion.com/user/" + userId + "?fields=id,screenname,description,created_time";
        return restTemplate.getForObject(url, DailymotionChannel.class);
    }
    public DailymotionCaption[] getCaptions(String videoId) {
        String url = "https://api.dailymotion.com/video/" + videoId + "/subtitles?fields=id,language,url";
        return restTemplate.getForObject(url, DailymotionCaption[].class);
    }
}
