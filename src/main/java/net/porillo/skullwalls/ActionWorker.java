package net.porillo.skullwalls;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ActionWorker {
	private SkullWalls	skws;

	public ActionWorker(SkullWalls skws) {
		this.skws = skws;
	}

	public void queueAction(BlockDamageEvent e, String name) {
		Player player = e.getPlayer();
		ItemStack item = e.getItemInHand();
		for (Action a : this.skws.getConfiguration().getActionMap().values())
			if (a.getItem() == item.getTypeId() && player.hasPermission(a.getPerm())) {
				String cmd = a.execute(name, player.getName());
				player.sendMessage(ChatColor.GRAY + "Successfully executed '/" + cmd + "' ");
				this.skws.log(player.getName() + " executed '/" + cmd + "' using a "
						+ item.getType().toString().toLowerCase());
			}
	}

	public void queueCuboid(BlockDamageEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("skullwalls.wand.use")) {
			Block b = e.getBlock();
			this.skws.getCuboider().add(p, b);
			int step = this.skws.getCuboider().getStep(p);
			if (step == 1) {
				p.sendMessage(ChatColor.GREEN + "Selected bound one " + ChatColor.GOLD + b.getX()
						+ ChatColor.GREEN + ", " + ChatColor.GOLD + b.getY() + ChatColor.GREEN
						+ ", " + ChatColor.GOLD + b.getZ());
			} else if (step == 2) {
				p.sendMessage(ChatColor.GRAY + "Selected bound two " + ChatColor.GOLD + b.getX()
						+ ChatColor.GRAY + ", " + ChatColor.GOLD + b.getY() + ChatColor.GRAY + ", "
						+ ChatColor.GOLD + b.getZ());
				p.sendMessage(ChatColor.BLUE + "Use /skull create to create a new skull wall");
			} else {
				p.sendMessage(ChatColor.RED + "Error, your step is " + step);
			}
			e.setCancelled(true);
		} else {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You don't have permission to use the wand");
		}
	}

	public void queueQuery(Player p, String name) {
		String msg = this.skws.getConfiguration().getClickMessage().replace("%p", name);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}
