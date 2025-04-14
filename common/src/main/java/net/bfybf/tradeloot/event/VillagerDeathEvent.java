package net.bfybf.tradeloot.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.bfybf.tradeloot.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Vindicator;
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

import java.util.Arrays;

import static net.bfybf.tradeloot.Tradeloot.NOTARDELOOT;
import static net.minecraft.world.entity.EntityType.VINDICATOR;

public class VillagerDeathEvent {

    private enum PunishmentType {
        LIGHTNING(Config.PunishLightingWeight),
        IRON_GOLEM(Config.PunishIronManWeight),
        VINDICATOR(Config.PunishJohnnyWeight);

        final double weight;
        PunishmentType(double weight) {
            this.weight = weight;
        }
    }

    public VillagerDeathEvent() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            final Level level = entity.level();
            if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) || !Config.enableVillagerDrops) {
                return EventResult.interruptDefault();
            }

            final LivingEntity killer = (LivingEntity) source.getEntity();
            if (!(killer instanceof Player) && Config.requirePlayer) {
                return EventResult.interruptDefault();
            }

            if (entity instanceof AbstractVillager villager) {
                if (killer instanceof Player player) {
                    applyPunishment(level, villager, player);
                }
                int lootinglevel = (killer != null) ? EnchantmentHelper.getItemEnchantmentLevel(Enchantments.LOOTING, killer.getMainHandItem()) : 0;

                if (villager instanceof Villager realvillager) {
                    int villagerlevel = realvillager.getVillagerData().getLevel();
                    dropInventoryItems(level, entity, realvillager);
                    dropPotatoForNitwit(level, entity, realvillager, killer);
                    dropTradeItems(level, entity, realvillager.getOffers(), villagerlevel, lootinglevel);
                } else {
                    dropTradeItems(level, entity, villager.getOffers(), 1, lootinglevel);
                }
            }

            return EventResult.interruptDefault();
        });
    }

    private void applyPunishment(Level level, AbstractVillager villager, Player player) {
        boolean isBaby = villager.isBaby();
        double penaltyChance = Config.PenaltyChance * (isBaby ? Config.BabyPenaltyMultiplier : 1.0);

        if (level.random.nextDouble() >= penaltyChance) {
            return;
        }

        double totalWeight = Arrays.stream(PunishmentType.values())
                .mapToDouble(t -> t.weight).sum();
        double r = level.random.nextDouble() * totalWeight;
        PunishmentType type = null;
        double cumulative = 0;
        for (PunishmentType t : PunishmentType.values()) {
            cumulative += t.weight;
            if (r < cumulative) {
                type = t;
                break;
            }
        }

        if (type == null) return;

        switch (type) {
            case LIGHTNING -> {
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                lightningBolt.setPos(player.getX(), player.getY(), player.getZ());
                level.addFreshEntity(lightningBolt);
            }
            case IRON_GOLEM -> {
                IronGolem ironGolem = new IronGolem(EntityType.IRON_GOLEM, level);
                ironGolem.setPos(villager.getX(), villager.getY(), villager.getZ());
                ironGolem.setTarget(player);
                ironGolem.setCustomName(Component.literal("VillagerMan"));
                ironGolem.setCustomNameVisible(true);
                ironGolem.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 4));
                ironGolem.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 2));
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            ironGolem.getX(), ironGolem.getY() + 1.0, ironGolem.getZ(),
                            80, 1.2, 1.5, 1.2, 0.1);
                }
                level.playSound(null, ironGolem.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.addFreshEntity(ironGolem);
            }
            case VINDICATOR -> {
                Vindicator vindicator = new Vindicator(VINDICATOR, level);
                vindicator.setPos(villager.getX(), villager.getY(), villager.getZ());
                vindicator.setTarget(player);
                vindicator.setCustomName(Component.literal("Johnny"));
                vindicator.setCustomNameVisible(true);
                vindicator.setAggressive(true);
                ItemStack netheriteAxe = new ItemStack(Items.NETHERITE_AXE);
                vindicator.setItemSlot(EquipmentSlot.MAINHAND, netheriteAxe);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                            vindicator.getX(), vindicator.getY() + 1.0, vindicator.getZ(),
                            150, 1.0, 1.5, 1.0, 0.2);
                }
                level.playSound(null, vindicator.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
                level.addFreshEntity(vindicator);
            }
        }
    }

    private void dropInventoryItems(Level level, LivingEntity entity, Villager realvillager) {
        if (Config.invDropsChance <= 0.0) return;
        SimpleContainer villagerinventory = realvillager.getInventory();
        for (ItemStack inventory : villagerinventory.removeAllItems()) {
            int invCount = inventory.getCount();
            if (invCount <= 0) continue;
            int amount = 0;
            for (int i = 0; i < invCount; i++) {
                if (level.random.nextDouble() < Config.invDropsChance) {
                    amount++;
                }
            }
            if (amount > 0) {
                inventory.setCount(amount);
                ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), inventory);
                level.addFreshEntity(itemEntity);
            }
        }
    }

    private void dropPotatoForNitwit(Level level, LivingEntity entity, Villager realvillager, LivingEntity killer) {
        if (realvillager.getVillagerData().getProfession() != VillagerProfession.NITWIT) return;
        if (!(killer instanceof Player)) return;
        if (level.random.nextDouble() >= Config.PotatoChance) return;

        Item potatoitem;
        String appletext;
        try {
            Item[] potatoes = Config.Potatoes.stream()
                    .map(itemId -> BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(itemId)))
                    .toArray(Item[]::new);
            String[] apples = Config.Apples.toArray(new String[0]);
            int validLength = Math.min(potatoes.length, apples.length);
            if (validLength > 0) {
                int seed = level.random.nextInt(validLength);
                potatoitem = potatoes[seed];
                appletext = apples[seed];
            } else {
                potatoitem = Items.POTATO;
                appletext = "item.minecraft.apple";
            }
        } catch (Exception e) {
            potatoitem = Items.POTATO;
            appletext = "item.minecraft.apple";
        }

        ItemStack potato = new ItemStack(potatoitem, 1);
        potato.set(DataComponents.CUSTOM_NAME, Component.translatable(appletext));
        ItemEntity apple = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), potato);
        apple.setCustomName(apple.getItem().getHoverName());
        apple.setCustomNameVisible(true);
        level.addFreshEntity(apple);
    }

    private void dropTradeItems(Level level, LivingEntity entity, MerchantOffers offers, int villagerlevel, int lootinglevel) {
        int bonusdrop = lootinglevel * Config.dropsBonus;
        double dropChance = Math.min(Config.dropsChance, 1);
        double lootingChance = Math.min(Config.lootingBonus, 1);
        int drops = 0;

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
            if (bonusdrop > 0 && level.random.nextDouble() < lootingChance) {
                ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), itemStack);
                level.addFreshEntity(itemEntity);
                bonusdrop -= 1;
            }
        }
    }
}