package easybroadcast;

import easybroadcast.commands.Broadcast;
import easybroadcast.commands.Info;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Jaldaba0th
 */
public class EasyBroadcast extends JavaPlugin {

    private static final String PREFIX = "&c[&aServer&c]";
    private static final String DEFAULT_COL = "&c";
    private static final String DEFAULT_MSG = "This messages broadcasts every 60 seconds on all worlds";

    private static final int MULTIPLICATOR = 1000;

    private final File f = new File(getDataFolder(), "config.yml");

    private static Logger log;

    private Timer[] t;

    private boolean running = false;

    @Override
    public void onEnable() {
        log = getLogger();
        if (!f.exists()) {
            loadConfig();
        } else {
            if (!checkConfig()) {
                log.warning("> There is something wrong with the config.yml");
                log.warning("> Disabling the plugin... ");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        registerCommands();
        if (timerSet()) {
            initTimer();
        } else {
            log.info("> There are no timed messages configured");
        }
        log.info("enabled");
    }

    @Override
    public void onDisable() {
        log.info("disabled");
    }

    /**
     *
     */
    private void registerCommands() {
        Info info = new Info(this);
        getCommand("ebc").setExecutor(info);
        getCommand("easybroadcast").setExecutor(info);
        Broadcast bc = new Broadcast(this);
        getCommand("bc").setExecutor(bc);
        getCommand("broadcast").setExecutor(bc);
    }

    /**
     *
     * @return
     */
    private boolean checkConfig() {
        return getConfig().contains("prefix") && getConfig().contains("default-broadcast-color");
    }

    /**
     *
     */
    private void loadConfig() {
        getConfig().addDefault("prefix", PREFIX);
        getConfig().addDefault("default-broadcast-color", DEFAULT_COL);
        String path = "timed-messages.first";
        getConfig().addDefault(path + ".message", DEFAULT_MSG);
        getConfig().addDefault(path + ".delay", 60);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    /**
     *
     * @return
     */
    public boolean initTimer() {
        if (!running) {
            Set<String> namesSet = getConfig().getConfigurationSection("timed-messages").getKeys(false);
            String[] names = namesSet.toArray(new String[namesSet.size()]);
            t = new Timer[names.length];
            for (int i = 0; i < t.length; i++) {
                String name = names[i];
                int delay = getConfig().getInt("timed-messages." + name + ".delay");
                String msg = getConfig().getString("timed-messages." + name + ".message");
                List<String> worlds = getConfig().getStringList("timed-messages." + name + ".world");
                int period = delay * MULTIPLICATOR;
                t[i] = new Timer();
                t[i].schedule(new TimedMessageThread(replaceColors(getPrefix()), replaceColors(msg), worlds), period, period);
            }
            running = true;
        } else {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean stopTimer() {
        if (running) {
            for (Timer timer : t) {
                timer.cancel();
            }
            running = false;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Takes a string and replaces &# color codes with ChatColors
     *
     * @param message
     * @return
     */
    public String replaceColors(String message) {
        return message.replaceAll("(?i)&([a-f0-9])", "\u00A7$1");
    }

    /**
     *
     * @return
     */
    public String getPrefix() {
        return getConfig().getString("prefix");
    }

    /**
     *
     * @return
     */
    public String getDefaultCol() {
        return getConfig().getString("default-broadcast-color");
    }

    /**
     *
     * @return
     */
    public boolean getRunning() {
        return running;
    }

    /**
     *
     * @return
     */
    public boolean timerSet() {
        return getConfig().contains("timed-messages");
    }

}
