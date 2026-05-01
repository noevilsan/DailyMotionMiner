package aiss.dailymotionminer.model.dailymotionminer;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DailymotionUser {

    @JsonProperty("id")
    private String id;

    @JsonProperty("screenname") // Dailymotion devuelve el nombre aquí
    private String name;

    @JsonProperty("avatar_240_url") // Esta es la URL de la foto en Dailymotion
    private String pictureLink;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("screenname")
    public String getName() {
        return name;
    }

    @JsonProperty("screenname")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("avatar_240_url")
    public String getPictureLink() {
        return pictureLink;
    }

    @JsonProperty("avatar_240_url")
    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}