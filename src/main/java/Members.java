import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tag",
        "name",
        "role",
        "level",
        "donated",
        "received",
        "rank",
        "trophies",
        "league",
        "townHall",
        "weight",
        "inWar"
})
public class Members {

    @JsonProperty("tag")
    private String tag;
    @JsonProperty("name")
    private String name;
    @JsonProperty("role")
    private String role;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("donated")
    private Integer donated;
    @JsonProperty("received")
    private Integer received;
    @JsonProperty("rank")
    private Integer rank;
    @JsonProperty("trophies")
    private Integer trophies;
    @JsonProperty("league")
    private String league;
    @JsonProperty("townHall")
    private Integer townHall;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("inWar")
    private Boolean inWar;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

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

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    @JsonProperty("donated")
    public Integer getDonated() {
        return donated;
    }

    @JsonProperty("donated")
    public void setDonated(Integer donated) {
        this.donated = donated;
    }

    @JsonProperty("received")
    public Integer getReceived() {
        return received;
    }

    @JsonProperty("received")
    public void setReceived(Integer received) {
        this.received = received;
    }

    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @JsonProperty("trophies")
    public Integer getTrophies() {
        return trophies;
    }

    @JsonProperty("trophies")
    public void setTrophies(Integer trophies) {
        this.trophies = trophies;
    }

    @JsonProperty("league")
    public String getLeague() {
        return league;
    }

    @JsonProperty("league")
    public void setLeague(String league) {
        this.league = league;
    }

    @JsonProperty("townHall")
    public Integer getTownHall() {
        return townHall;
    }

    @JsonProperty("townHall")
    public void setTownHall(Integer townHall) {
        this.townHall = townHall;
    }

    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @JsonProperty("inWar")
    public Boolean getInWar() {
        return inWar;
    }

    @JsonProperty("inWar")
    public void setInWar(Boolean inWar) {
        this.inWar = inWar;
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