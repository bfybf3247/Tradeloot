package net.bfybf.tradeloot.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.bfybf.tradeloot.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            if(level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) && Config.enableVillagerDrops) {
                final LivingEntity killer = (LivingEntity) source.getEntity();
                if (!(killer instanceof Player) && Config.requirePlayer) {
                    return EventResult.interruptDefault();
                }
                if (entity instanceof AbstractVillager villager) {

                    final MerchantOffers offers = villager.getOffers();
                    int villagerlevel = 1;
                    int drops = 0;
                    int lootinglevel = 0;
                    if (killer != null) {
                        lootinglevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.LOOTING, killer.getMainHandItem());
                    }

                    if (villager instanceof Villager realvillager) {
                        villagerlevel = realvillager.getVillagerData().getLevel();
                        SimpleContainer villagerinventory = realvillager.getInventory();

                        if (Config.invDropsChance > 0.0) {
                            for (ItemStack inventory : villagerinventory.removeAllItems()) {
                                int invCount = inventory.getCount();
                                if(invCount <= 0) continue;
                                int amount = 0;
                                for (int i = 0; i < invCount; i++) {
                                    if (level.random.nextDouble() < Config.invDropsChance) {
                                        amount++;
                                    }
                                }
                                if(amount > 0)
                                {
                                    inventory.setCount(amount);
                                    ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), inventory);
                                    level.addFreshEntity(itemEntity);
                                }
                            }
                        }

                        if (realvillager.getVillagerData().getProfession() == VillagerProfession.NITWIT && killer instanceof Player) {
                            if (level.random.nextDouble() < Config.PotatoChance) {
                                Item[] potatos = {
                                        Items.POTATO,
                                        Items.POISONOUS_POTATO,
                                        Items.PUFFERFISH,
                                        Items.ROTTEN_FLESH,
                                        Items.SPIDER_EYE,
                                        Items.DIORITE,
                                        Items.LEAD,
                                        Items.BOOK,
                                        Items.KNOWLEDGE_BOOK,
                                        Items.NETHERITE_INGOT
                                };
                                String[] apples = {
                                        "item.minecraft.apple",
                                        "item.minecraft.golden_apple",
                                        "item.minecraft.enchanted_golden_apple",
                                        "item.minecraft.cooked_beef",
                                        "item.minecraft.ender_eye",
                                        "block.minecraft.diorite",
                                        "entity.minecraft.wandering_trader",
                                        "enchantment.minecraft.mending",
                                        "lectern.take_book",
                                        "item.minecraft.nether_brick"
                                };
                                int seed = level.random.nextInt(potatos.length);
                                ItemStack potato = new ItemStack(potatos[seed], 1);
                                potato.set(DataComponents.CUSTOM_NAME, Component.translatable(apples[seed]));
                                ItemEntity apple = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), potato);
                                apple.setCustomName(apple.getItem().getHoverName());
                                apple.setCustomNameVisible(true);
                                level.addFreshEntity(apple);
                            }
                        }
                    }

                    int bonusdrop = lootinglevel * Config.dropsBonus;
                    double dropChance = Math.min(Config.dropsChance, 1);
                    double lootingChance = Math.min(Config.lootingBonus, 1);

                    for (MerchantOffer offer : offers) {
                        ItemStack itemStack = offer.getResult().copy();
                        if (offer.isOutOfStock() || itemStack.is(NOTARDELOOT)) continue;
                        if (Config.dropsNumber == -1 || drops < villagerlevel * Config.dropsNumber) {
                            if (level.random.nextDouble() < dropChance) {
                                ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), itemStack);
                                level.addFreshEntity(itemEntity);
                                drops += 1;
                            }
                        }
                        if(bonusdrop > 0 && level.random.nextDouble() < lootingChance){
                            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), itemStack);
                            level.addFreshEntity(itemEntity);
                            bonusdrop -= 1;
                        }
                    }

                }
            }
            return EventResult.interruptDefault();
        });
    }

}