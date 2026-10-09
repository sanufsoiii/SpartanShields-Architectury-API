package com.oblivioussp.spartanshields.network;

import dev.architectury.networking.NetworkManager;

public class NetworkHandler {
   public static void init() {
      NetworkManager.registerReceiver(NetworkManager.c2s(), ShieldBashPacket.TYPE, ShieldBashPacket.STREAM_CODEC, ShieldBashPacket::handle);
   }
}
