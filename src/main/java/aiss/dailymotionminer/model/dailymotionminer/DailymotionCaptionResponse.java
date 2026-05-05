package aiss.dailymotionminer.model.dailymotionminer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailymotionCaptionResponse {

    @JsonProperty("list")
    private List<DailymotionCaption> list;

    public List<DailymotionCaption> getList() { return list; }
    public void setList(List<DailymotionCaption> list) { this.list = list; }
}