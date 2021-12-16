package arm32x.minecraft.armorswap.mixin;

import arm32x.minecraft.armorswap.ArmorSwap;
import java.util.List;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import static net.minecraft.entity.LivingEntity.getPreferredEquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;
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
		// Avoid infinite recursion by checking the stack. This is done because
		// I can’t add parameters using Mixin.
		List<StackWalker.StackFrame> frames = StackWalker.getInstance().walk(stream ->
			stream.dropWhile(frame -> !frame.getClassName().equals("net.minecraft.client.network.ClientPlayerInteractionManager") || !frame.getMethodName().equals("clickSlot"))
				.limit(2)
				.collect(Collectors.toList()));
		if (frames.size() >= 2 && frames.get(1).getClassName().equals("net.minecraft.client.network.ClientPlayerInteractionManager") && frames.get(1).getMethodName().endsWith("onSlotClick")) {
			return;
		}

		if (actionType == ArmorSwap.config.trigger.actionType && button == (ArmorSwap.config.trigger.mouseButton == ClickType.LEFT ? 0 : 1) && slotId >= 0) {
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
