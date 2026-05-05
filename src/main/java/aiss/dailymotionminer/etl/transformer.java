package aiss.dailymotionminer.etl;

import aiss.dailymotionminer.model.dailymotionminer.*;
import aiss.dailymotionminer.model.videominer.*;
import aiss.dailymotionminer.service.DailymotionService; // Importante
import java.util.ArrayList;
import java.util.List;

public class transformer {

    // Cambiado: Ahora recibe el "service" como parámetro
    public static Channel buildChannel(String userId, Integer maxVideos, Integer maxComments, DailymotionService service) {
        // Llamada corregida usando la instancia 'service'
        DailymotionChannel dmChannel = service.getChannel(userId);
        if (dmChannel == null) return null;

        Channel channel = new Channel();
        channel.setId(dmChannel.getId());
        channel.setName(dmChannel.getScreenName());
        channel.setDescription(dmChannel.getDescription());
        channel.setCreatedTime(dmChannel.getCreatedTime() != null ? dmChannel.getCreatedTime().toString() : null);

        // Pasamos también el service a mapVideos
        channel.setVideos(mapVideos(userId, maxVideos, dmChannel.getScreenName(), maxComments, service));

        return channel;
    }

    private static List<Video> mapVideos(String userId, Integer maxVideos, String authorName, Integer maxComments, DailymotionService service) {
        // Llamada corregida usando 'service'
        DailymotionVideoList dmVideoList = service.getVideosFromUser(userId, maxVideos, 1);
        List<Video> videos = new ArrayList<>();

        if (dmVideoList != null && dmVideoList.getList() != null) {
            for (DailymotionVideo dmVideo : dmVideoList.getList()) {
                videos.add(buildVideo(dmVideo, authorName, userId, maxComments));
            }
        }
        return videos;
    }

    private static Video buildVideo(DailymotionVideo dmVideo, String authorName, String userId, Integer maxComments){
        Video video = new Video();
        video.setId(dmVideo.getId());
        video.setName(dmVideo.getTitle());
        video.setDescription(dmVideo.getDescription());
        video.setReleaseTime(dmVideo.getCreatedTime() != null ? dmVideo.getCreatedTime().toString() : null);

        User author = new User();
        author.setId((long) userId.hashCode());
        author.setName(authorName);
        author.setUser_link("https://www.dailymotion.com/" + userId);
        video.setAuthor(author);

        List<Comment> comments = new ArrayList<>();
        if (dmVideo.getTags() != null) {
            int contador = 0;
            for (String tag : dmVideo.getTags()) {
                if (contador >= maxComments) break;
                Comment comment = new Comment();
                comment.setId(userId + "-" + contador); // ID único sugerido
                comment.setText(tag);
                comment.setCreatedOn(video.getReleaseTime());
                comments.add(comment);
                contador++;
            }
        }
        video.setComments(comments);
        video.setCaptions(new ArrayList<>()); // Evita null en captions
        return video;
    }
}