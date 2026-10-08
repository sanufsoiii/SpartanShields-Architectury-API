package com.oblivioussp.spartanshields.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

/**
 * Porting note: replaces Forge's {@code RegisterKeyMappingsEvent} with Architectury's
 * {@link KeyMappingRegistry}, which registers on both loaders.
 */
public class ModKeyBinds {
   public static final String CATEGORY = "key.categories.spartanshields";
   public static final KeyMapping KEY_ALT_SHIELD_BASH = new KeyMapping(
         "key.spartanshields.alt_shield_bash", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);

   public static void register() {
      KeyMappingRegistry.register(KEY_ALT_SHIELD_BASH);
   }

   private ModKeyBinds() {
   }
}
