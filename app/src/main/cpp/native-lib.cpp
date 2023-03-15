#include <jni.h>
#include <string>

#include "AndroidLog.h"
#include "DataQueue.h"
#include "Audio.h"

#include "AudioRecorder.h"

DataQueue *dataQueue = NULL;
Audio *audio = NULL;


extern "C"
JNIEXPORT void JNICALL
Java_com_opensource_openslesdemo_OpenSlEsPlayer_init(JNIEnv *env, jobject thiz) {
    if (dataQueue == NULL) {
        dataQueue = new DataQueue();
    }
}

/***
 * PCM 数据（char【】）的发送给底层的es进行播放
 *
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_opensource_openslesdemo_OpenSlEsPlayer_sendPcmData(JNIEnv *env, jobject thiz,
                                                            jbyteArray data_, jint size) {
    if (audio == NULL) {
        audio = new Audio(dataQueue, 32000);
        audio->play();
    }
    jbyte *data = env->GetByteArrayElements(data_, nullptr);

    PcmData *pdata = new PcmData((char *) data, size);
    dataQueue->putPcmData(pdata);
    LOGE("size is %d queue size is %d", size, dataQueue->getPcmDataSize());
    env->ReleaseByteArrayElements(data_, data, 0);

}
extern "C"
JNIEXPORT void JNICALL
Java_com_opensource_openslesdemo_OpenSlEsPlayer_release(JNIEnv *env, jobject thiz) {
    if (audio != NULL) {
        delete audio;
        audio = NULL;
    }
    if (dataQueue != NULL) {
        delete dataQueue;
        dataQueue = NULL;
    }
}

AudioRecorder *audioRecorder;
extern "C"
JNIEXPORT void JNICALL
Java_com_opensource_openslesdemo_AudioRecorder_startRecord(JNIEnv *env, jobject thiz) {
    LOGE("初始化AudioRecorder");
    if (audioRecorder == nullptr) {
        audioRecorder = new AudioRecorder();
        audioRecorder->startRecord();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_opensource_openslesdemo_AudioRecorder_stopRecord(JNIEnv *env, jobject thiz) {
    if (audioRecorder != nullptr) {
        audioRecorder->stopRecord();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_opensource_openslesdemo_AudioRecorder_release(JNIEnv *env, jobject thiz) {

    if (audioRecorder != nullptr) {
        audioRecorder->release();
        delete audioRecorder;
        audioRecorder = nullptr;
    }
}
