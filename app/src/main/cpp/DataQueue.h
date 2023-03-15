//
// Created by Jesson on 2022/5/26.
//

#ifndef OPENSLESDEMO_DATAQUEUE_H
#define OPENSLESDEMO_DATAQUEUE_H

#include "queue"
#include "PcmData.h"
#include "pthread.h"

class DataQueue {
public:
    std::queue<PcmData *> queuePacket;
    pthread_mutex_t mutexPacket;
    pthread_cond_t condPacket;
public:
    DataQueue();

    ~DataQueue();

    int putPcmData(PcmData *data);

    PcmData *getPcmData();

    int getPcmDataSize();

    int clearPcmData();

    void release();
};



#endif //OPENSLESDEMO_DATAQUEUE_H
