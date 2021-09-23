import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class MessageListener extends ListenerAdapter {
    private static final Map<String, Clans> allClans = new HashMap<>();
    private static final RestTemplate restTemplate = new RestTemplate();
    private final Map<Pair<Integer, Integer>, Integer> weightRange = new HashMap<>();
    private final Map<Integer, String> emojiMap = new HashMap<>();

    public MessageListener() {
        weightRange.put(Pair.of(Integer.MAX_VALUE / 1000, 131), 14);
        weightRange.put(Pair.of(131, 121), 13);
        weightRange.put(Pair.of(121, 111), 12);
        weightRange.put(Pair.of(111, 91), 11);
        weightRange.put(Pair.of(91, 71), 10);
        weightRange.put(Pair.of(71, 56), 9);
        weightRange.put(Pair.of(56, 0), 8);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Clans[] clansOp = mapper.readValue(new File("src/main/resources/Clans.json"), Clans[].class);
            for (Clans clan : clansOp) {
                allClans.put(clan.getTag(), clan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        emojiMap.put(14, "<:th_14:881852620485050369>");
        emojiMap.put(13, "<:th_13:881852471255900181>");
        emojiMap.put(12, "<:th_12:881852800248741948>");
        emojiMap.put(11, "<:th_11:881854521003900999>");
        emojiMap.put(10, "<:th_10:881854603023503360>");
        emojiMap.put(9, "<:th_9:881854733151793152>");
    }

    public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createLight(args[0], EnumSet.noneOf(GatewayIntent.class))
                .addEventListeners(new MessageListener()).build();
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(new CommandData("comp", "gets the TH composition of the clan")
                .addOptions(new OptionData(STRING, "tag", "Clan tag. if blank, use default clans").setRequired(false))
        );
        commands.queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null)// Only accept commands from guilds
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
        String clanTagstr = event.getOption("tag") == null ? "#9JUVCV0L" : event.getOption("tag").getAsString();
        event.deferReply().queue();
        String responseMessage = calculateThComp(clanTagstr);
        WebhookMessageAction<Message> messageWebhookMessageAction = event.getHook().sendMessage(responseMessage);
        try {
            URL url = new URL(allClans.get(clanTagstr).getImage());
            BufferedImage img = ImageIO.read(url);
            File file = new File("temp.png"); // change the '.jpg' to whatever extension the image has
            ImageIO.write(img, "png", file); // again, change 'jpg' to the correct extension
            messageWebhookMessageAction = messageWebhookMessageAction.addFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageWebhookMessageAction.queue();
    }

    private String calculateThComp(String clanTagstr) {
        ResponseEntity<List<Members>> responseMembers = getMemberInfo(clanTagstr);
        return getResponseString(responseMembers.getBody(), clanTagstr);
    }

    @NotNull String getResponseString(List<Members> members, String clanTagstr) {
        Map<Integer, Integer> actual = new HashMap<>();
        Map<Integer, Integer> weight = new HashMap<>();
        List<Members> missingWeights = new ArrayList<>();
        for (Members m : members) {
            Integer thLvl = actual.getOrDefault(m.getTownHall(), 0);
            actual.put(m.getTownHall(), ++thLvl);
            if (m.getWeight() == 0) {
                missingWeights.add(m);
            } else {
                for (Map.Entry<Pair<Integer, Integer>, Integer> entry : weightRange.entrySet()) {
                    Pair<Integer, Integer> p = entry.getKey();
                    Integer t = entry.getValue();
                    int memberWeight = m.getWeight() / 1000;
                    if (memberWeight <= p.getLeft() && memberWeight > p.getRight()) {
                        weight.compute(t, (k, v) -> v == null ? 1 : v + 1);
                        break;
                    }
                }
            }
        }
        StringBuilder s = new StringBuilder("TH count for clan **").append(allClans.get(clanTagstr).getName()).append("**\nTH\tlvl\tweight\n");
        for (int i = 14; i > 8; i--) {
            s.append(String.format("%5s %5d %5d\n", emojiMap.get(i), actual.getOrDefault(i, 0), weight.getOrDefault(i, 0)));
        }
        if (!missingWeights.isEmpty()) {
            s.append("\nweight missing for ")
                    .append(missingWeights.stream().map(Members::getName).collect(Collectors.joining(", ")))
                    .append("\n Please update the war weight at https://fwastats.com/Clan/").append(clanTagstr.substring(1))
                    .append("/Weight");
        }
        return s.toString();
    }

    private ResponseEntity<List<Members>> getMemberInfo(String clanTagstr) {
        ResponseEntity<List<Members>> responseMembers =
                restTemplate.exchange("https://fwastats.com/Clan/" + clanTagstr.substring(1) + "/Members.json",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Members>>() {
                        });
        return responseMembers;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        System.out.println(event.getMessage().getContentRaw());
    }
}