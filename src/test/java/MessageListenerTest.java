import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class MessageListenerTest {

    @Test
    void calcStats() {
        ArrayList<Members> members = new ArrayList<>();
        members.add(getMembers(138000, 14));
        members.add(getMembers(118000, 13));
        members.add(getMembers(108000, 12));
        members.add(getMembers(85000, 10));
        members.add(getMembers(68000, 9));
        members.add(getMembers(100, 12));
        members.add(getMembers(800, 14));
        String responseString = new CompCommandProcessor().getResponseString(members,"");
        System.out.println(responseString);
    }

    @NotNull
    private Members getMembers(int weight, int townHall) {
        Members m = new Members();
        m.setTownHall(townHall);
        m.setWeight(weight);
        return m;
    }
}