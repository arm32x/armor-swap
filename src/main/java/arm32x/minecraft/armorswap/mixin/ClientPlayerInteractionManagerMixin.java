package arm32x.minecraft.armorswap.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import static net.minecraft.entity.LivingEntity.getPreferredEquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	@Shadow public abstract void clickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity playerEntity);

	@Inject(method = "clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"), cancellable = true)
	public void onSlotClick(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity playerEntity, CallbackInfo ci) {
		if (actionType == SlotActionType.PICKUP && button == 1) {
			Slot slot = playerEntity.currentScreenHandler.slots.get(slotId);
			ItemStack stack = slot.getStack();
			if (stack.getItem() instanceof Wearable) {
				int targetSlotId = 8 - getPreferredEquipmentSlot(stack).getEntitySlotId();
				if (slotId == targetSlotId) {
					clickSlot(syncId, slotId, 0, SlotActionType.QUICK_MOVE, playerEntity);
				} else {
					clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, playerEntity);
					clickSlot(syncId, targetSlotId, 0, SlotActionType.PICKUP, playerEntity);
					clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, playerEntity);
				}
				ci.cancel();
			}
		}
	}
}
