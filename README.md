# HardcoreMP
_"For the slightly more unfair Minecraft experience"_

## Content
1. Introduction
2. Functions
3. Installation
4. Config
5. Future Plans

## Introduction
HardcoreMP is a plugin that adds more unfair stuff to a Minecraft Server.
If you want to keep playing after you die but you don't want to miss out on big pain, this plugin is for you.

## Functions
- Do not drop items or experience on death
- Reset everyone's inventory if any player dies
- more to come!

## Installation
To install the plugin, head over to [RELEASES](https://github.com/InitialPosition/HardcoreMP/releases), download the latest `.jar` file and place it into the `plugins` folder of your Spigot installation.
The plugin will generate a config file on first startup. It is recommended to edit this file and to either reload or restart the server afterwards.

## Config
The following parameters exist in the config file and can be changed.

| Parameter | Usage | Accepted input | Default
| :-------- | ----- | -------------- | :-----: |
| DELETE_INVENTORIES_ON_DEATH | If set to `true`, all players' inventories will be deleted on any players' death. | `true` or `false` | `true` |
| ONE_HIT_KILL | If set to `true`, any damage is lethal to players. | `true` or `false` | `false` | 
| DYING_PLAYER_DROPS_INVENTORY | If set to `true`, the dying player drops his inventory on death (default Minecraft behavior). | `true` or `false` | `false` |
| DYING_PLAYER_DROPS_EXP | If set to `true`, the dying player drops experience on death (default Minecraft behavior). | `true` or `false` | `false` |
| HANDLE_CURSE_OF_BINDING | If `keep`, items with Curse of Binding will be kept (but dropped).  If `disenchant`, the curse will be removed from items with it.  If `delete`, items will be deleted. | `delete`, `disenchant`, or `keep` | `delete` |
| CLEAR_ENDER_CHESTS | If `all`, all Ender Chests are cleared on any player's death.  If `player`, the dying player has their Ender Chest cleared.  If `keep`, Ender Chests are kept. | `default`, `player`, or `all` | `keep` |
| RANDOM_CURSE_OF_VANISHING | Items with Curse of Vanishing have an `x`% chance of vanishing when switched to. | `0` - `100` | `0` |

All other config entries are used internally and should not be changed.

# Future Plans
- [ ] More settings
- [ ] Ingame option commands
- [ ] Bug fixes
