package org.redsxi.mc.ctplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.redsxi.mc.ctplus.CardRegistries;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.model.CardItemModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelBakery.class)
@Environment(EnvType.CLIENT)
public class ModelBakeryMixin {
    @Inject(at=@At("HEAD"), method = "loadBlockModel", cancellable = true)
    private void load(ResourceLocation resourceLocation, CallbackInfoReturnable<BlockModel> cir) {
        if (resourceLocation.getPath().startsWith("item/card_")) {
            String id = resourceLocation.getPath().substring(10);
            ResourceLocation cardLocation = new ResourceLocation(resourceLocation.getNamespace(), id);
            Card<?, ?> card = CardRegistries.CARD.get(cardLocation);
            BlockModel model = BlockModel.fromString(CardItemModelGenerator.INSTANCE.generate(card.getCardItemTextureLocation()));
            cir.setReturnValue(model);
        }
    }
}
