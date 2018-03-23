package easybroadcast.commands;

import easybroadcast.EasyBroadcast;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jaldaba0th
 */
public class Broadcast implements CommandExecutor {

    private final EasyBroadcast plugin;

    public Broadcast(EasyBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player sender = (Player) cs;
            if (sender.hasPermission("easybroadcast.broadcast")) {
                if (args.length == 0) {
                    return false;
                }
                Bukkit.broadcastMessage(plugin.replaceColors(plugin.getPrefix() + " " + getMsg(args)));
            }
        } else {
            if (args.length == 0) {
                return false;
            }
            Bukkit.broadcastMessage(plugin.replaceColors(plugin.getPrefix() + " " + getMsg(args)));
        }
        return true;
    }

    private String getMsg(String[] args) {
        String msg = args[0];
        for (int i = 1; i < args.length; i++) {
            msg = msg + " " + args[i];
        }
        return plugin.getDefaultCol() + msg;
    }

}
