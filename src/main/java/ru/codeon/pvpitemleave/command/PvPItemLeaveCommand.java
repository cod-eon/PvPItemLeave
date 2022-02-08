package ru.codeon.pvpitemleave.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.codeon.pvpitemleave.PvPItemLeavePlugin;
import ru.codeon.pvpitemleave.handlers.PvPItemLeaveHandler;
import ru.codeon.pvpitemleave.utils.StringUtils;

public class PvPItemLeaveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {

            sender.sendMessage(StringUtils.convertString(PvPItemLeavePlugin.dataConfig.getIncorrectSyntax()));
            return true;

        }

        if (args[0].equalsIgnoreCase("reload")) {

            PvPItemLeavePlugin.instance.reload();
            sender.sendMessage(StringUtils.convertString(PvPItemLeavePlugin.dataConfig.getSuccessReloaded()));
            return true;

        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null) {

            sender.sendMessage(StringUtils.convertString(PvPItemLeavePlugin.dataConfig.getPlayerIsNull()));
            return true;

        }

        targetPlayer.getInventory().addItem(PvPItemLeaveHandler.setUpItemLeave());
        sender.sendMessage(StringUtils.convertString(PvPItemLeavePlugin.dataConfig.getSuccessGived()));

        return true;
    }
}
