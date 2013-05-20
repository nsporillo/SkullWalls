package net.milkycraft.skullwalls;

import static org.bukkit.ChatColor.RED;
import net.milkycraft.skullwalls.walls.Slot;
import net.milkycraft.skullwalls.walls.Utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ActionListener implements Listener {

	private final SkullWalls sw;
	private final String def;
	private String noperm = RED + "Permission to destroy skull is denied";
	private final String perm = "skullwalls.break.walls";
	private final String des = RED + "Destroyed #'s skull";

	public ActionListener(final SkullWalls sw) {
		this.sw = sw;
		this.def = sw.getConfiguration().getName();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (b.getType() == Material.SKULL) {
				Skull skull = ((Skull) e.getBlock().getState());
				String name = hasOwner(skull) ? skull.getOwner() : this.def;
				if (!hasTool(p)) {
					if (p.isSneaking()) {
						if (Utils.isSlot(b)) {
							if (p.hasPermission(perm) || p.isOp()) {
								p.sendMessage(des.replace("#", name));
								Slot.getAt(b.getLocation()).disable();
							} else {
								e.setCancelled(true);
								p.sendMessage(noperm);
							}
						}
					} else {
						if (Utils.isSlot(b)) {
							sw.getActionWorker().queueQuery(p, name);
							if (p.hasPermission(perm)) {
								e.setCancelled(true);
								p.sendMessage(RED
										+ "To destroy skull you must sneak and break");
							} else {
								e.setCancelled(true);
								p.sendMessage(noperm);
							}
						}
					}
				} else {
					if (p.isSneaking()) {
						e.setCancelled(true);
						sw.getActionWorker().queueQuery(p.getPlayer(), name);
					} else {
						e.setCancelled(true);
						ItemStack s = p.getItemInHand();
						onBlockDamage(new BlockDamageEvent(p, b, s, false));
						if (Utils.isSlot(b)) {
							p.sendMessage(RED
									+ "Block selected is already a part of a skull wall");
							p.sendMessage(RED
									+ "Overlapping walls will not perform as expected!");
						}
					}
				}
			}
		} else if (this.sw.getConfiguration().isProtecting()) {
			if (e.getBlock().getType() == Material.SKULL) {
				if (!p.hasPermission(perm)) {
					e.setCancelled(Utils.check(b, p, noperm));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockDamage(BlockDamageEvent e) {
		if (e.getBlock().getType().equals(Material.SKULL)) {
			Player player = e.getPlayer();
			Skull skull = ((Skull) e.getBlock().getState());
			String name = hasOwner(skull) ? skull.getOwner() : this.def;
			if (e.getItemInHand().getTypeId() == this.sw.getConfiguration().getTool()) {
				this.sw.getActionWorker().queueCuboid(e);
			} else if (this.sw.getConfiguration().getActionMap()
					.containsKey(e.getItemInHand().getTypeId())) {
				this.sw.getActionWorker().queueAction(e, name);
			} else {
				if (skull.getSkullType() == SkullType.PLAYER) {
					sw.getActionWorker().queueQuery(player, name);
				}
			}
		}
	}

	private boolean hasOwner(Skull skull) {
		return skull.getOwner().equals("") ? false : true;
	}

	private boolean hasTool(Player p) {
		int ph = p.getItemInHand().getTypeId();
		int ct = sw.getConfiguration().getTool();
		return ph == ct ? true : false;
	}
}
