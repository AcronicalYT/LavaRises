package dev.acronical.lavarises.commands;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static com.sk89q.worldedit.world.block.BlockTypes.*;

public class PluginCommands implements CommandExecutor {

    File lavaFile = new File(Bukkit.getPluginsFolder() + "/lavarises", "lava.yml");
    FileConfiguration data = YamlConfiguration.loadConfiguration(lavaFile);

    int yLevel = data.getInt("yLevel");
    boolean afterFirstRun = data.getBoolean("afterFirstRun");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("[!] Only players can use that command.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("lavarise") && player.hasPermission("lavarises.lavarise")) {
            // get the opposite corners of the selection
            int x1 = -150;
            int z1 = -150;
            int x2 = 150;
            int z2 = 150;

            if (afterFirstRun == false) {
                try {
                    data.set("afterFirstRun", true);
                    data.set("yLevel", -64);
                    data.save(lavaFile);
                    yLevel = -64;
                    System.out.println("First run of command, yLevel set to -64 and afterFirstRun set to true.");
                } catch (IOException e) {
                    System.out.println("[!] An error occurred while saving the data.");
                    throw new RuntimeException(e);
                }
            }

            int oldYLevel = yLevel;

            System.out.println("Player: " + player.getName() + " has used the lavarise command.");

            // Get the number of block to raise the lava by
            if (strings.length > 0) {
                try {
                    if (strings[0].startsWith("-")) {
                        player.sendMessage("Please provide a positive number.");
                        return true;
                    }
                    yLevel = Integer.parseInt(strings[0]) + yLevel;
                    if (yLevel > 320) {
                        player.sendMessage("Lava too high, please enter " + (320 - oldYLevel) + " or less.");
                        return true;
                    }
                    data.set("yLevel", yLevel);
                    data.save(lavaFile);
                } catch (NumberFormatException e) {
                    player.sendMessage("[!] Invalid number provided.");
                    return true;
                } catch (IOException e) {
                    System.out.println("[!] An error occurred while saving the data.");
                    throw new RuntimeException(e);
                }
            } else if (strings.length == 0) {
                player.sendMessage("Please provide a number of blocks to raise the lava by.");
                return true;
            }

            World world = FaweAPI.getWorld(player.getWorld().getName());

            BlockVector3 coords1 = BlockVector3.at(x1, oldYLevel, z1);
            BlockVector3 coords2 = BlockVector3.at(x2, yLevel, z2);

            Region region = new CuboidRegion(world, coords1, coords2);

            world.setBlocks(region, new BaseBlock(LAVA));

            player.sendMessage("Lava has been raised by " + (yLevel - oldYLevel) + " blocks.");
        }
        return true;
    }
}

// 150 -150
// -150 150