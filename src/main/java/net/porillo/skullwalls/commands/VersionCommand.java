package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

public class VersionCommand extends BaseCommand {

    public VersionCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("version");

    }

    public void runCommand(CommandSender sender, List<String> args) {
        PluginDescriptionFile pdf = this.plugin.getDescription();
        sender.sendMessage(ChatColor.RED + "[DIAG] [W:" + SkullWalls.getWallHandler().getReadOnlyWalls().size() + "]");
        sender.sendMessage(ChatColor.GOLD + "Version: " + pdf.getVersion());
        sender.sendMessage(ChatColor.GOLD + "Author: " + pdf.getAuthors().get(0));
        sender.sendMessage(ChatColor.GOLD + "Website: " + pdf.getWebsite());
    }
}
