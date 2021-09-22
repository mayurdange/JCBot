import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MessageListenerTest {

    @Test
    void calcStats() {
        ArrayList<Members> members = new ArrayList<>();

        //        weightRange.put(Pair.of(139000, 116000), 14);
//        weightRange.put(Pair.of(116000, 119000), 13);
//        weightRange.put(Pair.of(119000, 109000), 12);
//        weightRange.put(Pair.of(109000, 86000), 11);
//        weightRange.put(Pair.of(86000, 69000), 10);
//        weightRange.put(Pair.of(69000, 0), 9);

        members.add(getMembers(138000, 14));
        members.add(getMembers(118000, 13));
        members.add(getMembers(108000, 12));
        members.add(getMembers(85000, 10));
        members.add(getMembers(68000, 9));
        members.add(getMembers(100, 12));
        members.add(getMembers(800, 14));
        String responseString = new MessageListener().getResponseString(members,"");
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