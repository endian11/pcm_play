//
// Created by Jesson on 2022/5/26.
//

#include "malloc.h"
#include "string.h"
#include "PcmData.h"

PcmData::PcmData(char *data, int size) {
    this->data = static_cast<char *>(malloc(size));
    memcpy(this->data,data,size);
    this->size = size;
}

PcmData::~PcmData() {

}

char* PcmData::getData() {
    return data;
}

int PcmData::getSize() {
    return size;
}

