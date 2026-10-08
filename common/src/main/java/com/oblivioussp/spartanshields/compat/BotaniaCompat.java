package com.oblivioussp.spartanshields.compat;

import com.oblivioussp.spartanshields.util.Constants;
import com.oblivioussp.spartanshields.util.Log;
import com.oblivioussp.spartanshields.util.TierSS;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Botania integration without a compile-time dependency.
 * <p>
 * Porting note: the Forge release declared Botania classes directly
 * ({@code BotaniaAPI}, {@code ManaItemHandler}, {@code ToolCommons}, {@code PixieHandler}) and had
 * no Fabric-compatible artifact to point at, which made the Botania shields impossible to port
 * as-is. Botania ships different artifacts per loader, so the calls are resolved reflectively
 * once and every entry point degrades to a no-op when Botania is absent or its API moved.
 */
public final class BotaniaCompat {
   private static final String API_CLASS = "vazkii.botania.api.BotaniaAPI";
   private static final String MANA_HANDLER_CLASS = "vazkii.botania.api.mana.ManaItemHandler";
   private static final String PIXIE_HANDLER_CLASS = "vazkii.botania.common.handler.PixieHandler";

   private static final Supplier<Method> MANA_INSTANCE = method(MANA_HANDLER_CLASS, "instance");
   private static final Supplier<Method> REQUEST_MANA = method(MANA_HANDLER_CLASS, "requestManaExactForTool",
         ItemStack.class, Player.class, int.class, boolean.class);
   private static final Supplier<Method> BOTANIA_INSTANCE = method(API_CLASS, "instance");
   private static final Supplier<Field> PIXIE_CHANCE = field(PIXIE_HANDLER_CLASS, "PIXIE_SPAWN_CHANCE");
   private static final Supplier<Method> PIXIE_MODIFIER = method(PIXIE_HANDLER_CLASS, "makeModifier",
         EquipmentSlot.class, String.class, double.class);

   private BotaniaCompat() {
   }

   public static boolean isLoaded() {
      return Platform.isModLoaded(Constants.BOTANIA_MODID);
   }

   /** Botania's tier, or {@code fallback} when Botania is unavailable. */
   public static TierSS tier(String getterName, TierSS fallback) {
      Method instance = BOTANIA_INSTANCE.get();
      Method getter = method(API_CLASS, getterName).get();
      if (instance == null || getter == null) {
         return fallback;
      }
      try {
         Object api = instance.invoke(null);
         Object tier = getter.invoke(api);
         if (tier instanceof ToolMaterial toolMaterial) {
            return new TierSS(toolMaterial, fallback.getRepairTag());
         }
         return fallback;
      } catch (ReflectiveOperationException | RuntimeException e) {
         Log.warn("Botania: failed to read tier '" + getterName + "': " + e);
         return fallback;
      }
   }

   /** Botania's {@code requestManaExactForTool}; returns false when Botania is unavailable. */
   public static boolean requestManaForTool(ItemStack stack, Player player, int mana) {
      Method instance = MANA_INSTANCE.get();
      Method request = REQUEST_MANA.get();
      if (instance == null || request == null) {
         return false;
      }
      try {
         Object handler = instance.invoke(null);
         return Boolean.TRUE.equals(request.invoke(handler, stack, player, mana, true));
      } catch (ReflectiveOperationException | RuntimeException e) {
         Log.warn("Botania: failed to request mana: " + e);
         return false;
      }
   }

   public static net.minecraft.core.Holder<Attribute> pixieSpawnChanceHolder() {
      ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(Constants.BOTANIA_MODID, "pixie_spawn_chance");
      return net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.get(loc).map(h -> (net.minecraft.core.Holder<Attribute>) h).orElse(null);
   }

   public static Attribute pixieSpawnChanceAttribute() {
      Field field = PIXIE_CHANCE.get();
      if (field == null) {
         return null;
      }
      try {
         Object value = field.get(null);
         return value instanceof Attribute attribute ? attribute : null;
      } catch (ReflectiveOperationException | RuntimeException e) {
         return null;
      }
   }

   public static AttributeModifier pixieModifier(EquipmentSlot slot, String name, double amount) {
      Method makeModifier = PIXIE_MODIFIER.get();
      if (makeModifier == null) {
         return null;
      }
      try {
         Object modifier = makeModifier.invoke(null, slot, name, amount);
         return modifier instanceof AttributeModifier attributeModifier ? attributeModifier : null;
      } catch (ReflectiveOperationException | RuntimeException e) {
         return null;
      }
   }

   public static boolean damageItemIfPossible(ItemStack stack, int amount, LivingEntity entity, int manaPerDamage) {
      return requestManaForTool(stack, entity instanceof Player player ? player : null, amount * manaPerDamage);
   }

   // ------------------------------------------------------------------ reflection plumbing

   private static Supplier<Method> method(String className, String methodName, Class<?>... args) {
      return new Cached<>(() -> {
         try {
            Class<?> clazz = classForName(className);
            if (clazz == null) {
               return null;
            }
            try {
               return clazz.getMethod(methodName, args);
            } catch (NoSuchMethodException e) {
               // Botania has renamed/relocated some of these over the years; accept any
               // overload with the same arity as a best effort before giving up.
               for (Method candidate : clazz.getMethods()) {
                  if (candidate.getName().equals(methodName) && candidate.getParameterCount() == args.length) {
                     return candidate;
                  }
               }
               return null;
            }
         } catch (LinkageError | RuntimeException e) {
            return null;
         }
      });
   }

   private static Supplier<Field> field(String className, String fieldName) {
      return new Cached<>(() -> {
         try {
            Class<?> clazz = classForName(className);
            return clazz == null ? null : clazz.getField(fieldName);
         } catch (NoSuchFieldException | LinkageError | RuntimeException e) {
            return null;
         }
      });
   }

   private static Class<?> classForName(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException | LinkageError e) {
         return null;
      }
   }

   /** Lazily resolved once; a {@code null} result is remembered instead of retried. */
   private static final class Cached<T> implements Supplier<T> {
      private final Supplier<T> delegate;
      private boolean resolved;
      private T value;

      private Cached(Supplier<T> delegate) {
         this.delegate = delegate;
      }

      @Override
      public T get() {
         if (!this.resolved) {
            this.value = this.delegate.get();
            this.resolved = true;
         }
         return this.value;
      }
   }
}
