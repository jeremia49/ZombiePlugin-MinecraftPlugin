package pw.jere.ZombiePlugin

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable
import kotlin.random.Random

enum class ArmorHierarcy(val code : Int){
    NOTHING(0),
    LEATHER(1),
    STONE(2),
    IRON(3),
    DIAMOND(4),
}

enum class SwordHierarcy(val code:Int){
    NOTHING(0),
    STONE(1),
    IRON(2),
    DIAMOND(3),
}

class Zombie (val main:Main) : Listener {
    val maxSkeletonSpawnRate = 0.2f;
    val maxFullSpawnDate = 100f;
//
//    @EventHandler
//    private fun onPlayerMove(e : PlayerMoveEvent){
//        val from : Location = e.from
//        val to : Location? = e.to
//        if (to != null) {
//            if(from.blockX == to.blockX && from.blockY == to.blockY && from.blockZ == to.blockZ){
//                return
//            }
//            main.server.broadcastMessage("Game Time : " + (e.entity.world.fullTime/24000).toString())
//        }
//    }

    @EventHandler
    private fun onZombieSpawn(e : CreatureSpawnEvent){
        if(e.entity is Skeleton){

            val day : Int = (e.entity.world.fullTime/24000f).toInt()
            val rate : Float = if(day >= 100f) {
                maxSkeletonSpawnRate
                }else{
                    day.toFloat() / maxFullSpawnDate * maxSkeletonSpawnRate
                }
            main.server.broadcastMessage("Day is " + day.toString())
            main.server.broadcastMessage("Probabilty is " + rate.toString())

            if(rand() > rate) return;

            if(rand() < 0.3f){
                e.entity.world.spawnEntity(e.entity.location, EntityType.WITHER_SKELETON)
                e.entity.remove()
            }

            val swordrandom = randomSwordType()

            if(swordrandom.equals(SwordHierarcy.NOTHING)){
                //Archer Skeleton

                val bow : Material = Material.BOW
                val armorHelmet: Material
                val armorBoots: Material
                val armorChestplate: Material
                val armorLeggings: Material

                var rand = rand()
                armorHelmet = if( rand < 0.3f){
                        Material.CHAINMAIL_HELMET
                    }else{
                        Material.LEATHER_HELMET
                    }

                rand = rand()
                armorBoots = if( rand < 0.3f){
                    Material.CHAINMAIL_BOOTS
                }else{
                    Material.LEATHER_BOOTS
                }

                rand = rand()
                armorChestplate = if( rand < 0.3f){
                    Material.CHAINMAIL_CHESTPLATE
                }else{
                    Material.LEATHER_CHESTPLATE
                }

                rand = rand()
                armorLeggings = if( rand < 0.3f){
                    Material.CHAINMAIL_LEGGINGS
                }else{
                    Material.LEATHER_LEGGINGS
                }


                val bowStack : ItemStack = ItemStack(bow,1)
                bowStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE,Random.nextInt(1,4))
                bowStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE,Random.nextInt(0,1))
                bowStack.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK,Random.nextInt(1,2))


                val armorHelmetStack : ItemStack = ItemStack(armorHelmet,1)
                val armorBootsStack : ItemStack = ItemStack(armorBoots,1)
                val armorChestplateStack: ItemStack= ItemStack(armorChestplate,1)
                val armorLeggingsStack: ItemStack = ItemStack(armorLeggings,1)

                armorHelmetStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                armorHelmetStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))

                armorBootsStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                armorBootsStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))

                armorChestplateStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                armorChestplateStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))

                armorLeggingsStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                armorLeggingsStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))

                e.entity.equipment?.setItemInMainHand(bowStack)
                e.entity.equipment?.helmet = armorHelmetStack
                e.entity.equipment?.boots = armorBootsStack
                e.entity.equipment?.chestplate = armorChestplateStack
                e.entity.equipment?.leggings = armorLeggingsStack



            }else{
                //Warrior Skeleton
                var sword : Material? = null
                var armorHelmet : Material? = null
                var armorBoots: Material? = null
                var armorChestplate: Material? = null
                var armorLeggings: Material? = null


                if(swordrandom.equals(SwordHierarcy.STONE)){
                    sword = Material.STONE_SWORD


                    if(rand70Chance()){
                        armorHelmet = Material.LEATHER_HELMET
                    }
                    if(rand70Chance()){
                        armorBoots = Material.LEATHER_BOOTS
                    }
                    if(!rand70Chance()){
                        armorChestplate = Material.LEATHER_CHESTPLATE
                    }
                    if(!rand70Chance()){
                        armorLeggings = Material.LEATHER_LEGGINGS
                    }

                }else if(swordrandom.equals(SwordHierarcy.IRON)){
                    sword = Material.IRON_SWORD

                    armorHelmet = if(rand70Chance()){
                        Material.LEATHER_HELMET
                    }else{
                        Material.IRON_HELMET
                    }
                    armorBoots = if(rand70Chance()){
                        Material.LEATHER_BOOTS
                    }else{
                        Material.IRON_BOOTS
                    }
                    armorChestplate = if(rand70Chance()){
                        Material.LEATHER_CHESTPLATE
                        }else{
                            Material.IRON_CHESTPLATE
                        }
                    armorLeggings = if(rand70Chance()){
                        Material.LEATHER_LEGGINGS
                        }else {
                        Material.IRON_LEGGINGS
                    }

                }else if(swordrandom.equals(SwordHierarcy.DIAMOND)){
                    sword = Material.DIAMOND_SWORD

                    armorHelmet = if(rand70Chance()){
                        Material.IRON_HELMET
                    }else{
                        Material.DIAMOND_HELMET
                    }
                    armorBoots = if(rand70Chance()){
                        Material.IRON_BOOTS
                    }else{
                        Material.DIAMOND_BOOTS
                    }
                    armorChestplate = if(rand70Chance()){
                        Material.IRON_CHESTPLATE
                    }else{
                        Material.DIAMOND_CHESTPLATE
                    }
                    armorLeggings = if(rand70Chance()){
                        Material.IRON_LEGGINGS
                    }else {
                        Material.DIAMOND_LEGGINGS
                    }

                    if(rand() < 0.05f){
                        armorHelmet = Material.DIAMOND_HELMET
                        armorBoots = Material.DIAMOND_BOOTS
                        armorChestplate = Material.DIAMOND_CHESTPLATE
                        armorLeggings = Material.DIAMOND_LEGGINGS
                    }

                }

                val swordStack : ItemStack = ItemStack(sword!!,1)
                swordStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,Random.nextInt(1,3))
                e.entity.equipment?.setItemInMainHand(swordStack)

                if(armorHelmet != null) {
                    val armorHelmetStack: ItemStack = ItemStack(armorHelmet, 1)
                    armorHelmetStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                    armorHelmetStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))
                    e.entity.equipment?.helmet = armorHelmetStack
                }

                if(armorBoots != null){
                    val armorBootsStack : ItemStack = ItemStack(armorBoots,1)
                    armorBootsStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                    armorBootsStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))
                    e.entity.equipment?.boots = armorBootsStack
                }

                if(armorChestplate != null){
                    val armorChestplateStack: ItemStack= ItemStack(armorChestplate,1)
                    armorChestplateStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                    armorChestplateStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))
                    e.entity.equipment?.chestplate = armorChestplateStack
                }

                if(armorLeggings != null){
                    val armorLeggingsStack: ItemStack = ItemStack(armorLeggings,1)
                    armorLeggingsStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Random.nextInt(2,4))
                    armorLeggingsStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Random.nextInt(1,3))
                    e.entity.equipment?.leggings = armorLeggingsStack

                }


            }

        }
    }

    private fun rand():Float{
        return (Random.nextInt(1,100)).toFloat()/ 100
    }

    private fun rand70Chance():Boolean{
        if(rand() < 0.7f){
            return true
        }
        return false
    }

    private fun rand50Chance():Boolean{
        if(rand() < 0.5f){
            return true
        }
        return false
    }


    private fun randomSwordType() : SwordHierarcy{
        val useArmor : Float = (Random.nextInt(1,100)).toFloat()/ 100

        //NOArmor = 50
        //useArmor = 50

        //nothing = 0
        //leather = 1
        //stone = 2
        //iron = 3
        //diamond = 4

        if(useArmor  < 0.5f ){
            //tanpa sword
            return SwordHierarcy.NOTHING
        }else{
            //dengan sword
            val armorRand : Float = (Random.nextInt(1,100)).toFloat()/ 100
            if(armorRand < 0.5){
                return SwordHierarcy.STONE
            }else if(armorRand < 0.8){
                return SwordHierarcy.IRON
            }
            return SwordHierarcy.DIAMOND
        }
    }



}