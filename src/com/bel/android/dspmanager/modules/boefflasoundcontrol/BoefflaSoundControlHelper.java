/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.bel.android.dspmanager.modules.boefflasoundcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bel.android.dspmanager.activity.DSPManager;
import com.bel.android.dspmanager.activity.Utils;

import java.util.ArrayList;

/**
 * Helper class to control Boeffla Sound Control
 */
public class BoefflaSoundControlHelper {

    //=========================
    // Fields
    //=========================
    private static final String TAG = "BoefflaSoundControlHelper";
    private static BoefflaSoundControlHelper boefflaSoundControlHelper;
    //=========================
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mSharedPrefsEditor;
    //=========================
    // Paths
    //=========================
    private static String BOEFFLA_SOUND_ROOT = "/sys/devices/virtual/misc/boeffla_sound";
    private static String BOEFFLA_SOUND = "/sys/devices/virtual/misc/boeffla_sound/boeffla_sound";
    private static String DAC_DIRECT = "/sys/devices/virtual/misc/boeffla_sound/dac_direct";
    private static String DAC_OVERSAMPLING = "/sys/devices/virtual/misc/boeffla_sound/dac_oversampling";
    private static String FLL_TUNING = "/sys/devices/virtual/misc/boeffla_sound/fll_tuning";
    private static String HEADPHONE_VOLUME = "/sys/devices/virtual/misc/boeffla_sound/headphone_volume";
    private static String MIC_CALL = "/sys/devices/virtual/misc/boeffla_sound/mic_level_call";
    private static String MIC_GENERAL = "/sys/devices/virtual/misc/boeffla_sound/mic_level_general";
    private static String MONO_DOWNMIX = "/sys/devices/virtual/misc/boeffla_sound/mono_downmix";
    private static String OVER_SATURATION_SUPPRESS = "/sys/devices/virtual/misc/boeffla_sound/eq";
    private static String PRIVACY_MODE = "/sys/devices/virtual/misc/boeffla_sound/privacy_mode";
    private static String SPEAKER_TUNING = "/sys/devices/virtual/misc/boeffla_sound/speaker_tuning";
    private static String SPEAKER_VOLUME = "/sys/devices/virtual/misc/boeffla_sound/speaker_volume";
    private static String STEREO_EXPANSION = "/sys/devices/virtual/misc/boeffla_sound/stereo_expansion";

    private BoefflaSoundControlHelper(Context paramContext) {
        mSharedPrefs = paramContext.getSharedPreferences(
                DSPManager.SHARED_PREFERENCES_BASENAME + ".boefflasoundcontrol",
                Context.MODE_MULTI_PROCESS);
        mSharedPrefsEditor = mSharedPrefs.edit();
    }

    /**
     * Get an instance of the BoefflaSoundControlHelper
     *
     * @param paramContext The context of the current Activity
     * @return An instance of the Boeffla Control Helper
     */
    public static BoefflaSoundControlHelper getBoefflaSoundControlHelper(Context paramContext) {
        if (boefflaSoundControlHelper == null) {
            boefflaSoundControlHelper = new BoefflaSoundControlHelper(paramContext);
        }
        return boefflaSoundControlHelper;
    }

    //=========================
    // Save and Load
    //=========================

