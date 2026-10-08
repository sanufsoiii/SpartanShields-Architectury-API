package com.oblivioussp.spartanshields.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
   private static final Logger LOG = LoggerFactory.getLogger("Spartan Shields");

   public static void info(Object message) {
      LOG.info(String.valueOf(message));
   }

   public static void warn(Object message) {
      LOG.warn(String.valueOf(message));
   }

   public static void error(Object message) {
      LOG.error(String.valueOf(message));
   }

   public static void fatal(Object message) {
      LOG.error(String.valueOf(message));
   }

   public static void debug(Object message) {
      LOG.debug(String.valueOf(message));
   }
}
