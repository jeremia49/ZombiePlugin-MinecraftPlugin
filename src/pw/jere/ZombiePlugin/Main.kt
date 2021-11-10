package pw.jere.ZombiePlugin


import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(){


    override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(Zombie(this),this)
        server.pluginManager.registerEvents(Skeleton(this),this)
    }

    override fun onDisable() {
        super.onDisable()
    }

}