    /**
     * Applies saved values to kernel
     */
    public void applyValues() {
        if (isSupported()) {

            if (getBoefflaSound()) {
                boolean b = mSharedPrefs.getBoolean("boeffla_sound", false);
                applyBoefflaSound(b);
            }

            if (getDACDirect()) {
                boolean b = mSharedPrefs.getBoolean("dac_direct", false);
                applyDACDirect(b);
            }

            if (getDACOversampling()) {
                boolean b = mSharedPrefs.getBoolean("dac_oversampling", false);
                applyDACOversampling(b);
            }

            if (getFLLTuning()) {
                boolean b = mSharedPrefs.getBoolean("fll_tuning", false);
                applyFLLTuning(b);
            }

            if (getHeadphoneVolumeLeft()) {
                int i = mSharedPrefs.getInt("headphone_volume_left", 57);
                applyHeadphoneVolumeLeft(i);
            }

            if (getHeadphoneVolumeRight()) {
                int i = mSharedPrefs.getInt("headphone_volume_right", 57);
                applyHeadphoneVolumeRight(i);
            }

            if (getMicrophoneCall()) {
                int i = mSharedPrefs.getInt("microphone_call", 25);
                applyMicrophoneCall(i);
            }

            if (getMicrophoneGeneral()) {
                int i = mSharedPrefs.getInt("microphone_general", 28);
                applyMicrophoneGeneral(i);
            }

            if (getMonoDownmix()) {
                boolean b = mSharedPrefs.getBoolean("mono_downmix", false);
                applyMonoDownmix(b);
            }

            if (getOverSaturationSuppress()) {
                boolean b = mSharedPrefs.getBoolean("over_saturation_suppress", false);
                applyOverSaturationSuppress(b);
            }

            if (getPrivacyMode()) {
                boolean b = mSharedPrefs.getBoolean("privacy_mode", false);
                applyPrivacyMode(b);
            }

            if (getSpeakerTuning()) {
                boolean b = mSharedPrefs.getBoolean("speaker_tuning", false);
                applySpeakerTuning(b);
            }

            if (getSpeakerVolume()) {
                int i = mSharedPrefs.getInt("speaker_volume", 57);
                applySpeakerVolume(i);
            }

            if (getStereoExpansion()) {
                int i = mSharedPrefs.getInt("stereo_expansion", 0);
                applyStereoExpansion(i);
            }
        }
    }

    //=========================
    // Apply
    //=========================

    /**
     * Applies Boeffla Sound
     */
    public void applyBoefflaSound(boolean b) {
        Utils.writeValue(BOEFFLA_SOUND, b);
    }

    /**
     * Applies DAC Direct
     */
    public void applyDACDirect(boolean b) {
        Utils.writeValue(DAC_DIRECT, b);
    }

    /**
     * Applies DAC Oversampling
     */
    public void applyDACOversampling(boolean b) {
        Utils.writeValue(DAC_OVERSAMPLING, b);
    }

    /**
     * Applies FLL Tuning
     */
    public void applyFLLTuning(boolean b) {
        Utils.writeValue(FLL_TUNING, b);
    }

    /**
     * Applies Headphone volume (left)
     */
    public void applyHeadphoneVolumeLeft(int i) {
        String s = i + " " + readHeadphoneVolumeRight();
        Utils.writeValue(HEADPHONE_VOLUME, s);
    }

    /**
     * Applies Headphone volume (right)
     */
    public void applyHeadphoneVolumeRight(int i) {
        String s = readHeadphoneVolumeLeft() + " " + i;
        Utils.writeValue(HEADPHONE_VOLUME, s);
    }

    /**
     * Applies Microphone volume (call)
     */
    public void applyMicrophoneCall(int i) {
        Utils.writeValue(MIC_CALL, Integer.toString(i));
    }

    /**
     * Applies Microphone volume (general)
     */
    public void applyMicrophoneGeneral(int i) {
        Utils.writeValue(MIC_GENERAL, Integer.toString(i));
    }

    /**
     * Applies Mono Downmix
     */
    public void applyMonoDownmix(boolean b) {
        Utils.writeValue(MONO_DOWNMIX, b);
    }

    /**
     * Applies Over Saturation Suppress
     */
    public void applyOverSaturationSuppress(boolean b) {
        String s = "0";
        if (b) {
            s = "2";
        }
        Utils.writeValue(OVER_SATURATION_SUPPRESS, s);
    }

    /**
     * Applies Privacy Mode
     */
    public void applyPrivacyMode(boolean b) {
        Utils.writeValue(PRIVACY_MODE, b);
    }

    /**
     * Applies Speaker Tuning
     */
    public void applySpeakerTuning(boolean b) {
        Utils.writeValue(SPEAKER_TUNING, b);
    }

    /**
     * Applies Speaker Volume
     */
    public void applySpeakerVolume(int i) {
        Utils.writeValue(SPEAKER_VOLUME, Integer.toString(i));
    }

    /**
     * Applies Stereo Expansion
     */
    public void applyStereoExpansion(int i) {
        Utils.writeValue(STEREO_EXPANSION, Integer.toString(i));
    }

    //=========================
    // Read
    //=========================

