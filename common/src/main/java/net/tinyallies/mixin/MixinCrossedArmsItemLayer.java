package net.tinyallies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(CrossedArmsItemLayer.class)
public abstract class MixinCrossedArmsItemLayer <T extends LivingEntity, M extends EntityModel<T>>
		extends RenderLayer<T, M>{
	@Shadow @Final private ItemInHandRenderer itemInHandRenderer;

	public MixinCrossedArmsItemLayer(RenderLayerParent<T, M> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		poseStack.pushPose();
		if(this.getParentModel().young){
			poseStack.translate(0.0, 1.1f, -0.2f);
		}else{
			poseStack.translate(0.0, 0.4f, -0.4f);
		}
		poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
		ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.MAINHAND);
		itemInHandRenderer.renderItem(livingEntity, itemStack, ItemTransforms.TransformType.GROUND, false, poseStack, multiBufferSource, i);
		poseStack.popPose();
	}
}
