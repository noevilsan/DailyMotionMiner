package aiss.dailymotionminer.model.dailymotionminer;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DailymotionChannel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("screenname")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_time")
    private Integer createdTime;

    @JsonProperty("avatar_240_url") // <--- EL BOLSILLO NUEVO
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

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("created_time")
    public Integer getCreatedTime() {
        return createdTime;
    }

    @JsonProperty("created_time")
    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
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