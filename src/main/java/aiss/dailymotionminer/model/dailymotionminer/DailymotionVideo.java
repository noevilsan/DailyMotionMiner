package aiss.dailymotionminer.model.dailymotionminer;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class DailymotionVideo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title") // Dailymotion lo llama "title"
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_time") // Dailymotion lo llama "created_time"
    private Integer releaseTime;

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

    @JsonProperty("title")
    public String getName() {
        return name;
    }

    @JsonProperty("title")
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
    public Integer getReleaseTime() {
        return releaseTime;
    }

    @JsonProperty("created_time")
    public void setReleaseTime(Integer releaseTime) {
        this.releaseTime = releaseTime;
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