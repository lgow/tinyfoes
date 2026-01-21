package github.lgow.tinyfoes.common;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.player.Player;
import github.lgow.tinyfoes.common.client.model.BabificationRayModel;
import github.lgow.tinyfoes.common.client.renderer.BabyficationRayRenderer;
import github.lgow.tinyfoes.common.entity.ModEntities;
import github.lgow.tinyfoes.common.util.TinyFoesResLoc;

@Environment(EnvType.CLIENT)
public class CommonClientTinyFoes {
	public static ModelLayerLocation BABYFICATION_RAY;

	public static void preClientInit() {
		EntityRendererRegistry.register(ModEntities.BABYFICATION_RAY, BabyficationRayRenderer::new);
		BABYFICATION_RAY = new ModelLayerLocation(new TinyFoesResLoc("blob"), "main");
		EntityModelLayerRegistry.register(BABYFICATION_RAY, BabificationRayModel::createBodyLayer);
		EntityRendererRegistry.register(ModEntities.BABYFICATION_RAY, BabyficationRayRenderer::new);
	}

	public static Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}