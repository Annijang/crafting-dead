/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.equipment;

public class SimpleHat implements Hat {

  private final boolean waterBreathing;
  private final boolean nightVision;
  private final float headshotReductionPercentage;
  private final boolean immuneToFlashes;

  public SimpleHat(
      boolean waterBreathing, boolean nightVision,
      float headshotReductionPercentage,
      boolean immuneToFlashes) {
    this.waterBreathing = waterBreathing;
    this.nightVision = nightVision;
    this.headshotReductionPercentage = headshotReductionPercentage;
    this.immuneToFlashes = immuneToFlashes;
  }

  @Override
  public boolean waterBreathing() {
    return this.waterBreathing;
  }

  @Override
  public boolean nightVision() {
    return this.nightVision;
  }

  @Override
  public float headshotReductionPercentage() {
    return this.headshotReductionPercentage;
  }

  @Override
  public boolean immuneToFlashes() {
    return this.immuneToFlashes;
  }
}
