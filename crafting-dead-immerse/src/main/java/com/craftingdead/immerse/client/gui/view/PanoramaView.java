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

import java.util.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;

public class PanoramaView extends View {

  private final PanoramaRenderer panorama;

  private final ResourceLocation panoramaTexture;

  public PanoramaView(Properties<?> properties, ResourceLocation panoramaTexture) {
    super(properties);
    Objects.requireNonNull(panoramaTexture, "Panorama texture cannot be null");
    this.panoramaTexture = panoramaTexture;
    this.panorama = new PanoramaRenderer(new CubeMap(this.panoramaTexture));
  }

  @Override
  public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    this.panorama.render(partialTicks, this.getAlpha());
  }
}