    /**
     * Returns the value of boeffla_sound
     */
    public int readBoefflaSound() {
        String s = Utils.readOneLine(BOEFFLA_SOUND).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of dac_direct
     */
    public int readDACDirect() {
        String s = Utils.readOneLine(DAC_DIRECT).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of dac_oversampling
     */
    public int readDACOversampling() {
        String s = Utils.readOneLine(DAC_OVERSAMPLING).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of fll_tuning
     */
    public int readFLLTuning() {
        String s = Utils.readOneLine(FLL_TUNING).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of headphone_volume (left)
     */
    public int readHeadphoneVolumeLeft() {
        String s = Utils.getSystemFileString(HEADPHONE_VOLUME).split("Headphone volume:Left: ")[1].split("Right:")[0];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of headphone_volume (right)
     */
    public int readHeadphoneVolumeRight() {
        String s = Utils.getSystemFileString(HEADPHONE_VOLUME).split("Headphone volume:Left: ")[1].split("Right: ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of mic_level_call
     */
    public int readMicrophoneCall() {
        String s = Utils.readOneLine(MIC_CALL).split("ll ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of mic_level_general
     */
    public int readMicrophoneGeneral() {
        String s = Utils.readOneLine(MIC_GENERAL).split("al ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of mono_downmix
     */
    public int readMonoDownmix() {
        String s = Utils.readOneLine(MONO_DOWNMIX).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of eq
     */
    public int readOverSaturationSuppress() {
        String s = Utils.readOneLine(OVER_SATURATION_SUPPRESS).split(": ")[1];
        int i = Integer.parseInt(s);
        if (i == 2) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns the value of privacy_mode
     */
    public int readPrivacyMode() {
        String s = Utils.readOneLine(PRIVACY_MODE).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of speaker_tuning
     */
    public int readSpeakerTuning() {
        String s = Utils.readOneLine(SPEAKER_TUNING).split(": ")[1];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of speaker_volume
     */
    public int readSpeakerVolume() {
        String s = Utils.getSystemFileString(SPEAKER_VOLUME).split("Speaker volume:Left: ")[1].split("Right:")[0];
        return Integer.parseInt(s);
    }

    /**
     * Returns the value of stereo_expansion
     */
    public int readStereoExpansion() {
        String s = Utils.readOneLine(STEREO_EXPANSION).split(": ")[1];
        return Integer.parseInt(s);
    }


    //=========================
    // Get
    //=========================

    public boolean isSupported() {
        return Utils.fileExists(BOEFFLA_SOUND_ROOT);
    }

    /**
     * @return null if not existing
     */

    public boolean getBoefflaSound() {
        return Utils.readOneLine(BOEFFLA_SOUND) != null;
    }

    public boolean getDACDirect() {
        return Utils.readOneLine(DAC_DIRECT) != null;
    }

    public boolean getDACOversampling() {
        return Utils.readOneLine(DAC_OVERSAMPLING) != null;
    }

    public boolean getFLLTuning() {
        return Utils.readOneLine(FLL_TUNING) != null;
    }

    public boolean getHeadphoneVolumeLeft() {
        return Utils.readOneLine(HEADPHONE_VOLUME) != null;
    }

    public boolean getHeadphoneVolumeRight() {
        return Utils.readOneLine(HEADPHONE_VOLUME) != null;
    }

    public boolean getMicrophoneCall() {
        return Utils.readOneLine(MIC_CALL) != null;
    }

    public boolean getMicrophoneGeneral() {
        return Utils.readOneLine(MIC_GENERAL) != null;
    }

    public boolean getMonoDownmix() {
        return Utils.readOneLine(MONO_DOWNMIX) != null;
    }

    public boolean getOverSaturationSuppress() {
        return Utils.readOneLine(OVER_SATURATION_SUPPRESS) != null;
    }

    public boolean getPrivacyMode() {
        return Utils.readOneLine(PRIVACY_MODE) != null;
    }

    public boolean getSpeakerTuning() {
        return Utils.readOneLine(SPEAKER_TUNING) != null;
    }

    public boolean getSpeakerVolume() {
        return Utils.readOneLine(SPEAKER_VOLUME) != null;
    }

    public boolean getStereoExpansion() {
        return Utils.readOneLine(STEREO_EXPANSION) != null;
    }
}
