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

    // --- MÉTODOS DE EXTRACCIÓN (API) ---

    public DailymotionChannel getChannel(String userId) {
        String url = "https://api.dailymotion.com/user/" + userId + "?fields=id,name,description,created_time";
        return restTemplate.getForObject(url, DailymotionChannel.class);
    }

    public DailymotionVideoList getVideosFromUser(String userId, Integer limit) {
        // Simplificamos la URL asumiendo página 1 por defecto para el minado
        String url = "https://api.dailymotion.com/user/" + userId + "/videos?limit=" + limit + "&fields=id,name,description,releaseTime";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }

    // --- MÉTODOS DE MAPEADO (LIMPIEZA ESTILO PEERTUBE) ---

    public Channel buildChannel(String userId, Integer maxVideos, Integer maxComments) {
        // 1. Obtenemos datos del canal
        DailymotionChannel dmChannel = getChannel(userId);

        // 2. Creamos y rellenamos el objeto Channel
        Channel channel = new Channel();
        channel.setId(dmChannel.getId());
        channel.setName(dmChannel.getName());
        channel.setDescription(dmChannel.getDescription());
        channel.setCreatedTime(dmChannel.getCreatedTime() != null ? dmChannel.getCreatedTime().toString() : "Unknown");

        // 3. Obtenemos y mapeamos los vídeos
        channel.setVideos(this.mapVideos(userId, maxVideos, dmChannel.getName()));

        return channel;
    }

    private List<Video> mapVideos(String userId, Integer maxVideos, String authorName) {
        DailymotionVideoList dmVideoList = getVideosFromUser(userId, maxVideos);
        List<Video> videos = new ArrayList<>();

        if (dmVideoList != null && dmVideoList.getList() != null) {
            for (DailymotionVideo dmVideo : dmVideoList.getList()) {
                videos.add(this.buildVideo(dmVideo, authorName));
            }
        }
        return videos;
    }

    private Video buildVideo(DailymotionVideo dmVideo, String authorName) {
        Video video = new Video();
        video.setId(dmVideo.getId());
        video.setName(dmVideo.getName());
        video.setDescription(dmVideo.getDescription());
        video.setReleaseTime(dmVideo.getReleaseTime() != null ? dmVideo.getReleaseTime().toString() : "Unknown");

        // Mapeo del Autor
        User author = new User();
        author.setName(authorName);
        video.setAuthor(author);

        // Inicialización de listas requeridas
        video.setComments(new ArrayList<>());
        video.setCaptions(new ArrayList<>());

        return video;
    }
}