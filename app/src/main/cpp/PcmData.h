//
// Created by Jesson on 2022/5/26.
//

#ifndef OPENSLESDEMO_PCMDATA_H
#define OPENSLESDEMO_PCMDATA_H

/***
 * PCM数据封装
 *
 */
class PcmData {
public:
    char *data;
    int size;

public:
    PcmData(char *data, int size);

    ~PcmData();

    int getSize();

    char *getData();
};



#endif //OPENSLESDEMO_PCMDATA_H
