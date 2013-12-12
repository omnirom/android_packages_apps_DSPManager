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
package com.bel.android.dspmanager.modules.soundcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bel.android.dspmanager.activity.DSPManager;
import com.bel.android.dspmanager.activity.Utils;

import java.util.ArrayList;

/**
 * Helper class to control Faux123's SoundControl Kernel Modules
 */
public class SoundControlHelper {

    //=========================
    // Fields
    //=========================
    private static final String TAG = "SoundControlHelper";
    private static SoundControlHelper soundControlHelper;
    //=========================
    private static final ArrayList<String> mPresets = new ArrayList<String>();
    private static Context mContext;
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mSharedPrefsEditor;
    //=========================
    // Presets
    //=========================
    private static final String[] mPresetsList =
            {"Custom", "Quality", "Loudness", "Quiet", "Stock"};
    private static final int[] mPresetQuality = {-7, -7, 0, 0, -7};
    private static final int[] mPresetLoudness = {5, 5, 0, 0, 0};
    private static final int[] mPresetQuiet = {-20, -20, 0, 0, -20};
    private static final int[] mPresetStock = {0, 0, 0, 0, 0};
    //=========================
    // Paths
    //=========================
    private static String HEADPHONE_GAIN = "/sys/kernel/sound_control/gpl_headphone_gain";
    private static String HEADPHONE_PA_GAIN = "/sys/kernel/sound_control/gpl_headphone_pa_gain";
    private static String MIC_GAIN = "/sys/kernel/sound_control/gpl_mic_gain";
    private static String CAM_GAIN = "/sys/kernel/sound_control/gpl_cam_mic_gain";
    private static String SPEAKER_GAIN = "/sys/kernel/sound_control/gpl_speaker_gain";
    private static String VERSION = "/sys/kernel/sound_control/gpl_sound_control_version";
    private static String LOCKED = "/sys/kernel/sound_control_3/gpl_sound_control_locked";
    //=========================
    // Version Paths
    //=========================
    private static final String SC_GPL = "/sys/kernel/sound_control/gpl_sound_control_version";
    private static final String SC_GPL_3 = "/sys/kernel/sound_control_3/gpl_sound_control_version";
    private static final String SC_3X = "/sys/kernel/mm/tabla_mm/scv";

    private SoundControlHelper(Context paramContext) {
        mContext = paramContext;
        switch (getModuleVersion()) {
            default:
            case 0:
                break;
            case 1: // GPL
                HEADPHONE_GAIN = "/sys/kernel/sound_control/gpl_headphone_gain";
                HEADPHONE_PA_GAIN = "/sys/kernel/sound_control/gpl_headphone_pa_gain";
                MIC_GAIN = "/sys/kernel/sound_control/gpl_mic_gain";
                CAM_GAIN = "/sys/kernel/sound_control/gpl_cam_mic_gain";
                SPEAKER_GAIN = "/sys/kernel/sound_control/gpl_speaker_gain";
                VERSION = "/sys/kernel/sound_control/gpl_sound_control_version";
                LOCKED = "";
                break;
            case 2: // GPL 3
                HEADPHONE_GAIN = "/sys/kernel/sound_control_3/gpl_headphone_gain";
                HEADPHONE_PA_GAIN = "/sys/kernel/sound_control_3/gpl_headphone_pa_gain";
                MIC_GAIN = "/sys/kernel/sound_control_3/gpl_mic_gain";
                CAM_GAIN = "/sys/kernel/sound_control_3/gpl_cam_mic_gain";
                SPEAKER_GAIN = "/sys/kernel/sound_control_3/gpl_speaker_gain";
                VERSION = "/sys/kernel/sound_control_3/gpl_sound_control_version";
                LOCKED = "/sys/kernel/sound_control_3/gpl_sound_control_locked";
                break;
            case 3: // 3
                HEADPHONE_GAIN = "/sys/kernel/mm/tabla_mm/hpg";
                HEADPHONE_PA_GAIN = "/sys/kernel/mm/tabla_mm/hppg";
                MIC_GAIN = "/sys/kernel/mm/tabla_mm/mg";
                CAM_GAIN = "/sys/kernel/mm/tabla_mm/cmg";
                SPEAKER_GAIN = "/sys/kernel/mm/tabla_mm/sg";
                VERSION = "/sys/kernel/mm/tabla_mm/scv";
                LOCKED = "";
                break;
        }

        mSharedPrefs = paramContext.getSharedPreferences(
                DSPManager.SHARED_PREFERENCES_BASENAME + ".soundcontrol",
                Context.MODE_MULTI_PROCESS);

        mSharedPrefsEditor = mSharedPrefs.edit();
        if (SoundControlHelper.mPresets.size() == 0) {
            loadPresets();
        }
    }

