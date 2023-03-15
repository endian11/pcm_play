package com.opensource.openslesdemo;

public class OpenSlEsPlayer {
   static {
      System.loadLibrary("openslesdemo");
   }

   public native void init();
   public native void sendPcmData(byte[] data, int size);
   public native void release();
}
