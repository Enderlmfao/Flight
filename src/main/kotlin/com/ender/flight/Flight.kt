package com.ender.flight

import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class Flight : JavaPlugin(), CommandExecutor, Listener {

    override fun onEnable() {
        getCommand("fly")?.setExecutor(this)
        server.pluginManager.registerEvents(this, this)
        logger.info("Flight plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Flight plugin has been disabled.")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("flight.use")) {
            sender.sendMessage("You do not have permission to use this command.")
            return true
        }

        if (sender.gameMode == GameMode.CREATIVE || sender.gameMode == GameMode.SPECTATOR) {
            sender.sendMessage("You cannot use this command in Creative or Spectator mode.")
            return true
        }

        if (sender.allowFlight) {
            sender.allowFlight = false
            sender.isFlying = false
            sender.sendMessage("Flight has been disabled.")
        } else {
            sender.allowFlight = true
            sender.sendMessage("Flight has been enabled.")
        }

        return true
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        if (player.isFlying) {
            player.isFlying = false
            player.allowFlight = false
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.gameMode != GameMode.CREATIVE && player.gameMode != GameMode.SPECTATOR) {
            player.allowFlight = false
            player.isFlying = false
        }
    }
}