package net.porillo.skullwalls;

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

import static org.bukkit.ChatColor.RED;

public class ActionListener implements Listener {
    private final SkullWalls sw;
    private final String def;
    private final String noperm = RED + "Permission to destroy skull is denied";
    private final String perm = "skullwalls.break.walls";
    private final String des = RED + "Destroyed #'s skull";

    public ActionListener(SkullWalls sw) {
        this.sw = sw;
        this.def = sw.getConfiguration().getName();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (p.getGameMode() == GameMode.CREATIVE) {
            if (b.getType() == Material.SKULL) {
                Skull skull = (Skull) e.getBlock().getState();
                String name = this.hasOwner(skull) ? skull.getOwner() : this.def;
                if (!this.hasTool(p)) {
                    if (p.isSneaking()) {
                        if (SkullWalls.getWallHandler().getWallFromBlock(b) != null) {
                            if (p.hasPermission(perm) || p.isOp()) {
                                p.sendMessage(this.des.replace("#", name));
                                // Disable skull?
                            } else {
                                e.setCancelled(true);
                                p.sendMessage(this.noperm);
                            }
                        }
                    } else if (SkullWalls.getWallHandler().getWallFromBlock(b) != null) {
                        this.sw.getActionWorker().queueQuery(p, name);
                        if (p.hasPermission(perm)) {
                            e.setCancelled(true);
                            p.sendMessage(RED + "To destroy skull you must sneak and break");
                        } else {
                            e.setCancelled(true);
                            p.sendMessage(this.noperm);
                        }
                    }
                } else if (p.isSneaking()) {
                    e.setCancelled(true);
                    this.sw.getActionWorker().queueQuery(p.getPlayer(), name);
                } else {
                    e.setCancelled(true);
                    ItemStack s = p.getItemInHand();
                    this.onBlockDamage(new BlockDamageEvent(p, b, s, false));
                    if (SkullWalls.getWallHandler().getWallFromBlock(b) != null) {
                        p.sendMessage(RED + "Block selected is already a part of a skull wall");
                        p.sendMessage(RED + "Overlapping walls will not perform as expected!");
                    }
                }
            }
        } else if (this.sw.getConfiguration().isProtecting() && e.getBlock().getType() == Material.SKULL && !p.hasPermission(perm))
            e.setCancelled(SkullWalls.getWallHandler().hasPermission(b, p, this.noperm));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDamage(BlockDamageEvent e) {
        if (e.getBlock().getType().equals(Material.SKULL)) {
            Player player = e.getPlayer();
            Skull skull = (Skull) e.getBlock().getState();
            String name = this.hasOwner(skull) ? skull.getOwner() : this.def;
            if (e.getItemInHand().getTypeId() == this.sw.getConfiguration().getTool()) {
                this.sw.getActionWorker().queueCuboid(e);
            } else if (this.sw.getConfiguration().getActionMap().containsKey(e.getItemInHand().getTypeId())) {
                this.sw.getActionWorker().queueAction(e, name);
            } else if (skull.getSkullType() == SkullType.PLAYER)
                this.sw.getActionWorker().queueQuery(player, name);
        }
    }

    private boolean hasOwner(Skull skull) {
        return !skull.getOwner().equals("");
    }

    private boolean hasTool(Player p) {
        int ph = p.getItemInHand().getTypeId();
        int ct = this.sw.getConfiguration().getTool();
        return ph == ct;
    }
}
