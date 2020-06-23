package com.craftingdead.core.capability.living.player;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Util;

public class SelfPlayer extends DefaultPlayer<ClientPlayerEntity> {

  private static final int DOUBLE_CLICK_DURATION = 500;

  private boolean wasHoldingSneakKey;
  private long lastSneakPressTime;
  private boolean crouching;

  public SelfPlayer(ClientPlayerEntity entity) {
    super(entity);
  }

  public void updateMetadata(int daysSurvived, int zombiesKilled, int playersKilled, int water,
      int maxWater, int stamina, int maxStamina) {
    this.daysSurvived = daysSurvived;
    this.zombiesKilled = zombiesKilled;
    this.playersKilled = playersKilled;
    this.water = water;
    this.maxWater = maxWater;
    this.stamina = stamina;
    this.maxStamina = maxStamina;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.entity.isHoldingSneakKey() != this.wasHoldingSneakKey) {
      if (this.entity.isHoldingSneakKey()) {
        final long currentTime = Util.milliTime();
        if (currentTime - this.lastSneakPressTime <= DOUBLE_CLICK_DURATION) {
          this.crouching = true;
        }
        this.lastSneakPressTime = Util.milliTime();
      } else {
        this.crouching = false;
      }
      this.wasHoldingSneakKey = this.entity.isHoldingSneakKey();
    }

    if (this.crouching) {
      this.entity.setPose(Pose.SWIMMING);
    }
  }
}
