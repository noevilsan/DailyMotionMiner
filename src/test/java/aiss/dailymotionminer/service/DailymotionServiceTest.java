package aiss.dailymotionminer.service;

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
        // Usamos una cuenta pública conocida de Dailymotion
        DailymotionChannel channel = service.getChannel("redbull");

        assertNotNull(channel, "El canal no debería ser nulo");
        assertNotNull(channel.getId(), "El ID del canal no debería ser nulo");
        System.out.println("Canal recuperado: " + channel.getName());
    }

    @Test
    @DisplayName("Probar que recupera vídeos de una cuenta en Dailymotion")
    void getVideosFromUserTest() {
        // 1. Llamamos al método con un usuario, límite de 10 vídeos, página 1
        DailymotionVideoList videoList = service.getVideosFromUser("redbull", 10, 1);

        // 2. Comprobaciones de seguridad (Assertions)
        assertNotNull(videoList, "La respuesta no debería ser nula");
        assertNotNull(videoList.getList(), "La lista 'list' de vídeos no debería ser nula");

        // 3. Verificamos que realmente ha traído vídeos
        assertFalse(videoList.getList().isEmpty(), "La lista de vídeos no debería estar vacía");

        // 4. Print de control en la consola de IntelliJ
        System.out.println("Vídeos encontrados: " + videoList.getList().size());
        System.out.println("Nombre del primer vídeo: " + videoList.getList().get(0).getName());
    }

    @Test
    @DisplayName("Probar que recupera los datos de un vídeo específico")
    void getVideoTest() {
        // 1. Sacamos primero un vídeo real del canal para asegurar que existe
        DailymotionVideoList videoList = service.getVideosFromUser("redbull", 1, 1);
        assertFalse(videoList.getList().isEmpty(), "No hay vídeos para hacer la prueba");
        String realVideoId = videoList.getList().get(0).getId();

        // 2. Pedimos los datos individuales de ese vídeo
        DailymotionVideo video = service.getVideo(realVideoId);

        // 3. Comprobaciones
        assertNotNull(video, "El vídeo no debería ser nulo");
        assertEquals(realVideoId, video.getId(), "El ID recuperado debe coincidir con el solicitado");
        System.out.println("Datos recuperados del vídeo: " + video.getName());
    }

    @Test
    @DisplayName("Probar que recupera subtítulos de un vídeo (Captions)")
    void getCaptionsTest() {
        // 1. Volvemos a usar el ID de un vídeo real
        DailymotionVideoList videoList = service.getVideosFromUser("redbull", 1, 1);
        assertFalse(videoList.getList().isEmpty(), "No hay vídeos para hacer la prueba");
        String realVideoId = videoList.getList().get(0).getId();

        // 2. Pedimos los subtítulos (recuerda que en el service lo dejamos devolviendo Object)
        Object captions = service.getCaptions(realVideoId);

        // 3. Comprobaciones
        assertNotNull(captions, "El objeto de subtítulos no debería ser nulo");
        System.out.println("Subtítulos recuperados para el vídeo: " + realVideoId);
    }
}