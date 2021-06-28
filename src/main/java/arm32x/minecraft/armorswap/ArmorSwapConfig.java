package arm32x.minecraft.armorswap;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigContainer.Transitive;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import me.lortseam.completeconfig.data.Config;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;

public final class ArmorSwapConfig extends Config implements ConfigContainer {
	@Transitive
	public Trigger trigger = new Trigger();

	public ArmorSwapConfig() {
		super("armor-swap");
	}

	@ConfigEntries
	public static class Trigger implements ConfigGroup {
		@ConfigEntry.Dropdown
		public SlotActionType actionType = SlotActionType.PICKUP;
		public ClickType mouseButton = ClickType.RIGHT;
	}
}
