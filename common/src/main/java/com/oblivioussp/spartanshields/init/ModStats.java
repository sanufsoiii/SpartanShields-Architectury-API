package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class ModStats {
   public static final ResourceLocation SHIELD_BASH_HITS = new ResourceLocation(SpartanShields.ID, "shield_bash_hits");

   public static void register() {
      registerStat(SHIELD_BASH_HITS, StatFormatter.DEFAULT);
   }

   private static void registerStat(ResourceLocation statNameIn, StatFormatter formatterIn) {
      Registry.register(BuiltInRegistries.CUSTOM_STAT, statNameIn, statNameIn);
      Stats.CUSTOM.get(statNameIn);
   }

   private ModStats() {
   }
}
