package net.bfybf.tradeloot.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.bfybf.tradeloot.config.Config;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import static net.bfybf.tradeloot.Tradeloot.NOTARDELOOT;

public class VillagerDeathEvent {
    public VillagerDeathEvent(){
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            final Level level = entity.level();
            if(level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) && Config.enableVillagerDrops)
            {
                final LivingEntity killer = (LivingEntity) source.getEntity();
                if (!(killer instanceof Player) && Config.requirePlayer){
                    return EventResult.interruptDefault();
                }
                if(entity instanceof AbstractVillager villager && !entity.isBaby()){

                    final MerchantOffers offers = villager.getOffers();
                    int villagerlevel = 1;
                    int drops = 0;
                    int lootinglevel = 0;
                    if (killer != null) {
                        lootinglevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, killer.getMainHandItem());
                    }

                    SimpleContainer villagerinventory = null;

                    if (villager instanceof Villager realvillager) {
                        villagerlevel = realvillager.getVillagerData().getLevel();
                        villagerinventory = realvillager.getInventory();

                        if (Config.invDropsChance > 0.0){
                            for(ItemStack inventory : villagerinventory.removeAllItems()){
                                int invCount = inventory.getCount();
                                int amount = 0;
                                for(int i=0 ;i < invCount; i++)
                                {
                                    if(level.random.nextDouble() < Config.invDropsChance){
                                        amount++;
                                    }
                                }
                                inventory.setCount(amount);
                                ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), inventory);
                                level.addFreshEntity(itemEntity);
                                drops += 1;
                            }
                        }
                    }


                    
                    for(MerchantOffer offer : offers){
                        if (!offer.isOutOfStock() && level.random.nextDouble() < Math.min(Config.dropsChance + lootinglevel * Config.lootingBonus , 1)) {
                            if(Config.dropsNumber == 0 || drops < villagerlevel * Config.dropsNumber) {
                                ItemStack itemStack = offer.getResult().copy();
                                if(!itemStack.is(NOTARDELOOT)){
                                    ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), itemStack);
                                    level.addFreshEntity(itemEntity);
                                    drops += 1;
                                }
                            }
                        }
                    }
                }
            }




            return EventResult.interruptDefault();
        });
    }

}
