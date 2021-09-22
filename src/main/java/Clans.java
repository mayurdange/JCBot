import com.fasterxml.jackson.annotation.*;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tag",
        "name",
        "level",
        "points",
        "type",
        "location",
        "requiredTrophies",
        "warFrequency",
        "winStreak",
        "wins",
        "ties",
        "losses",
        "isWarLogPublic",
        "image",
        "description",
        "th14Count",
        "th13Count",
        "th12Count",
        "th11Count",
        "th10Count",
        "th9Count",
        "th8Count",
        "thLowCount",
        "estimatedWeight"
})
@Generated("jsonschema2pojo")
public class Clans {

    @JsonProperty("tag")
    private String tag;
    @JsonProperty("name")
    private String name;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("points")
    private Integer points;
    @JsonProperty("type")
    private String type;
    @JsonProperty("location")
    private String location;
    @JsonProperty("requiredTrophies")
    private Integer requiredTrophies;
    @JsonProperty("warFrequency")
    private String warFrequency;
    @JsonProperty("winStreak")
    private Integer winStreak;
    @JsonProperty("wins")
    private Integer wins;
    @JsonProperty("ties")
    private Integer ties;
    @JsonProperty("losses")
    private Integer losses;
    @JsonProperty("isWarLogPublic")
    private Boolean isWarLogPublic;
    @JsonProperty("image")
    private String image;
    @JsonProperty("description")
    private String description;
    @JsonProperty("th14Count")
    private Integer th14Count;
    @JsonProperty("th13Count")
    private Integer th13Count;
    @JsonProperty("th12Count")
    private Integer th12Count;
    @JsonProperty("th11Count")
    private Integer th11Count;
    @JsonProperty("th10Count")
    private Integer th10Count;
    @JsonProperty("th9Count")
    private Integer th9Count;
    @JsonProperty("th8Count")
    private Integer th8Count;
    @JsonProperty("thLowCount")
    private Integer thLowCount;
    @JsonProperty("estimatedWeight")
    private Integer estimatedWeight;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    @JsonProperty("tag")
    public void setTag(String tag) {
        this.tag = tag;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    @JsonProperty("points")
    public Integer getPoints() {
        return points;
    }

    @JsonProperty("points")
    public void setPoints(Integer points) {
        this.points = points;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("requiredTrophies")
    public Integer getRequiredTrophies() {
        return requiredTrophies;
    }

    @JsonProperty("requiredTrophies")
    public void setRequiredTrophies(Integer requiredTrophies) {
        this.requiredTrophies = requiredTrophies;
    }

    @JsonProperty("warFrequency")
    public String getWarFrequency() {
        return warFrequency;
    }

    @JsonProperty("warFrequency")
    public void setWarFrequency(String warFrequency) {
        this.warFrequency = warFrequency;
    }

    @JsonProperty("winStreak")
    public Integer getWinStreak() {
        return winStreak;
    }

    @JsonProperty("winStreak")
    public void setWinStreak(Integer winStreak) {
        this.winStreak = winStreak;
    }

    @JsonProperty("wins")
    public Integer getWins() {
        return wins;
    }

    @JsonProperty("wins")
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    @JsonProperty("ties")
    public Integer getTies() {
        return ties;
    }

    @JsonProperty("ties")
    public void setTies(Integer ties) {
        this.ties = ties;
    }

    @JsonProperty("losses")
    public Integer getLosses() {
        return losses;
    }

    @JsonProperty("losses")
    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    @JsonProperty("isWarLogPublic")
    public Boolean getIsWarLogPublic() {
        return isWarLogPublic;
    }

    @JsonProperty("isWarLogPublic")
    public void setIsWarLogPublic(Boolean isWarLogPublic) {
        this.isWarLogPublic = isWarLogPublic;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("th14Count")
    public Integer getTh14Count() {
        return th14Count;
    }

    @JsonProperty("th14Count")
    public void setTh14Count(Integer th14Count) {
        this.th14Count = th14Count;
    }

    @JsonProperty("th13Count")
    public Integer getTh13Count() {
        return th13Count;
    }

    @JsonProperty("th13Count")
    public void setTh13Count(Integer th13Count) {
        this.th13Count = th13Count;
    }

    @JsonProperty("th12Count")
    public Integer getTh12Count() {
        return th12Count;
    }

    @JsonProperty("th12Count")
    public void setTh12Count(Integer th12Count) {
        this.th12Count = th12Count;
    }

    @JsonProperty("th11Count")
    public Integer getTh11Count() {
        return th11Count;
    }

    @JsonProperty("th11Count")
    public void setTh11Count(Integer th11Count) {
        this.th11Count = th11Count;
    }

    @JsonProperty("th10Count")
    public Integer getTh10Count() {
        return th10Count;
    }

    @JsonProperty("th10Count")
    public void setTh10Count(Integer th10Count) {
        this.th10Count = th10Count;
    }

    @JsonProperty("th9Count")
    public Integer getTh9Count() {
        return th9Count;
    }

    @JsonProperty("th9Count")
    public void setTh9Count(Integer th9Count) {
        this.th9Count = th9Count;
    }

    @JsonProperty("th8Count")
    public Integer getTh8Count() {
        return th8Count;
    }

    @JsonProperty("th8Count")
    public void setTh8Count(Integer th8Count) {
        this.th8Count = th8Count;
    }

    @JsonProperty("thLowCount")
    public Integer getThLowCount() {
        return thLowCount;
    }

    @JsonProperty("thLowCount")
    public void setThLowCount(Integer thLowCount) {
        this.thLowCount = thLowCount;
    }

    @JsonProperty("estimatedWeight")
    public Integer getEstimatedWeight() {
        return estimatedWeight;
    }

    @JsonProperty("estimatedWeight")
    public void setEstimatedWeight(Integer estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
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