package com.oblivioussp.spartanshields.network;

import com.oblivioussp.spartanshields.SpartanShields;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.SimpleNetworkManager;

/**
 * Porting note: the Forge release used {@code NetworkRegistry.newSimpleChannel} with a hand-rolled
 * packet id counter and sent packets through {@code player.connection.connection}. Architectury's
 * {@link SimpleNetworkManager} replaces all of that: packet types are registered by name on a
 * channel identified by the mod id, and the platform handles the plumbing (including the
 * handshake) on both loaders.
 */
public class NetworkHandler {
   public static final SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(SpartanShields.ID);

   public static void init() {
      // Touching ShieldBashPacket.TYPE registers the packet on the manager above.
      ShieldBashPacket.TYPE.getId();
   }
}
