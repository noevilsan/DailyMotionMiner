package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.dailymotionminer.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class DailymotionService {

    private final RestTemplate restTemplate = new RestTemplate();

    private String cachedToken = null;

    // ── TOKEN ────────────────────────────────────────────────────────────────

    private String getAccessToken() {
        if (cachedToken != null) return cachedToken;

        String url = "https://api.dailymotion.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", "6f92bb92e97a86877c38");       // ← pon tu client_id
        body.add("client_secret", "32edf840e6e95ea37aa5a5e986886f628dc123e4"); // ← pon tu client_secret

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        Map<?, ?> response = restTemplate.postForObject(url, request, Map.class);

        if (response != null && response.get("access_token") != null) {
            cachedToken = (String) response.get("access_token");
        }

        return cachedToken;
    }

    // ── CHANNEL ──────────────────────────────────────────────────────────────

    public DailymotionChannel getChannel(String userId) {
        String url = "https://api.dailymotion.com/user/" + userId
                + "?fields=id,screenname,description,created_time,avatar_240_url";
        return restTemplate.getForObject(url, DailymotionChannel.class);
    }

    // ── VIDEO ────────────────────────────────────────────────────────────────

    public DailymotionVideo getVideo(String videoId) {
        String url = "https://api.dailymotion.com/video/" + videoId
                + "?fields=id,title,description,created_time";
        return restTemplate.getForObject(url, DailymotionVideo.class);
    }

    public DailymotionVideoList getVideosFromUser(String userId, Integer limit, Integer page) {
        String url = "https://api.dailymotion.com/user/" + userId + "/videos"
                + "?limit=" + limit
                + "&page=" + page
                + "&fields=id,title,description,created_time,tags";
        return restTemplate.getForObject(url, DailymotionVideoList.class);
    }

    // ── CAPTIONS ─────────────────────────────────────────────────────────────

    public DailymotionCaptionResponse getCaptions(String videoId) {
        try {
            String token = getAccessToken();
            String url = "https://api.dailymotion.com/video/" + videoId
                    + "/subtitles?fields=id,url,language";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<DailymotionCaptionResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, DailymotionCaptionResponse.class
            );
            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error subtítulos para vídeo " + videoId + ": " + e.getMessage());
            return null;
        }
    }
}