package com.opensource.openslesdemo;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioTrackPlayer {
   private AudioTrack audioTrack;

   public AudioTrackPlayer() {
   }

   public void initAudioTrack() {
      int minBufferSize = AudioTrack.getMinBufferSize(44100,
              AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
      audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
              32000,//此处是采样率，可能需要修改
              AudioFormat.CHANNEL_OUT_STEREO,
              AudioFormat.ENCODING_PCM_16BIT,
              minBufferSize,
              AudioTrack.MODE_STREAM);
      audioTrack.play();
   }

   public void write(byte[] audioData, int offsetInBytes, int sizeInBytes) {
      audioTrack.write(audioData, offsetInBytes, sizeInBytes);
   }

   public void release() {
      try {
         if (audioTrack != null) {
            audioTrack.pause();
            audioTrack.flush();
            audioTrack.stop();
            audioTrack.release();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
