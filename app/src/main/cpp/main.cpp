#include <jni.h>
#include <string>
#include <cstring>
//
// Created by oocha on 2021/05/12.
//

extern "C"
JNIEXPORT jstring JNICALL
Java_com_websarva_wings_android_todoapps_MailAndPassRegistClass_getAESData(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jint flag) {
    // TODO: implement getAESData()
    std::string ret_data;

    switch (flag) {
        case 0:
            ret_data= "hcgdwpCP3n+X1BwkeknPjYoE6FYbtAHrGdxs8d+892o=";
            break;
        case 1:
            ret_data = "d0RySDgyRGNpSnRGQUI0RVRrVEtLenZ2N3JyUTR0RGM=";
            break;
        case 2:
            ret_data = "9qnbq9d/p9a0/BDXXPpMUA==";
            break;
        default:
            ret_data = "Error";
            break;
    }
    return env->NewStringUTF(ret_data.c_str());
}