    /**
     * Get an instance of the SoundControlHelper
     *
     * @param paramContext The context of the current Activity
     * @return An instance of the Sound Control Helper
     */
    public static SoundControlHelper getSoundControlHelper(Context paramContext) {
        if (soundControlHelper == null) {
            soundControlHelper = new SoundControlHelper(paramContext);
        }
        return soundControlHelper;
    }

    /**
     * Loads all presets
     */
    private void loadPresets() {
        if (mPresets.size() == 0) {
            mPresets.add(mPresetsList[0]);
            mPresets.add(mPresetsList[1]);
            mPresets.add(mPresetsList[2]);
            mPresets.add(mPresetsList[3]);
            mPresets.add(mPresetsList[4]);
        }
    }

    //=========================
    // Save and Load
    //=========================

    /**
     * Loads the custom preset and applies values
     */
    public void applyValues() {
        if (Utils.fileExists(VERSION)) {

            switchPreset(mContext.getSharedPreferences(
                    DSPManager.SHARED_PREFERENCES_BASENAME + ".soundcontrol",
                    Context.MODE_MULTI_PROCESS)
                    .getString("preset_profile", mPresetsList[0]));

            if (getHeadphone()) {
                applyHeadphoneLeft(mSharedPrefs.getString("headphone_l",
                        Integer.toString(readHeadphoneLeft() - 40)));

                applyHeadphoneRight(mSharedPrefs.getString("headphone_r",
                        Integer.toString(readHeadphoneRight() - 40)));
            }

            if (getMicrophoneHandset()) {
                applyMicrophoneHandset(mSharedPrefs.getString("handset_mic",
                        Integer.toString(readMicrophoneHandset() - 40)));
            }

            if (getMicrophoneCam()) {
                applyMicrophoneCamcorder(mSharedPrefs.getString("camcorder_mic",
                        Integer.toString(readMicrophoneCamcorder() - 40)));
            }

            if (getSpeaker()) {
                applySpeaker(mSharedPrefs.getString("speaker",
                        Integer.toString(readSpeaker() - 40)));
            }

            if (getHeadphonePa()) {
                applyHeadphonePowerampLeft(
                        mSharedPrefs.getString("headphone_pa_l",
                                Integer.toString(readHeadphonePowerampLeft() - 40)));
                applyHeadphonePowerampRight(
                        mSharedPrefs.getString("headphone_pa_r",
                                Integer.toString(readHeadphonePowerampRight() - 40)));
            }
        }
    }

    /**
     * Loads a preset and applies its values
     *
     * @param presetId The id of the preset
     */
    public void switchPreset(int presetId) {
        switchPreset(mPresets.get(presetId));
    }

    /**
     * Loads a preset and applies its values
     *
     * @param paramString The name of the preset
     */
    public void switchPreset(String paramString) {

        mSharedPrefsEditor.putString("preset_profile", paramString).apply();

        if (mPresets.size() == 0) {
            loadPresets();
        }
        switch (mPresets.indexOf(paramString)) {
            default:
            case 0:
                return;
            case 1:
                applyHeadphoneLeft(Integer.toString(mPresetQuality[0]));
                applyHeadphoneRight(Integer.toString(mPresetQuality[1]));
                applyMicrophoneHandset(Integer.toString(mPresetQuality[2]));
                applyMicrophoneCamcorder(Integer.toString(mPresetQuality[3]));
                applySpeaker(Integer.toString(mPresetQuality[4]));
                return;
            case 2:
                applyHeadphoneLeft(Integer.toString(mPresetLoudness[0]));
                applyHeadphoneRight(Integer.toString(mPresetLoudness[1]));
                applyMicrophoneHandset(Integer.toString(mPresetLoudness[2]));
                applyMicrophoneCamcorder(Integer.toString(mPresetLoudness[3]));
                applySpeaker(Integer.toString(mPresetLoudness[4]));
                return;
            case 3:
                applyHeadphoneLeft(Integer.toString(mPresetQuiet[0]));
                applyHeadphoneRight(Integer.toString(mPresetQuiet[1]));
                applyMicrophoneHandset(Integer.toString(mPresetQuiet[2]));
                applyMicrophoneCamcorder(Integer.toString(mPresetQuiet[3]));
                applySpeaker(Integer.toString(mPresetQuiet[4]));
                return;
            case 4:
                applyHeadphoneLeft(Integer.toString(mPresetStock[0]));
                applyHeadphoneRight(Integer.toString(mPresetStock[1]));
                applyMicrophoneHandset(Integer.toString(mPresetStock[2]));
                applyMicrophoneCamcorder(Integer.toString(mPresetStock[3]));
                applySpeaker(Integer.toString(mPresetStock[4]));
        }
    }

