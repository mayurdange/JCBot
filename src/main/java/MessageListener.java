import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class MessageListener extends ListenerAdapter {
    CompCommandProcessor compCommandProcessor=new CompCommandProcessor();
    public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createLight(args[0], EnumSet.noneOf(GatewayIntent.class))
                .addEventListeners(new MessageListener()).build();
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(new CommandData("comp", "gets the TH composition of the clan")
                .addOptions(new OptionData(STRING, "tag", "Clan tag. if blank, use default clans").setRequired(false))
        );
        commands.addCommands(new CommandData("jctest", "try out the bot's command (only for ⌜Co⌟ ⌯ Mayur's testing)")
                .addOptions(new OptionData(STRING, "tag", "option if any").setRequired(false))
        );
        commands.queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        try {
            if (event.getGuild() == null)// Only accept commands from guilds
                return;
            switch (event.getName()) {
                case "comp":
                    System.out.println(event.getMember().getUser().getAsTag()+" requested comp for "+event.getOptions());
                    compCommandProcessor.calcStats(event); // content is required so no null-check here
                    break;
                case "jctest":
                    if ("mayur#6158".equals(event.getMember().getUser().getAsTag())) {
                        new TestCommandProcessor().calcStats(event);
                    } else {
                        System.out.println(event.getMember().getUser().getAsTag()+" tried to run jctest command");
                        event.reply("this command is only reserved for ⌜Co⌟ ⌯ Mayur for testing purposes, only he can run this").queue();
                    }
                    break;
                default:
                    System.out.println("unknown command " + event);
            }
        }catch(Exception e){
            event.reply("something went wrong :poop:").queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        System.out.println(event.getMessage().getContentRaw());
    }
}