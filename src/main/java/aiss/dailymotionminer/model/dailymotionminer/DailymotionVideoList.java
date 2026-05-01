package aiss.dailymotionminer.model.dailymotionminer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "list"
})
@Generated("jsonschema2pojo")
public class DailymotionVideoList {

    // CORRECCIÓN: Ahora es una lista de DailymotionVideo, no de DailymotionVideoList
    @JsonProperty("list")
    private List<DailymotionVideo> list;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("list")
    public List<DailymotionVideo> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(List<DailymotionVideo> list) {
        this.list = list;
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