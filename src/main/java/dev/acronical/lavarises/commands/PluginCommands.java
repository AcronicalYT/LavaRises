package dev.acronical.lavarises.commands;

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

public class PluginCommands implements CommandExecutor {

    File lavaFile = new File(Bukkit.getPluginsFolder() + "/lavarises", "lava.yml");
    FileConfiguration data = YamlConfiguration.loadConfiguration(lavaFile);

    int yLevel = data.getInt("yLevel");
    boolean initialised = data.getBoolean("initialised");
    int width = data.getInt("width");
    int length = data.getInt("length");
    int originX = data.getInt("originX");
    int originZ = data.getInt("originZ");

    int x1 = 0;
    int z1 = 0;
    int x2 = 0;
    int z2 = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("[!] Only players can use that command.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("initlava") && player.hasPermission("lavarises.initlava")) {
            // save the coordinates of the opposite corners of the selection based on the user input
            if (strings.length < 2) {
                player.sendMessage("Please provide the length and width area, adding coordinates for the origin is optional.");
                return true;
            }

            int length = Integer.parseInt(strings[0]);
            int width = Integer.parseInt(strings[1]);

            if(strings.length == 3) {
                originX = Integer.parseInt(strings[2]);
                originZ = originX;
            } else if(strings.length == 4) {
                originX = Integer.parseInt(strings[2]);
                originZ = Integer.parseInt(strings[3]);
            } else {
                originX = 0;
                originZ = 0;
            }

            data.set("length", length);
            data.set("width", width);
            data.set("originX", originX);
            data.set("originZ", originZ);

            x1 = originX - (length / 2);
            z1 = originZ - (width / 2);
            x2 = originX + (length / 2);
            z2 = originZ + (width / 2);

            data.set("initialised", true);
            data.set("yLevel", -64);
            yLevel = -64;
            initialised = true;
            try {
                data.save(lavaFile);
                player.sendMessage("Lavarises has been initialised.");
                player.sendMessage("The area has been set to " + length + "x" + width + " blocks around point " + originX + "," + originZ + ".");
                player.sendMessage("You can now use /lavarise [amount] to raise the lava.");
            } catch (IOException e) {
                System.out.println("[!] An error occurred while saving the data.");
                player.sendMessage("[!] An error occurred while saving the data.");
                throw new RuntimeException(e);
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("lavarise") && player.hasPermission("lavarises.lavarise")) {
            if (!initialised) {
                player.sendMessage("Please initialise lavarises first.");
                player.sendMessage("Use /initlava to initialise lavarises.");
                return true;
            }

            int oldYLevel = yLevel;

            System.out.println("Player: " + player.getName() + " has used the lavarise command.");

            // Get the number of block to raise the lava by
            if (strings.length > 0) {
                if (strings[0].startsWith("-")) {
                    player.sendMessage("Please provide a positive number.");
                    return true;
                }
                if ((Integer.parseInt(strings[0]) + yLevel) > 320) {
                    player.sendMessage("Lava too high, please enter " + (320 - oldYLevel) + " or less.");
                    yLevel = yLevel;
                    return true;
                } else {
                    yLevel = Integer.parseInt(strings[0]) + yLevel;
                }
                try {
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

            player.performCommand(String.format("/pos1 %s,%s,%s", x1, oldYLevel, z1));
            System.out.println("Pos1 set to: " + x1 + "" + oldYLevel + "" + z1);
            player.performCommand(String.format("/pos2 %s,%s,%s", x2, yLevel, z2));
            System.out.println("Pos2 set to: " + x2 + "" + yLevel + "" + z2);
            player.performCommand("/replace air,water lava");
            player.performCommand("/confirm");
            System.out.println("Replaced air and water with lava.");

            player.sendMessage("Lava has been raised by " + (yLevel - oldYLevel) + " blocks.");
        }
        return true;
    }
}

// 150 -150
// -150 150