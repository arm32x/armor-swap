package arm32x.minecraft.armorswap;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ArmorSwap implements ClientModInitializer {
	public static ArmorSwapConfig config;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(ArmorSwapConfig.class, Toml4jConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ArmorSwapConfig.class).getConfig();
	}
}
