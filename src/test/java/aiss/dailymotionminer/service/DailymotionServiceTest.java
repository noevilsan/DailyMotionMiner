package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.dailymotionminer.DailymotionCaptionResponse; // Importante añadirlo
import aiss.dailymotionminer.model.dailymotionminer.DailymotionChannel;
import aiss.dailymotionminer.model.dailymotionminer.DailymotionVideo;
import aiss.dailymotionminer.model.dailymotionminer.DailymotionVideoList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DailymotionServiceTest {

    @Autowired
    DailymotionService service;

    @Test
    @DisplayName("Probar que recupera el Canal de Dailymotion")
    void getChannelTest() {
        DailymotionChannel channel = service.getChannel("redbull");
        assertNotNull(channel, "El canal no debería ser nulo");
        assertNotNull(channel.getId(), "El ID del canal no debería ser nulo");
        System.out.println("Canal recuperado: " + channel.getScreenName());
    }

    @Test
    @DisplayName("Probar que recupera vídeos de una cuenta en Dailymotion")
    void getVideosFromUserTest() {
        DailymotionVideoList videoList = service.getVideosFromUser("redbull", 10, 1);
        assertNotNull(videoList, "La respuesta no debería ser nula");
        assertNotNull(videoList.getList(), "La lista 'list' de vídeos no debería ser nula");
        assertFalse(videoList.getList().isEmpty(), "La lista de vídeos no debería estar vacía");
        System.out.println("Vídeos encontrados: " + videoList.getList().size());
        System.out.println("Nombre del primer vídeo: " + videoList.getList().get(0).getTitle());
    }

    @Test
    @DisplayName("Probar que recupera los datos de un vídeo específico")
    void getVideoTest() {
        DailymotionVideoList videoList = service.getVideosFromUser("redbull", 1, 1);
        assertFalse(videoList.getList().isEmpty(), "No hay vídeos para hacer la prueba");
        String realVideoId = videoList.getList().get(0).getId();

        DailymotionVideo video = service.getVideo(realVideoId);
        assertNotNull(video, "El vídeo no debería ser nulo");
        assertEquals(realVideoId, video.getId(), "El ID recuperado debe coincidir con el solicitado");
        System.out.println("Datos recuperados del vídeo: " + video.getTitle());
    }

    @Test
    @DisplayName("Probar que recupera subtítulos de un vídeo (Captions)")
    void getCaptionsTest() {
        // 1. Obtenemos un vídeo real (Mejor usar 'ted' para asegurar que existan subtítulos)
        DailymotionVideoList videoList = service.getVideosFromUser("ted", 1, 1);
        assertFalse(videoList.getList().isEmpty(), "No hay vídeos para hacer la prueba");
        String realVideoId = videoList.getList().get(0).getId();

        // 2. CORRECCIÓN: El método se llama getCaptions y devuelve DailymotionCaptionResponse
        DailymotionCaptionResponse captions = service.getCaptions(realVideoId);

        // 3. Comprobaciones
        assertNotNull(captions, "El objeto de subtítulos no debería ser nulo");
        // Opcional: imprimir cuántos subtítulos se han encontrado
        if (captions.getList() != null) {
            System.out.println("Subtítulos encontrados: " + captions.getList().size());
        }
        System.out.println("Respuesta de subtítulos recibida para el vídeo: " + realVideoId);
    }
}