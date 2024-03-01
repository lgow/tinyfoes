package net.tinyfoes.forge.networking;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayerData {
	private static boolean isBaby, isBabyfied;

	public static boolean isBaby() {
		return isBaby;
	}

	public static void setBaby(boolean isBaby) {
		PlayerData.isBaby = isBaby;
	}

	public static boolean isBabyfied() {
		return isBabyfied;
	}

	public static void setBabyfied(boolean isBabyfied) {
		PlayerData.isBabyfied = isBabyfied;
	}
}
