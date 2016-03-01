package net.porillo.skullwalls;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@Data
@RequiredArgsConstructor
public class Action {

    private final String name;
    private final String cmd;
    private final String perm;
    private final int item;

    public String execute(String name, String executor) {
        String command = this.cmd.replace("%p", name).replace("%e", executor);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        return command;
    }
}