    //=========================
    // Apply
    //=========================

    /**
     * Applies <b>left headphone</b> gain
     *
     * @param paramString The value of the left headphone gain
     */
    public void applyHeadphoneLeft(String paramString) {
        int i1 = readHeadphoneRight();
        int i2 = Integer.parseInt(paramString);
        Utils.writeValue(HEADPHONE_GAIN,
                Integer.toString(i2 + 40) + " " + Integer.toString(i1));

        mSharedPrefsEditor.putString("headphone_l", Integer.toString(i2)).apply();
    }

    /**
     * Applies <b>right headphone</b> gain
     *
     * @param paramString The value of the right headphone gain
     */
    public void applyHeadphoneRight(String paramString) {
        int i1 = readHeadphoneLeft();
        int i2 = Integer.parseInt(paramString);
        Utils.writeValue(HEADPHONE_GAIN,
                Integer.toString(i1) + " " + Integer.toString(i2 + 40));

        mSharedPrefsEditor.putString("headphone_r", Integer.toString(i2)).apply();
    }

    /**
     * Applies <b>handset microphone</b> gain
     *
     * @param paramString The value of the Handset Microphone gain
     */
    public void applyMicrophoneHandset(String paramString) {
        int i1 = Integer.parseInt(paramString);
        Utils.writeValue(MIC_GAIN, Integer.toString(i1 + 40));

        mSharedPrefsEditor.putString("handset_mic", Integer.toString(i1)).apply();
    }

    /**
     * Applies <b>camcorder microphone</b> gain
     *
     * @param paramString The value of the Camcorder Microphone gain
     */
    public void applyMicrophoneCamcorder(String paramString) {
        int i1 = Integer.parseInt(paramString);
        Utils.writeValue(CAM_GAIN, Integer.toString(i1 + 40));

        mSharedPrefsEditor.putString("camcorder_mic", Integer.toString(i1)).apply();
    }

    /**
     * Applies <b>speaker</b> gain
     *
     * @param paramString The value of the Speaker gain
     */
    public void applySpeaker(String paramString) {
        int i1 = Integer.parseInt(paramString);
        Utils.writeValue(SPEAKER_GAIN, Integer.toString(i1 + 40));

        mSharedPrefsEditor.putString("speaker", Integer.toString(i1)).apply();
    }

    /**
     * Applies <b>left headphone poweramp</b> gain
     *
     * @param paramString The value of the left headphone PowerAmp gain
     */
    public void applyHeadphonePowerampLeft(String paramString) {
        int i1 = readHeadphonePowerampRight();
        int i2 = Integer.parseInt(paramString);
        Utils.writeValue(
                HEADPHONE_PA_GAIN, Integer.toString(i2 + 12) + " " + Integer.toString(i1));

        mSharedPrefsEditor.putString("headphone_pa_l", Integer.toString(i2)).apply();
    }

    /**
     * Applies <b>right headphone poweramp</b> gain
     *
     * @param paramString The value of the right headphone PowerAmp gain
     */
    public void applyHeadphonePowerampRight(String paramString) {
        int i1 = readHeadphonePowerampLeft();
        int i2 = Integer.parseInt(paramString);
        Utils.writeValue(
                HEADPHONE_PA_GAIN, Integer.toString(i1) + " " + Integer.toString(i2 + 12));

        mSharedPrefsEditor.putString("headphone_pa_r", Integer.toString(i2)).apply();
    }

