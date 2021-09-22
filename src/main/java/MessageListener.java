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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
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
        weightRange.put(Pair.of(139000, 116000), 14);
        weightRange.put(Pair.of(116000, 119000), 13);
        weightRange.put(Pair.of(119000, 109000), 12);
        weightRange.put(Pair.of(109000, 86000), 11);
        weightRange.put(Pair.of(86000, 69000), 10);
        weightRange.put(Pair.of(69000, 0), 9);
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
                        .addOptions(new OptionData(STRING, "content", "Clan tag. if blank, use default clans")
                                .setRequired(false))
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

    private void calcStats(SlashCommandEvent event) {
        OptionMapping clanTag = event.getOption("content");
        event.reply("calculating..").queue();
        if (allClans.isEmpty()) {
            ResponseEntity<List<Clans>> responseClans =
                    restTemplate.exchange("https://fwastats.com/Clans.json",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Clans>>() {
                            });
            responseClans.getBody().forEach(c -> {
                allClans.put(c.getTag(), c);
            });
        }
        Clans clan = allClans.get(clanTag == null ? "#9JUVCV0L" : clanTag);

        ResponseEntity<List<Members>> responseMembers =
                restTemplate.exchange("https://fwastats.com/Clan/" + clan.getTag().substring(1) + "/Members.json",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Members>>() {
                        });

        Map<Integer, Integer> actual = new HashMap<>();
        Map<Integer, Integer> weight = new HashMap<>();
        responseMembers.getBody().forEach(m -> {
            Integer thLvl = actual.getOrDefault(m.getTownHall(), 0);
            actual.put(m.getTownHall(), ++thLvl);

            weightRange.forEach((p, t) -> {
                if (m.getWeight() > p.getLeft() && m.getWeight() < p.getRight()) {
                    Integer w = weight.getOrDefault(t, 0);
                    weight.put(t, ++w);
                }
            });
        });

        String act = "Actual: ";
        String wt = "Weight: ";
        String th = "TH: ";
        for (int i = 14; i > 7; i--) {
            Integer act1 = actual.getOrDefault(i, 0);
            act = act + "/" + act1;
            Integer wt1 = weight.getOrDefault(i, 0);
            wt = wt + "/" + wt1;
            th = th + "/" + i;
        }
        event.getMessageChannel().sendMessage(th + "\n" + act + "\n" + wt).queue(); // This requires no permissions!
    }
}