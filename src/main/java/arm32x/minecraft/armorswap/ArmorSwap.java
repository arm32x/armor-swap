package arm32x.minecraft.armorswap;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public final class ArmorSwap implements ClientModInitializer {
	public static ArmorSwapConfig config;

	@Override
	public void onInitializeClient() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
			ConfigScreenBuilder.setMain("armor-swap", new ClothConfigScreenBuilder());
		}

		config = new ArmorSwapConfig();
		config.load();
	}
}
