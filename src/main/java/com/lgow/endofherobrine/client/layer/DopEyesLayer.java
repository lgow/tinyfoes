package com.lgow.endofherobrine.client.layer;

import com.lgow.endofherobrine.entity.herobrine.Doppelganger;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DopEyesLayer<T extends Doppelganger, M extends PlayerModel<T>> extends WhiteEyesLayer<T, M> {

    public DopEyesLayer(RenderLayerParent<T, M> rendererIn) { super(rendererIn);}

    public String getEyeTexture(Doppelganger dop) {
//        AbstractClientPlayer localPlayer = dop.getClientPlayer();
//        PlayerEyes playerEyes = localPlayer.getPlayerEyes();
//        if (localPlayer != null) {
//            switch (playerEyes) {
//                case DEFAULT:
//                default:
//                    return "textures/entity/player/default_eyes.png";
//                case SQUARE:
//                    return "textures/entity/player/square_eyes.png";
//                case VERTICAL:
//                    return "textures/entity/player/vertical_eyes.png";
//                case LOW:
//                    return "textures/entity/player/low_default_eyes.png";
//                case LOW_SQUARE:
//                    return "textures/entity/player/low_square_eyes.png";
//                case CUSTOM:
//                    return "textures/entity/player/" + localPlayer.getUUID() + ".png";
//            }
//        }
//        else {
            return "textures/entity/layer/biped_eyes.png";
//        }
    }
}
