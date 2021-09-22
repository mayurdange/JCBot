import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class MessageListener extends ListenerAdapter {
    private static Map<String, Clans> allClans = new HashMap<>();
    private static RestTemplate restTemplate = new RestTemplate();
    private Map<Pair<Integer, Integer>, Integer> weightRange = new HashMap<>();

    public MessageListener() {
        weightRange.put(Pair.of(Integer.MAX_VALUE / 1000, 131), 14);
        weightRange.put(Pair.of(131, 121), 13);
        weightRange.put(Pair.of(121, 111), 12);
        weightRange.put(Pair.of(111, 91), 11);
        weightRange.put(Pair.of(91, 71), 10);
        weightRange.put(Pair.of(71, 56), 9);
        weightRange.put(Pair.of(56, 42), 8);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Clans[] clansOp = mapper.readValue(new File("src/main/resources/Clans.json"), Clans[].class);
            for (Clans clan: clansOp) {
                allClans.put(clan.getTag(),clan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createLight(args[0], EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
                .addEventListeners(new MessageListener())
                .build();
        // These commands take up to an hour to be activated after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                new CommandData("comp", "gets the TH composition of the clan")
                        .addOptions(new OptionData(STRING, "tag", "Clan tag. if blank, use default clans")
                                .setRequired(false))
        );

        commands.addCommands(
                new CommandData("abc", "description")
        );
        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName()) {
            case "comp":
                calcStats(event); // content is required so no null-check here
                break;
            default:
                System.out.println("unknown command " + event);
        }
    }

    public void calcStats(SlashCommandEvent event) {
        OptionMapping clanTag = event.getOption("tag");
        //event.reply("calculating..").queue();
        event.deferReply().queue();
        String responseMessage = calculateThComp(clanTag);
        //event.getMessageChannel().sendMessage(responseMessage).queue(); // This requires no permissions!
        //event.reply(responseMessage).queue();
        event.getHook().sendMessage(responseMessage).queue();
    }

    @NotNull
    private String calculateThComp(OptionMapping clanTag) {
        String clanTagstr =  clanTag== null ? "#9JUVCV0L" : clanTag.getAsString();
        System.out.println(clanTagstr);
        ResponseEntity<List<Members>> responseMembers = getMemberInfo(clanTagstr);
        String responseMessage = getResponseString(responseMembers.getBody(),clanTagstr);
        return responseMessage;
    }

    @NotNull String getResponseString(List<Members> members,String clanTagstr) {
        Map<Integer, Integer> actual = new HashMap<>();
        Map<Integer, Integer> weight = new HashMap<>();
        for (Members m : members) {
            Integer thLvl = actual.getOrDefault(m.getTownHall(), 0);
            actual.put(m.getTownHall(), ++thLvl);

            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : weightRange.entrySet()) {
                Pair<Integer, Integer> p = entry.getKey();
                Integer t = entry.getValue();
                Integer memberWeight = m.getWeight() / 1000;
                if (memberWeight < p.getLeft() && memberWeight > p.getRight()) {
                    Integer w = weight.getOrDefault(t, 0);
                    weight.put(t, ++w);
                    break;
                }
            }
        }

        System.out.println("actual" + actual + "\nweight" + weight);

        String act = "\nActual:\t";
        String wt = "\nWeight:  ";
        String th = "\nTH:\t\t\t<:th_14:881852620485050369>\t<:th_13:881852471255900181>\t<:th_12:881852800248741948>\t<:th_11:881854521003900999>\t<:th_10:881854603023503360>\t<:th_9:881854733151793152>";
        for (int i = 14; i > 7; i--) {
            Integer act1 = actual.get(i);
            act = act + "\t" + formattedWeight(act1) + "  ";
            Integer wt1 = weight.get(i);
            wt = wt + "\t" + formattedWeight(wt1 ) + "  ";
        }
        String responseMessage = "TH count for clan *"+allClans.get(clanTagstr).getName()+"*"+th + act + wt;
        return responseMessage;
    }

    private String formattedWeight(Integer i) {
        if (i==null){
            return "  ";
        }else if (i<10){
            return " "+i;
        }
        return  String.valueOf(i);
    }

    @NotNull
    private ResponseEntity<List<Members>> getMemberInfo(String clanTagstr) {
        ResponseEntity<List<Members>> responseMembers =
                restTemplate.exchange("https://fwastats.com/Clan/" + clanTagstr.substring(1) + "/Members.json",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Members>>() {
                        });
        return responseMembers;
    }

    private void populateClanInfo() {
        ResponseEntity<List<Clans>> responseClans =
                restTemplate.exchange("https://fwastats.com/Clans.json",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Clans>>() {
                        });
        responseClans.getBody().forEach(c -> {
            allClans.put(c.getTag(), c);
        });
    }
}