    //=========================
    // Read
    //=========================

    /**
     * @return The value of the <b>left headphone</b> gain
     */
    public int readHeadphoneLeft() {
        String str = Utils.readOneLine(HEADPHONE_GAIN).split(" ")[0];
        try {
            return Integer.parseInt(str);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    /**
     * @return The value of the <b>right headphone</b> gain
     */
    public int readHeadphoneRight() {
        String str = Utils.readOneLine(HEADPHONE_GAIN).split(" ")[1];
        try {
            return Integer.parseInt(str);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    /**
     * @return The value of the <b>handset microphone</b> gain
     */
    public int readMicrophoneHandset() {
        String str = Utils.readOneLine(MIC_GAIN);
        try {
            return Integer.parseInt(str);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    /**
     * @return The value of the <b>camera microphone</b> gain
     */
    public int readMicrophoneCamcorder() {
        String str = Utils.readOneLine(CAM_GAIN);
        try {
            return Integer.parseInt(str);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    /**
     * @return The value of the <b>Speaker</b> gain
     */
    public int readSpeaker() {
        String str = Utils.readOneLine(SPEAKER_GAIN);
        try {
            return Integer.parseInt(str);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    /**
     * @return The value of the <b>left headset poweramp</b> gain
     */
    public int readHeadphonePowerampLeft() {
        String str = Utils.readOneLine(HEADPHONE_PA_GAIN);
        try {
            return Integer.parseInt(str.split(" ")[0]);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    /**
     * @return The value of the <b>right headset poweramp</b> gain
     */
    public int readHeadphonePowerampRight() {
        String str = Utils.readOneLine(HEADPHONE_PA_GAIN);
        try {
            return Integer.parseInt(str.split(" ")[1]);
        } catch (Exception localException) {
            Log.e(TAG, "bad str->int conversion!");
        }
        return 0;
    }

    //=========================
    // Get
    //=========================

    /**
     * @return null if not existing
     */
    public boolean getHeadphone() {
        return Utils.readOneLine(HEADPHONE_GAIN) != null;
    }

    /**
     * @return null if not existing
     */
    public boolean getSpeaker() {
        return Utils.readOneLine(SPEAKER_GAIN) != null;
    }

    /**
     * @return null if not existing
     */
    public boolean getMicrophoneHandset() {
        return Utils.readOneLine(MIC_GAIN) != null;
    }

    /**
     * @return null if not existing
     */
    public boolean getMicrophoneCam() {
        return Utils.readOneLine(CAM_GAIN) != null;
    }

    /**
     * @return null if not existing
     */
    public boolean getHeadphonePa() {
        return Utils.readOneLine(HEADPHONE_PA_GAIN) != null;
    }

    /**
     * @return List of presets
     */
    public String[] getPresetsList() {
        return mPresetsList;
    }

    /**
     * @return Name of the current preset
     */
    public String getCurrentPreset() {
        return mSharedPrefs.getString("preset_profile", mPresetsList[0]);
    }

    /**
     * @return The Version of the Sound Control Module
     */
    public boolean getVersion() {
        return Utils.readOneLine(VERSION) != null;
    }

    /**
     * @return The Version of the Sound Control Module
     */
    public String getVersionFormatted() {
        return getVersion() ?
                Utils.readOneLine(VERSION) :
                "---";
    }

    //=========================
    // Static Methods
    //=========================

    /**
     * @return <b>true</b>, if SoundControl is supported
     */
    public static boolean isSupported() {
        return getModuleVersion() != 0;
    }

    //=========================
    // Methods
    //=========================

    /**
     * 0 = none <br />
     * 1 = GPL < 3 <br />
     * 2 = GPL Version 3+ <br />
     * 3 = Version 3+ <br />
     *
     * @return The Version of sound control or -1 if none
     */
    private static int getModuleVersion() {
        int i = 0;

        if (Utils.fileExists(SC_GPL)) {
            i = 1;
        }
        if (Utils.fileExists(SC_GPL_3)) {
            i = 0; //change to 2 once tested
        }
        if (Utils.fileExists(SC_3X)) {
            i = 0; //change to 3 once tested
        }

        return i;
    }

}
