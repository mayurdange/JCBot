import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class MessageListener extends ListenerAdapter {
    public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createLight(args[0], EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
                .addEventListeners(new MessageListener())
                .build();
        // These commands take up to an hour to be activated after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                new CommandData("compo", "gets the TH composition of the clan")
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
            case "compo":
                calcStats(event); // content is required so no null-check here
                break;
            default:
                System.out.println("unknown command " + event);
        }
    }

    private void calcStats(SlashCommandEvent event) {
        OptionMapping clanTag = event.getOption("content");
        //get https://fwastats.com/Clans.json
        //match hashtag
        //get https://fwastats.com/Clan/9JUVCV0L/Members.json
        //get https://fwastats.com/Weights.json
        //for each member get the weight
        //as per weight range for TH calculate the message
        event.reply(":townhall14:").queue(); // This requires no permissions!
    }
}