package com.lgow.endofherobrine.block;

import net.minecraft.util.StringRepresentable;

public enum TotemStates implements StringRepresentable {
   ACTIVE("active"),
   INACTIVE("inactive"),
   OVERCHARGED("overcharged");

   private final String name;

   TotemStates(String pName) {
      this.name = pName;
   }

   public String getSerializedName() {
      return this.name;
   }
}