package net.porillo.skullwalls.actions;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.actions.Action;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public class ActionWorker {
    private SkullWalls skws;

    public ActionWorker(SkullWalls skws) {
        this.skws = skws;
    }

    public void executeAction(BlockDamageEvent e, String name) {
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        this.skws.getConfiguration().getActionMap().values().stream().filter(a -> a.getItem() == item.getTypeId() && player.hasPermission(a.getPerm())).forEach(a -> {
            String cmd = a.execute(name, player.getName());
            player.sendMessage(GRAY + "Successfully executed '/" + cmd + "' ");
            this.skws.getLogger().info(player.getName() + " executed '/" + cmd + "' using a " + item.getType().toString().toLowerCase());
        });
    }

    public void executeCuboid(BlockDamageEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("skullwalls.wand")) {
            Block b = e.getBlock();
            SkullWalls.getCuboidHandler().add(p, b);
            int step = SkullWalls.getCuboidHandler().getStep(p);

            if (step == 1) {
                p.sendMessage(GREEN + "Selected bound one " + GOLD + b.getX() + GREEN + ", " + GOLD + b.getY() + GREEN + ", " + GOLD + b.getZ());
            } else if (step == 2) {
                p.sendMessage(GRAY + "Selected bound two " + GOLD + b.getX() + GRAY + ", " + GOLD + b.getY() + GRAY + ", " + GOLD + b.getZ());
                p.sendMessage(BLUE + "Use /skull create to create a new skull wall");
            } else {
                p.sendMessage(RED + "Error, your step is " + step);
            }

            e.setCancelled(true);
        } else {
            e.setCancelled(true);
            p.sendMessage(RED + "You don't have permission to use the wand");
        }
    }

    public void executeQuery(Player p, String name) {
        String msg = this.skws.getConfiguration().getClickMessage().replace("%p", name);
        p.sendMessage(translateAlternateColorCodes('&', msg));
    }
}
