import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;

import static net.dv8tion.jda.api.interactions.commands.OptionType.MENTIONABLE;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class MessageListener extends ListenerAdapter {
    CompCommandProcessor compCommandProcessor=new CompCommandProcessor();
    private static final ConfigDao configDao= new ConfigDao();

    public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createDefault(args[0])
//                createLight(args[0], EnumSet.noneOf(GatewayIntent.class))
                .addEventListeners(new MessageListener()).build();
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(new CommandData("comp", "gets the TH composition of the clan")
                .addOptions(new OptionData(STRING, "tag", "Clan tag. if blank, use default clans").setRequired(false))
        );
        commands.addCommands(new CommandData("prefix", "change the bot prefix")
                .addOptions(new OptionData(STRING, "new-prefix", "the new prefix to use").setRequired(true))
        );
        commands.addCommands(new CommandData("jctest", "try out the bot's command (only for ⌜Co⌟ ⌯ Mayur's testing)")
                .addOptions(new OptionData(STRING, "tag", "option if any").setRequired(false))
        );
        commands.addCommands(new CommandData("notify", "Who to notify when there is a error?")
                .addOptions(new OptionData(MENTIONABLE, "mention", "who should be mentioned when there is error").setRequired(false))
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
                    String clanTagstr = event.getOption("tag") == null ? "#9JUVCV0L" : event.getOption("tag").getAsString();
                    event.deferReply().queue();
                    MessageEmbed em = compCommandProcessor.calcStats(clanTagstr); // content is required so no null-check here
                    event.getHook().sendMessageEmbeds(em).queue();
                    break;
                case "prefix":
                    System.out.println(event.getMember().getUser().getAsTag()+" requested prefix for "+event.getOptions());
                    System.out.println(event.getMember().getRoles());
                    char asString = event.getOption("new-prefix").getAsString().charAt(0);
                    configDao.setPrefix(asString,event.getGuild().getName());
                    event.reply("bot prefix changed to "+configDao.getPrefix(event.getGuild())).queue();
                    Member botMem = event.getGuild().getMemberById(event.getJDA().getSelfUser().getId());
                    String nickname = "JC_BOT ["+asString+"]";
                    event.getGuild().modifyNickname(botMem,nickname).queue();
                    break;
                case "jctest":
                    if ("mayur#6158".equals(event.getMember().getUser().getAsTag())) {
                        new TestCommandProcessor().calcStats(event);
                    } else {
                        System.out.println(event.getMember().getUser().getAsTag()+" tried to run jctest command");
                        event.reply("this command is only reserved for ⌜Co⌟ ⌯ Mayur for testing purposes, only he can run this").queue();
                    }
                    break;
                case "notify":
                    IMentionable mention = event.getOption("mention").getAsMentionable();
                    event.reply("every bot command failure will be mentioned to : ").mention(mention).queue();
                    break;
                default:
                    System.out.println("unknown command " + event);
            }
        }catch(Exception e){
            event.reply("something went wrong :poop:").queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        String readMessage = event.getMessage().getContentRaw();
        String readMessageLowercase = readMessage.toLowerCase();
        //on *th remind to use prefix+th
        if (readMessage.length()>0 && readMessage.charAt(0)==configDao.getPrefix(event.getGuild())){
            processCommand(event, readMessage, readMessageLowercase);
        }



//        if( readMessageLowercase.contains("rino") || event.getAuthor().getName().toLowerCase().contains("rino")){
//            event.getMessage().addReaction("uhhh:884839422967820298").queue();
//        }
//        if( readMessageLowercase.contains("cj") || event.getAuthor().getName().toLowerCase().contains("c.j")){
//            event.getMessage().addReaction("hmm:881824281334530088").queue();
//        }
//
//        if(event.getMessage().getMentionedUsers().stream().anyMatch(u->u.getAsTag().equals("C.J.#2327"))){
//            event.getChannel().sendMessage("https://cdn.discordapp.com/emojis/881824281334530088.png").queue();
//        }
        if(!event.getAuthor().isBot()) System.out.println(event.getAuthor().getName() +"@"+event.getChannel().getName()+ " : " + readMessage +"\n");
    }

    private void processCommand(@NotNull GuildMessageReceivedEvent event, String readMessage, String readMessageLowercase) {
        try{
            int spaceIndex = readMessageLowercase.indexOf(" ");
            String command = readMessageLowercase.substring(1, spaceIndex>0?spaceIndex: readMessageLowercase.length() );
            switch(command){
                case "th"://composition
                    String clanTag = getClanTag(readMessage, readMessageLowercase,event.getAuthor().getAsTag());
                    MessageEmbed messageEmbed = compCommandProcessor.calcStats(clanTag);
                    event.getChannel().sendMessage(messageEmbed).queue();
                    break;
                case "ln"://link .. create alias
                    String[] s = readMessage.split(" ");
                    try {
                        if(s.length==2){
                            configDao.addUserAlias(event.getAuthor().getAsTag(),s[1]);
                            event.getMessage().addReaction("pepe_love:883680679693533255").queue();
                        }else{
                            configDao.addAlias(s[1], s[2]);
                            event.getMessage().addReaction("pepe_love:883680679693533255").queue();
                        }
                    } catch (IOException e) {
                        event.getMessage().addReaction("pepe_cry:883680509916508200").queue();
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case "help"://help message
                    EmbedBuilder eb = new EmbedBuilder();
                    char prefix = configDao.getPrefix(event.getGuild());
                    eb.setDescription("** "+prefix+"th #clanTag ** : to get the town hall composition of the clan\n" +
                            "** "+prefix+"ln alias #clanTag ** : link alias to clan tag\n"+
                            "** "+prefix+"ln #clanTag ** : link user to clan tag\n"+
                            "** "+prefix+"list-aliases ** list all aliases\n"+
                            "** "+prefix+"help ** : this help page");
                    event.getChannel().sendMessage(eb.build()).queue();
                    break;
                case "list-aliases":
                    EmbedBuilder eb1 = new EmbedBuilder();
                    StringBuilder ls = new StringBuilder("Listing all Aliases\n");
                    configDao.getAllAlias().forEach((a,t) -> ls.append(a).append("\t->\t").append(t).append("\n"));
                    ls.append("\nUser aliases:\n");
                    configDao.getUserAlias().forEach((a,t) -> ls.append(a).append("\t->\t").append(t).append("\n"));
                    eb1.setDescription(ls.toString());
                    event.getChannel().sendMessage(eb1.build()).queue();
                    break;
                default:
            }
        }catch(Exception e){
            System.out.println(e.getMessage()+" for "+ readMessage);
            e.printStackTrace();
            event.getMessage().addReaction("pepe_cry:883680509916508200").queue();
            char prefix = configDao.getPrefix(event.getGuild());
            String content = "something went wrong.\nTry ** " + prefix + "help ** for command info\n";
            if(e instanceof  RuntimeException){
                content=content+e.getMessage();
            }
            event.getMessage().reply(content).queue();
        }
    }

    @NotNull
    private String getClanTag(String readMessage, String readMessageLowercase,String requestorTag) {
        int hashIndex = readMessageLowercase.indexOf("#");
        if (hashIndex==-1){// check for alias
            if(readMessageLowercase.trim().length()==3){
                return configDao.getUserAlias().get(requestorTag);
            }
            String param = readMessageLowercase.substring(4);
            Map<String, String> allAlias = configDao.getAllAlias();
            System.out.println(param.toLowerCase());
            return allAlias.getOrDefault(param.toLowerCase(),param);
        }else{
            return readMessage.substring(hashIndex).toUpperCase();
        }
//        return "#9JUVCV0L";
    }

}