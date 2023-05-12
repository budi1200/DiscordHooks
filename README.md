# DiscordHooks

<img src="https://slocraft.eu/slocraft-logo-512.png" width=124 height=124 align="right"/>

DiscordHooks was developed for use on the [SloCraft](https://slocraft.eu) network.

Please keep in mind that the plugin has not been updated since May 2022.

### Description

The plugin provides features that allow server administrators to receive notifications on their Discord server when specific events occur on the proxy server, 
such as a player permission change.

### Features

- Integration with Litebans
  - Listens for new punishments and sends a notification to the configured Discord channel.
    <img src="https://slocraft.eu/github-pictures/litebans-demo.png" width=400 />
- Integration with Luckperms
  - Listens for any permission node changes and sends an alert to the configured Discord channel.
    <img src="https://slocraft.eu/github-pictures/luckperms-demo.png" width=400 />

### Dependencies

DiscordHooks requires a Velocity proxy version 3.1.2 or higher (not tested).

### Configuration

On startup a configuration file is loaded: `config.conf`. This file is generated automatically on first startup and can be found in the plugin's data folder.

- `config.conf` contains settings for the plugin and language strings.

### Usage

  - `/dh reload` - Reloads the plugin's configuration files. 

### Permissions

- `dh.admin` - Allows the player to use admin commands.
