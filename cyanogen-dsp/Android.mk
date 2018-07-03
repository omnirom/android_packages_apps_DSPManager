LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

ifneq ($(TARGET_SYSTEM_AUDIO_EFFECTS),true)
LOCAL_VENDOR_MODULE := true
endif

LOCAL_MODULE := libcyanogen-dsp

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE_RELATIVE_PATH := soundfx

LOCAL_PRELINK_MODULE := false

LOCAL_ARM_MODE := arm

LOCAL_SRC_FILES := \
	cyanogen-dsp.cpp \
	Biquad.cpp \
	Delay.cpp \
	Effect.cpp \
	EffectBassBoost.cpp \
	EffectCompression.cpp \
	EffectEqualizer.cpp \
	EffectVirtualizer.cpp \
	EffectStereoWide.cpp \
	FIR16.cpp

LOCAL_C_INCLUDES += \
        frameworks/av/include \
        hardware/libhardware/include \
        system/core/include \
        system/core/base/include \
        system/media/audio/include \
        system/media/audio_effects/include \
        frameworks/native/libs/binder/include

LOCAL_SHARED_LIBRARIES := \
	libcutils \
	liblog

include $(BUILD_SHARED_LIBRARY)

ifneq ($(TARGET_USE_DEVICE_AUDIO_EFFECTS_CONF),true)
include $(CLEAR_VARS)

LOCAL_MODULE := audio_effects.conf

LOCAL_SRC_FILES := $(LOCAL_MODULE)

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE_CLASS := ETC

LOCAL_VENDOR_MODULE := true

include $(BUILD_PREBUILT)
endif
