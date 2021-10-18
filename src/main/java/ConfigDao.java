import com.opencsv.CSVWriter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDao {
    public static final String ALIAS = "ALIAS";
    public static final String USER_ALIAS = "USER_ALIAS";
    public static final String PREFIX = "PREFIX";
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;
    public Map<String,Character> prefix= new HashMap<>();
    public Map<String,String> mentions = new HashMap<>();
    private Map<String,String> allAlias = new HashMap<>();
    private Map<String,String> userAlias = new HashMap<>();

    public ConfigDao() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./test");
        ds.setUser("sa");
        ds.setPassword("sa");
        jdbcTemplateObject = new JdbcTemplate(ds);
        loadAllConfig();
    }

    private void loadAllConfig() {
        jdbcTemplateObject.query("CALL CSVREAD('classpath:config.csv')",(rs)->{
            switch (rs.getString("type")){
                case ALIAS:
                    allAlias.put(rs.getString("key"), rs.getString("val"));
                    break;
                case USER_ALIAS:
                    userAlias.put(rs.getString("key"), rs.getString("val"));
                    break;
                case PREFIX:
                    prefix.put(rs.getString("key"), rs.getString("val").charAt(0));
                    break;
            }

        });
    }

    private void writeAllConfig() throws IOException {
        FileWriter writer = new FileWriter("src/main/resources/config.csv");
        CSVWriter csvWriter= new CSVWriter(writer);
        List<String[]> toWrite = new ArrayList<>();
        toWrite.add(new String[]{"type","key","val"});
        allAlias.forEach((k,v)-> toWrite.add(new String[]{ALIAS,k,v}));
        userAlias.forEach((k,v)-> toWrite.add(new String[]{USER_ALIAS,k,v}));
        prefix.forEach((k,v)-> toWrite.add(new String[]{PREFIX,k, String.valueOf(v)}));
        //toWrite.add(new String[]{PREFIX,PREFIX, String.valueOf(prefix)});
        csvWriter.writeAll(toWrite);
        csvWriter.close();
        writer.close();
    }

    public void addAlias(String alias, String clanTag) throws IOException {
        if(alias.length()>1) {
            allAlias.put(alias.toLowerCase(), clanTag);
            writeAllConfig();
        }else{
            throw new RuntimeException("Alias name must be >2 chars");
        }
    }

    public Map<String,String> getAllAlias(){
        return allAlias;
    }

    public void addUserAlias(String user, String clanTag) throws IOException {
        userAlias.put(user.toLowerCase(),clanTag);
        writeAllConfig();
    }

    public Map<String,String> getUserAlias(){
        return userAlias;
    }

    public void setMentionOnFailure(String serverName) throws IOException {
//        mentions.
//        writeAllConfig();
    }

    public void setPrefix(char newPrefix, String serverName) throws IOException {
        prefix.put(serverName,newPrefix);
        writeAllConfig();
    }

    public char getPrefix(Guild guild) {
        return prefix.getOrDefault(guild.getName(),'\'');
    }
}
