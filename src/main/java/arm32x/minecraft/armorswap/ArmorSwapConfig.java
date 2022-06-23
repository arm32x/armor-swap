package arm32x.minecraft.armorswap;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;

@Config(name = "armor-swap")
public final class ArmorSwapConfig implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject()
	public Trigger trigger = new Trigger();

	public static class Trigger {
		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
		public SlotActionType actionType = SlotActionType.PICKUP;

		@ConfigEntry.Gui.PrefixText
		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
		public ClickType mouseButton = ClickType.RIGHT;
	}
}
