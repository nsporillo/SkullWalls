package net.milkycraft.skullwalls;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ActionWorker {

	private SkullWalls skws;

	public ActionWorker(SkullWalls skws) {
		this.skws = skws;
	}

	public void queueAction(BlockDamageEvent e, String name) {
		Player player = e.getPlayer();
		ItemStack item = e.getItemInHand();
		for (Action a : skws.getConfiguration().getActionMap().values()) {
			if (a.getItem() == item.getTypeId()
					&& player.hasPermission(a.getPerm())) {
				String cmd = a.execute(name, player.getName());
				player.sendMessage(GRAY + "Successfully executed '/" + cmd
						+ "' ");
				skws.log(player.getName() + " executed '/" + cmd + "' using a "
						+ item.getType().toString().toLowerCase());
			}
		}
	}

	public void queueCuboid(BlockDamageEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("skullwalls.wand.use")) {
			Block b = e.getBlock();
			skws.getCuboider().add(p, b);
			int step = skws.getCuboider().getStep(p);
			if (step == 1) {
				p.sendMessage(GREEN + "Selected bound one " + GOLD + b.getX()
						+ GREEN + ", " + GOLD + b.getY() + GREEN + ", " + GOLD
						+ b.getZ());
			} else if (step == 2) {
				p.sendMessage(GRAY + "Selected bound two " + GOLD + b.getX()
						+ GRAY + ", " + GOLD + b.getY() + GRAY + ", " + GOLD
						+ b.getZ());
				p.sendMessage(BLUE
						+ "Use /skull create to create a new skull wall");
			} else {
				p.sendMessage(RED + "Error, your step is " + step);
			}
			e.setCancelled(true);
		} else {
			e.setCancelled(true);
			p.sendMessage(RED + "You don't have permission to use the wand");
		}
	}

	public void queueQuery(Player p, String name) {
		String msg = skws.getConfiguration().getClickMessage()
				.replace("%p", name);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}
