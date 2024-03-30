# Lava Rises by Acronical

A customisable lava rises plugin for Minecraft servers running the Spigot/Paper software.
This is a more customisable version of the plugin originally made for [Yrrah](https://www.linktr.ee/Yrrah).
If you use this plugin in your own content, please credit me by linking this repository and [my website](https://acronical.pages.dev).

Initialising the plugin allows you to set an area of length and width for the lava to rise, as well as the x and z coordinates for the centre of that area.
The raise lava command takes an input for the number of blocks you want to raise the lava by, the more blocks/closer to the surface, the more lag.

Init command usage: /initlava <length> <width> [originx] [originz]
- Length/width in blocks
- OriginX is an x coordinate (optional)
- OriginZ is a z coordinate (optional)
  - Setting one origin will set both coordinates to that value
  - Leaving the origin blank will set it to 0, 0

Rise command usage: /lavarise <amount>
- Amount is in blocks
- Initialised starts is always -64

To utilise this plugin, you must either be OP or have the following permissions:
- lavarises.*
- lavarises.initlavaA
- lavarises.lavarise

This plugin requires [FastAsyncWorldEdit](https://www.spigotmc.org/resources/fastasyncworldedit.13932/).

<h3 align="left">Support Me:</h3>
<p><a href="https://ko-fi.com/acronical"> <img align="left" src="https://cdn.ko-fi.com/cdn/kofi3.png?v=3" height="50" width="210" alt="acronical" /></a></p><br><br>
