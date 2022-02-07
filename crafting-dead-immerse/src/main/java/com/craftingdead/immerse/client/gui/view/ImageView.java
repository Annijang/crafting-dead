/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.gui.view;

import java.util.Optional;
import javax.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class ImageView extends View {

  private boolean depthTest = false;
  private boolean bilinearFiltering = false;

  private final StyleableProperty<Color> color =
      Util.make(StyleableProperty.create("color", Color.class, Color.WHITE),
          this::registerValueProperty);

  private final StyleableProperty<FitType> objectFit =
      Util.make(StyleableProperty.create("object-fit", FitType.class, FitType.FILL),
          this::registerValueProperty);

  private ResourceLocation image = TextureManager.INTENTIONAL_MISSING_TEXTURE;

  private Vec2 fittedImageSize;

  public ImageView(Properties<?> properties) {
    super(properties);
  }

  @Override
  protected void setLayout(@Nullable Layout layout) {
    super.setLayout(layout);
    if (layout != null) {
      layout.setMeasureFunction(this::measure);
    }
  }

  public StyleableProperty<Color> getColorProperty() {
    return this.color;
  }

  public final ImageView setImage(ResourceLocation image) {
    this.image = image;
    if (this.hasLayout()) {
      this.getLayout().markDirty();
    }
    return this;
  }

  public final ImageView setDepthTest(boolean depthTest) {
    this.depthTest = depthTest;
    return this;
  }

  public final ImageView setBilinearFiltering(boolean bilinearFiltering) {
    this.bilinearFiltering = bilinearFiltering;
    return this;
  }

  private Optional<Vec2> getImageSize() {
    if (this.image != null) {
      this.minecraft.getTextureManager().bindForSetup(this.image);
      return Optional.of(new Vec2(
          GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH),
          GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)));
    }
    return Optional.empty();
  }

  private Optional<Vec2> getFittedImageSize() {
    return this.getFittedImageSize(this.getContentWidth(), this.getContentHeight());
  }

  private Optional<Vec2> getFittedImageSize(float containerWidth, float containerHeight) {
    return this.getImageSize().map(imageSize -> this.objectFit.get()
        .getSize(imageSize.x, imageSize.y, containerWidth, containerHeight));
  }

  @Override
  public void layout() {
    super.layout();
    this.fittedImageSize = this.getFittedImageSize().orElse(null);
  }

  private Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height) {
    return this.getFittedImageSize(widthMode == MeasureMode.UNDEFINED ? Integer.MAX_VALUE : width,
        heightMode == MeasureMode.UNDEFINED ? Integer.MAX_VALUE : height)
        .orElse(new Vec2(width, height));
  }

  @Override
  public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    if (this.depthTest) {
      RenderSystem.enableDepthTest();
    }
    final var colour = this.color.get().getValue4f();
    RenderSystem.setShaderColor(colour[0], colour[1], colour[2], colour[3] * this.getAlpha());
    if (this.image != null) {
      if (this.bilinearFiltering) {
        this.minecraft.getTextureManager().getTexture(this.image).setFilter(true, true);
      }

      RenderSystem.setShaderTexture(0, this.image);
      RenderUtil.blit(matrixStack, this.getScaledContentX(), this.getScaledContentY(),
          this.fittedImageSize.x * this.getXScale(), this.fittedImageSize.y * this.getYScale());
    } else {
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderUtil.fill(matrixStack, this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledX() + this.getScaledContentWidth(),
          this.getScaledContentY() + this.getScaledContentHeight(),
          0xFFFFFFFF);
    }
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    if (this.depthTest) {
      RenderSystem.disableDepthTest();
    }
    RenderSystem.disableBlend();
  }
}
