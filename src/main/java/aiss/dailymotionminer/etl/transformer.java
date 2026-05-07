package aiss.dailymotionminer.etl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import aiss.dailymotionminer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Importamos TUS modelos basándonos en la captura y en tu código anterior
import aiss.dailymotionminer.model.dailymotionminer.*;
import aiss.dailymotionminer.model.videominer.*;

@Component
public class transformer {

    @Autowired
    DailymotionService service;


    public Channel buildChannel(String userId, Integer maxVideos, Integer maxComments) {
        DailymotionChannel dmChannel = service.getChannel(userId);
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
        DailymotionVideoList dmVideoList = service.getVideosFromUser(userId, maxVideos, 1);
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

        // Captions: se leen de subtitles_urls embebido en el vídeo (no requiere auth)
        List<Caption> captions = new ArrayList<>();
        Map<String, String> subtitlesUrls = dmVideo.getSubtitlesUrls();
        if (subtitlesUrls != null) {
            for (Map.Entry<String, String> entry : subtitlesUrls.entrySet()) {
                Caption caption = new Caption();
                caption.setId(dmVideo.getId() + "-" + entry.getKey()); // id único por vídeo+idioma
                caption.setLanguage(entry.getKey());                    // clave = código de idioma
                caption.setName(entry.getValue());                      // valor = URL del fichero
                captions.add(caption);
            }
        }
        video.setCaptions(captions);

        return video;
    }
}