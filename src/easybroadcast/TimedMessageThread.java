package easybroadcast;

import java.util.List;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Jaldaba0th
 */
public class TimedMessageThread extends TimerTask {

    private final String prefix;
    private final String msg;
    private final List<String> worlds;

    public TimedMessageThread(String prefix, String msg, List<String> worlds) {
        this.prefix = prefix;
        this.msg = msg;
        this.worlds = worlds;
    }

    @Override
    public void run() {
        if (worlds.isEmpty()) {
            Bukkit.broadcastMessage(prefix + " " + msg);
        } else {
            for (String world : worlds) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld().getName().equals(world)) {
                        Bukkit.broadcastMessage(prefix + " " + msg);
                    }
                }
            }
        }
    }

}
