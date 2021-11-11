package pw.jere.ZombiePlugin


import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.Random


class Zombie (val main:Main) : Listener {
    val maxZombieSpawnRate = 0.2f;
    val maxFullSpawnDate = 100f;

    @EventHandler
    private fun onZombieSpawn(e : CreatureSpawnEvent){
        if(e.entity is Zombie){

            val day : Int = (e.entity.world.fullTime/24000f).toInt()
            val rate : Float = if(day >= 100f) {
                maxZombieSpawnRate
                }else{
                    day.toFloat() / maxFullSpawnDate * maxZombieSpawnRate
                }

            if(rand() > rate) return;

            val zType = zombieType()
            if(zType == ZombieTier.NOTHING){
                return
            }

            val axeMaterial : Material;

            if(zType == ZombieTier.TIER_1){
                val typeRand = rand()
                axeMaterial = if(typeRand < 0.5f){
                        Material.WOODEN_AXE
                    }else{
                        Material.STONE_AXE
                    }

            }else{
                val typeRand = rand()
                axeMaterial = if(typeRand < 0.5f){
                        Material.STONE_AXE
                    }else if(typeRand < 0.8){
                        Material.IRON_AXE
                    }else{
                        Material.DIAMOND_AXE
                    }
            }

            val axeStack : ItemStack = ItemStack(axeMaterial,1)
            axeStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, rand(1,3))
            e.entity.equipment?.setItemInMainHand(axeStack)
            e.entity.customName = ChatColor.RED.toString() + "Juggernaut Zombie"
            e.entity.isCustomNameVisible = true

            if(zType == ZombieTier.TIER_2){
                e.entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW,3600,1))
                e.entity.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,3600,1))
            }

        }
    }

    @EventHandler
    private fun onZombieDeath(e:EntityDeathEvent){
        if(e is Zombie){
            if(e.customName.equals(ChatColor.RED.toString() + "Juggernaut Zombie")){
                e.drops.clear()
            }
        }
    }

    private fun rand():Float{
        return ( 1 + Random().nextInt(100)).toFloat()/ 100
    }

    private fun rand(lower:Int, until:Int) : Int{
        return ( lower + Random().nextInt(until))
    }



    private fun zombieType() : ZombieTier{
        val zombieRand : Float = rand()

        //NOTHING = 50

        if(zombieRand  < 0.5f ){
            return ZombieTier.NOTHING
        }else{

            //Tier 1 = 70
            //Tier 2 = 30

            val tierRand : Float = rand()
            if(tierRand < 0.7f){
                return ZombieTier.TIER_1
            }
            return ZombieTier.TIER_2
        }
    }



}