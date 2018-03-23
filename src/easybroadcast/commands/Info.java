package easybroadcast.commands;

import easybroadcast.EasyBroadcast;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jaldaba0th
 */
public class Info implements CommandExecutor {

    private final EasyBroadcast plugin;

    public Info(EasyBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player sender = (Player) cs;
            if (sender.hasPermission("easybroadcast.manage")) {
                if (args.length > 1) {
                    return false;
                }
            }
        } else {
            if (args.length > 1) {
                return false;
            }
        }
        return execInfo(cs, args);
    }

    /**
     *
     * @param cs
     */
    private void displayInfo(CommandSender cs) {
        cs.sendMessage(plugin.replaceColors("&7-------------- &c[&aEasyBroadcast&c] &ev" + plugin.getDescription().getVersion() + " &7--------------"));
        cs.sendMessage(plugin.replaceColors("&c> &a/easybroadcast&7(&a/ebc&7) display all commands"));
        cs.sendMessage(plugin.replaceColors("&c> &a/easybroadcast&7(&a/ebc&7) &c-r &7reload config"));
        cs.sendMessage(plugin.replaceColors("&c> &a/easybroadcast&7(&a/ebc&7) &c-on  &7start timer"));
        cs.sendMessage(plugin.replaceColors("&c> &a/easybroadcast&7(&a/ebc&7) &c-off  &7stop timer"));
        cs.sendMessage(plugin.replaceColors("&c> &a/broadcast&7(&a/bc&7) <&amessage&7>  broadcast a message"));
    }

    /**
     *
     * @param cs
     * @param args
     * @return
     */
    private boolean execInfo(CommandSender cs, String[] args) {
        if (args.length == 0) {
            displayInfo(cs);
        } else if (args[0].equalsIgnoreCase("-r")) {
            plugin.stopTimer();
            plugin.reloadConfig();
            timerInit(cs, "&c> [&aEasyBroadcast&c] &freloaded");
        } else if (args[0].equalsIgnoreCase("-on")) {
            if (!plugin.getRunning()) {
                timerInit(cs, "&c> [&aEasyBroadcast&c] &7Timer has been turned &cON");
            } else {
                cs.sendMessage(plugin.replaceColors("&c> [&aEasyBroadcast&c] &7Timer is already &cON"));
            }
        } else if (args[0].equalsIgnoreCase("-off")) {
            if (plugin.getRunning()) {
                plugin.stopTimer();
                cs.sendMessage(plugin.replaceColors("&c> [&aEasyBroadcast&c] &7Timer has been turned &cOFF"));
            } else {
                cs.sendMessage(plugin.replaceColors("&c> [&aEasyBroadcast&c] &7Timer is already &cOFF"));
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     *
     * @param cs
     * @param msg
     */
    private void timerInit(CommandSender cs, String msg) {
        if (plugin.timerSet()) {
            plugin.initTimer();
            cs.sendMessage(plugin.replaceColors(msg));
        } else {
            cs.sendMessage(plugin.replaceColors("&c> &7There are &cno &7timed messages configured"));
        }
    }

}
