package net.bfybf.tradeloot.event;

import net.bfybf.tradeloot.config.CommonConfig;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.bfybf.tradeloot.Tradeloot.NOTARDELOOT;

public class VillagerDropsHandler {

    @SubscribeEvent
    public static void onMobDropsLoot(LivingDropsEvent event) {
        if (CommonConfig.EnableVillagerDrops()) {
            final LivingEntity entity = event.getEntity();
            final DamageSource source = event.getSource();
            final Level level = entity.level();
            if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                final Entity killer = source.getEntity();
                if (!(killer instanceof  Player) && CommonConfig.RequirePlayer()){
                    return;
                }
                if (entity instanceof AbstractVillager villager && !entity.isBaby()) {
                    final MerchantOffers offers = villager.getOffers();
                    int villagerlevel = 1;
                    SimpleContainer villagerinventory = null;
                    if (villager instanceof Villager realvillager) {
                        villagerlevel = realvillager.getVillagerData().getLevel();
                        villagerinventory = realvillager.getInventory();
                    }
                    int drops = 0;
                    for (MerchantOffer offer : offers) {
                        if (!offer.isOutOfStock() && level.random.nextDouble() < Math.min(CommonConfig.DropsChance() + event.getLootingLevel() * CommonConfig.LootingBonus() , 1)) {
                            if(CommonConfig.DropsNumber() == 0 || drops < villagerlevel * CommonConfig.DropsNumber()) {
                                ItemStack itemStack = offer.getResult().copy();
                                if(!itemStack.is(NOTARDELOOT)) {
                                    event.getDrops().add(new ItemEntity(level, villager.getOnPos().getX(), villager.getOnPos().getY() + 1, villager.getOnPos().getZ(), itemStack));
                                    drops += 1;
                                }
                            }
                        }
                    }

                    if (CommonConfig.InvDropsChance() > 0.0 && villager instanceof Villager){
                        for(ItemStack inv : villagerinventory.removeAllItems()){
                            int invCount = inv.getCount();
                            int amount = 0;
                            for(int i=0 ;i < invCount; i++)
                            {
                                if(level.random.nextDouble() < CommonConfig.InvDropsChance()){
                                    amount++;
                                }
                            }
                            inv.setCount(amount);
                            event.getDrops().add(new ItemEntity(level, villager.getOnPos().getX(), villager.getOnPos().getY() + 1, villager.getOnPos().getZ(), inv));
                        }
                    }

                }
            }
        }
    }
}
