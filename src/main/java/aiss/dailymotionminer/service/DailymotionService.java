package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.dailymotionminer.*;
import aiss.dailymotionminer.model.videominer.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        String url = "https://api.dailymotion.com/user/" + userId + "/videos?limit=" + limit + "&page=" + page + "&fields=id,title,description,created_time,tags";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }

    public Map<String, Object> getCaptions(String videoId) {
        String url = "https://api.dailymotion.com/video/" + videoId + "/subtitles?fields=id,url,language";
        return restTemplate.getForObject(url, Map.class);
    }


    // --- Mapeo a modelos de VideoMiner ---

    public Channel buildChannel(String userId, Integer maxVideos, Integer maxComments) {
        DailymotionChannel dmChannel = getChannel(userId);
        if (dmChannel == null) return null;

        // Montamos los datos base del canal
        Channel channel = new Channel();
        channel.setId(dmChannel.getId());
        channel.setName(dmChannel.getScreenName()); // Usamos el getScreenname() de tu modelo o name si lo mapeaste así
        channel.setDescription(dmChannel.getDescription());
        channel.setCreatedTime(dmChannel.getCreatedTime() != null ? dmChannel.getCreatedTime().toString() : null);

        // Procesamos los videos aparte para que esto quede limpio
        channel.setVideos(this.mapVideos(userId, maxVideos, dmChannel.getScreenName(), maxComments));

        return channel;
    }

    private List<Video> mapVideos(String userId, Integer maxVideos, String authorName, Integer maxComments) {
        DailymotionVideoList dmVideoList = getVideosFromUser(userId, maxVideos, 1);
        List<Video> videos = new ArrayList<>();

        if (dmVideoList != null && dmVideoList.getList() != null) {
            for (DailymotionVideo dmVideo : dmVideoList.getList()) {
                videos.add(this.buildVideo(dmVideo, authorName, userId, maxComments));
            }
        }
        return videos;
    }

        private Video buildVideo(DailymotionVideo dmVideo, String authorName, String userId, Integer maxComments){
            Video video = new Video();
            video.setId(dmVideo.getId());
            video.setName(dmVideo.getTitle());
            video.setDescription(dmVideo.getDescription());
            video.setReleaseTime(dmVideo.getCreatedTime() != null ? dmVideo.getCreatedTime().toString() : null);

            User author = new User();

            author.setId((long) userId.hashCode()); //Se necesita convertir el nombre que introduzcamos en un id que es único.
            author.setName(authorName);

            // Ahora sí podemos usar el userId para construir el link sin errores
            author.setUser_link("https://www.dailymotion.com/" + userId);
            author.setPicture_link("");
            video.setAuthor(author);
            List<Comment> comments = new ArrayList<>();

            // 1. Comprobamos que el vídeo tenga tags
            if (dmVideo.getTags() != null) {
                int contador = 0; // Llevamos la cuenta de cuántos comentarios hemos metido

                // 2. Recorremos los tags uno a uno
                for (String tag : dmVideo.getTags()) {

                    // 3. Si ya hemos metido el máximo de comentarios que nos pedían, paramos el bucle
                    if (contador >= maxComments) {
                        break;
                    }

                    // 4. Creamos el comentario usando el tag
                    Comment comment = new Comment();
                    comment.setId(userId); // Un ID cualquiera
                    comment.setText(tag); // El texto es la etiqueta
                    comment.setCreatedOn(video.getReleaseTime());

                    comments.add(comment);
                    contador++; // Sumamos 1 al contador
                }
            }

            video.setComments(comments);

            List<Caption> captions = new ArrayList<>();

            // 1. Pedimos la respuesta a la API (que ahora es un Map)
            Map<String, Object> subsResponse = getCaptions(dmVideo.getId());

            // 2. Comprobamos que no sea nula y que tenga la lista dentro
            if (subsResponse != null && subsResponse.get("list") != null) {

                // 3. Extraemos la lista directamente
                List<Map<String, String>> subtitlesList = (List<Map<String, String>>) subsResponse.get("list");

                // 4. Hacemos el bucle como con los comments
                for (Map<String, String> sub : subtitlesList) {
                    Caption caption = new Caption();

                    caption.setId(sub.get("id"));
                    caption.setLanguage(sub.get("language"));
                    caption.setName(sub.get("url")); // Recordamos meter url en el name

                    captions.add(caption);
                }
            }
            video.setCaptions(captions);

            return video;
        }
    }