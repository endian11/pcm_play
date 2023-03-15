package com.opensource.openslesdemo;

public class AudioRecorder {
   static {
      System.loadLibrary("openslesdemo");
   }

   public native void startRecord();

   public native void stopRecord();

   public native void release();
}
