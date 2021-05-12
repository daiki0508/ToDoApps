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
            ret_data="9GSHtGl/7C/66ozyRKN3WiVFMj0g39zEt5ZN02eyXnw=";
            break;
        case 1:
            ret_data = "wDrH82DciJtFAB4ETkTKKzvv7rrQ4tDc";
            break;
        case 2:
            ret_data = "yEE5kMuq73NODaibgjDTpw==";
            break;
        default:
            ret_data = "Error";
            break;
    }
    return env->NewStringUTF(ret_data.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_websarva_wings_android_todoapps_ToProcessDynamicLinkActivity_getAESData(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint flag) {
    // TODO: implement getAESData()
    std::string ret_data;

    switch (flag) {
        case 0:
            ret_data="9GSHtGl/7C/66ozyRKN3WiVFMj0g39zEt5ZN02eyXnw=";
            break;
        case 1:
            ret_data = "wDrH82DciJtFAB4ETkTKKzvv7rrQ4tDc";
            break;
        case 2:
            ret_data = "yEE5kMuq73NODaibgjDTpw==";
            break;
        default:
            ret_data = "Error";
            break;
    }
    return env->NewStringUTF(ret_data.c_str());
}