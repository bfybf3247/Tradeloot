package net.bfybf.tradeloot.mixin;

import net.bfybf.tradeloot.config.CommonConfig;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class VillagerMixin {
    @Inject(method = "rewardTradeXp" ,at = @At("TAIL"))
    protected void AfterTrade(MerchantOffer offer, CallbackInfo ci){
        if(CommonConfig.AddInventory()) {
            final ItemStack CostA = offer.getCostA();
            final SimpleContainer villagerinventory = ((Villager) (Object) (this)).getInventory();
            if (villagerinventory.canAddItem(CostA)) {
                villagerinventory.addItem(CostA);
            }
        }
    }
}
