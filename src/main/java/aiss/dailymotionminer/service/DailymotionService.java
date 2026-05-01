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

    // --- Peticiones a la API de Dailymotion ---

    public DailymotionChannel getChannel(String userId) {
        String url = "https://api.dailymotion.com/user/" + userId + "?fields=id,screenname,description,created_time";
        return restTemplate.getForObject(url, DailymotionChannel.class);
    }

    public DailymotionVideo getVideo(String videoId) {
        String url = "https://api.dailymotion.com/video/" + videoId + "?fields=id,title,description,created_time";
        return restTemplate.getForObject(url, DailymotionVideo.class);
    }

    public DailymotionVideoList getVideosFromUser(String userId, Integer limit, Integer page) {
        String url = "https://api.dailymotion.com/user/" + userId + "/videos?limit=" + limit + "&page=" + page + "&fields=id,title,description,created_time";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }

    public Object getCaptions(String videoId) {
        String url = "https://api.dailymotion.com/video/" + videoId + "/subtitles";
        return restTemplate.getForObject(url, Object.class);
    }


    // --- Mapeo a modelos de VideoMiner ---

    public Channel buildChannel(String userId, Integer maxVideos, Integer maxComments) {
        DailymotionChannel dmChannel = getChannel(userId);
        if (dmChannel == null) return null;

        // Montamos los datos base del canal
        Channel channel = new Channel();
        channel.setId(dmChannel.getId());
        channel.setName(dmChannel.getName()); // Usamos el getScreenname() de tu modelo o name si lo mapeaste así
        channel.setDescription(dmChannel.getDescription());
        channel.setCreatedTime(dmChannel.getCreatedTime() != null ? dmChannel.getCreatedTime().toString() : null);

        // Procesamos los videos aparte para que esto quede limpio
        channel.setVideos(this.mapVideos(userId, maxVideos, dmChannel.getName()));

        return channel;
    }

    private List<Video> mapVideos(String userId, Integer maxVideos, String authorName) {
        DailymotionVideoList dmVideoList = getVideosFromUser(userId, maxVideos, 1);
        List<Video> videos = new ArrayList<>();

        if (dmVideoList != null && dmVideoList.getList() != null) {
            for (DailymotionVideo dmVideo : dmVideoList.getList()) {
                // Ahora esta línea dejará de estar roja porque el método de abajo ya acepta 3 cosas
                videos.add(this.buildVideo(dmVideo, authorName, userId));
            }
        }
        return videos;
    }

    // HEMOS AÑADIDO: ", String userId" al final de los paréntesis
    private Video buildVideo(DailymotionVideo dmVideo, String authorName, String userId) {
        Video video = new Video();
        video.setId(dmVideo.getId());
        video.setName(dmVideo.getName());
        video.setDescription(dmVideo.getDescription());
        video.setReleaseTime(dmVideo.getReleaseTime() != null ? dmVideo.getReleaseTime().toString() : null);

        User author = new User();
        author.setName(authorName);

        // Ahora sí podemos usar el userId para construir el link sin errores
        author.setUser_link("https://www.dailymotion.com/" + userId);

        video.setAuthor(author);
        video.setComments(new ArrayList<>());
        video.setCaptions(new ArrayList<>());

        return video;
    }
}