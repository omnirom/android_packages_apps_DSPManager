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
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.bel.android.dspmanager.R;
import com.bel.android.dspmanager.activity.DSPManager;
import com.bel.android.dspmanager.preference.SeekBarPreference;

import java.util.Locale;

/**
 * Controls Boeffla Sound
 */
public class BoefflaSoundControl extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    //=========================
    // Fields
    //=========================
    public static final String NAME = "BoefflaSoundControl";
    private SwitchPreference mBoefflaSound;
    // General
    private CheckBoxPreference mDACDirect;
    private CheckBoxPreference mDACOversampling;
    private CheckBoxPreference mFLLTuning;
    private CheckBoxPreference mMonoDownmix;
    private CheckBoxPreference mOverSaturationSuppress;
    private SeekBarPreference mStereoExpansion;
    // Speaker
    private SeekBarPreference mSpeakerVolume;
    private CheckBoxPreference mSpeakerTuning;
    // Headphone
    private SeekBarPreference mHeadphoneVolumeLeft;
    private SeekBarPreference mHeadphoneVolumeRight;
    private CheckBoxPreference mPrivacyMode;
    // Microphone
    private SeekBarPreference mMicrophoneCall;
    private SeekBarPreference mMicrophoneGeneral;
    //=========================
    // Preference Keys
    //=========================
    private static final String BOEFFLA_SOUND = "boeffla_sound";
    private static final String DAC_DIRECT = "dac_direct";
    private static final String DAC_OVERSAMPLING = "dac_oversampling";
    private static final String FLL_TUNING = "fll_tuning";
    private static final String HEADPHONE_VOLUME_LEFT = "headphone_volume_left";
    private static final String HEADPHONE_VOLUME_RIGHT = "headphone_volume_right";
    private static final String MICROPHONE_CALL = "microphone_call";
    private static final String MICROPHONE_GENERAL = "microphone_general";
    private static final String MONO_DOWNMIX = "mono_downmix";
    private static final String OVER_SATURATION_SUPPRESS = "over_saturation_suppress";
    private static final String PRIVACY_MODE = "privacy_mode";
    private static final String SPEAKER_VOLUME = "speaker_volume";
    private static final String SPEAKER_TUNING = "speaker_tuning";
    private static final String STEREO_EXPANSION = "stereo_expansion";

    //=========================
    // Overridden Methods
    //=========================

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getPreferenceManager().setSharedPreferencesName(
                DSPManager.SHARED_PREFERENCES_BASENAME + ".boefflasoundcontrol");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);

        addPreferencesFromResource(R.xml.boefflasoundcontrol_preferences);


        mBoefflaSound = (SwitchPreference) findPreference(BOEFFLA_SOUND);
        updateBoefflaSound();
        mBoefflaSound.setOnPreferenceChangeListener(this);

        // General
        mDACDirect = (CheckBoxPreference) findPreference(DAC_DIRECT);
        updateDACDirect();
        mDACDirect.setOnPreferenceChangeListener(this);

        mDACOversampling = (CheckBoxPreference) findPreference(DAC_OVERSAMPLING);
        updateDACOversampling();
        mDACOversampling.setOnPreferenceChangeListener(this);

        mFLLTuning = (CheckBoxPreference) findPreference(FLL_TUNING);
        updateFLLTuning();
        mFLLTuning.setOnPreferenceChangeListener(this);

        mMonoDownmix = (CheckBoxPreference) findPreference(MONO_DOWNMIX);
        updateMonoDownmix();
        mMonoDownmix.setOnPreferenceChangeListener(this);

        mOverSaturationSuppress = (CheckBoxPreference) findPreference(OVER_SATURATION_SUPPRESS);
        updateOverSaturationSuppress();
        mOverSaturationSuppress.setOnPreferenceChangeListener(this);

        mStereoExpansion = (SeekBarPreference) findPreference(STEREO_EXPANSION);
        updateStereoExpansion();
        mStereoExpansion.setOnPreferenceChangeListener(this);


        // Speaker
        mSpeakerTuning = (CheckBoxPreference) findPreference(SPEAKER_TUNING);
        updateSpeakerTuning();
        mSpeakerTuning.setOnPreferenceChangeListener(this);

        mSpeakerVolume = (SeekBarPreference) findPreference(SPEAKER_VOLUME);
        updateSpeakerVolume();
        mSpeakerVolume.setOnPreferenceChangeListener(this);


        // Headphone
        mHeadphoneVolumeLeft = (SeekBarPreference) findPreference(HEADPHONE_VOLUME_LEFT);
        updateHeadphoneVolumeLeft();
        mHeadphoneVolumeLeft.setOnPreferenceChangeListener(this);

        mHeadphoneVolumeRight = (SeekBarPreference) findPreference(HEADPHONE_VOLUME_RIGHT);
        updateHeadphoneVolumeRight();
        mHeadphoneVolumeRight.setOnPreferenceChangeListener(this);

        mPrivacyMode = (CheckBoxPreference) findPreference(PRIVACY_MODE);
        updatePrivacyMode();
        mPrivacyMode.setOnPreferenceChangeListener(this);


        // Microphone
        mMicrophoneCall = (SeekBarPreference) findPreference(MICROPHONE_CALL);
        updateMicrophoneCall();
        mMicrophoneCall.setOnPreferenceChangeListener(this);

        mMicrophoneGeneral = (SeekBarPreference) findPreference(MICROPHONE_GENERAL);
        updateMicrophoneGeneral();
        mMicrophoneGeneral.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mBoefflaSound) {
            boolean b = (Boolean) newValue;
            mBoefflaSound.setChecked(b);
            bHelper().applyBoefflaSound(b);
            if (!b) {
                resetAll();
            }
            return true;
        } else if (preference == mDACOversampling) {
            boolean b = (Boolean) newValue;
            mDACOversampling.setChecked(b);
            bHelper().applyDACOversampling(b);
            return true;
        } else if (preference == mDACDirect) {
            boolean b = (Boolean) newValue;
            mDACDirect.setChecked(b);
            bHelper().applyDACDirect(b);
            return true;
        } else if (preference == mFLLTuning) {
            boolean b = (Boolean) newValue;
            mFLLTuning.setChecked(b);
            bHelper().applyFLLTuning(b);
            return true;
        } else if (preference == mHeadphoneVolumeLeft) {
            int i = (Integer) newValue;
            mHeadphoneVolumeLeft.setValue(i);
            bHelper().applyHeadphoneVolumeLeft(i);
            return true;
        } else if (preference == mHeadphoneVolumeRight) {
            int i = (Integer) newValue;
            mHeadphoneVolumeRight.setValue(i);
            bHelper().applyHeadphoneVolumeRight(i);
            return true;
        } else if (preference == mMicrophoneCall) {
            int i = (Integer) newValue;
            mMicrophoneCall.setValue(i);
            bHelper().applyMicrophoneCall(i);
            return true;
        } else if (preference == mMicrophoneGeneral) {
            int i = (Integer) newValue;
            mMicrophoneGeneral.setValue(i);
            bHelper().applyMicrophoneGeneral(i);
            return true;
        } else if (preference == mMonoDownmix) {
            boolean b = (Boolean) newValue;
            mMonoDownmix.setChecked(b);
            bHelper().applyMonoDownmix(b);
            return true;
        } else if (preference == mOverSaturationSuppress) {
            boolean b = (Boolean) newValue;
            mOverSaturationSuppress.setChecked(b);
            bHelper().applyOverSaturationSuppress(b);
            return true;
        } else if (preference == mPrivacyMode) {
            boolean b = (Boolean) newValue;
            mPrivacyMode.setChecked(b);
            bHelper().applyPrivacyMode(b);
            return true;
        } else if (preference == mSpeakerTuning) {
            boolean b = (Boolean) newValue;
            mSpeakerTuning.setChecked(b);
            bHelper().applySpeakerTuning(b);
            return true;
        } else if (preference == mSpeakerVolume) {
            int i = (Integer) newValue;
            mSpeakerVolume.setValue(i);
            bHelper().applySpeakerVolume(i);
            return true;
        } else if (preference == mStereoExpansion) {
            int i = (Integer) newValue;
            mStereoExpansion.setValue(i);
            bHelper().applyStereoExpansion(i);
            return true;
        }
        return false;
    }

    //=========================
    // Methods
    //=========================

    private void resetAll() {
            bHelper().applyBoefflaSound(false);
            mBoefflaSound.setChecked(false);

            bHelper().applyDACDirect(false);
            mDACDirect.setChecked(false);

            bHelper().applyDACOversampling(false);
            mDACOversampling.setChecked(false);

            bHelper().applyFLLTuning(false);
            mFLLTuning.setChecked(false);

            bHelper().applyHeadphoneVolumeLeft(57);
            mHeadphoneVolumeLeft.setValue(57);

            bHelper().applyHeadphoneVolumeRight(57);
            mHeadphoneVolumeRight.setValue(57);

            bHelper().applyMicrophoneCall(25);
            mMicrophoneCall.setValue(25);

            bHelper().applyMicrophoneGeneral(28);
            mMicrophoneGeneral.setValue(28);

            bHelper().applyMonoDownmix(false);
            mMonoDownmix.setChecked(false);

            bHelper().applyOverSaturationSuppress(false);
            mOverSaturationSuppress.setChecked(false);

            bHelper().applyPrivacyMode(false);
            mPrivacyMode.setChecked(false);

            bHelper().applySpeakerTuning(false);
            mSpeakerTuning.setChecked(false);

            bHelper().applySpeakerVolume(57);
            mSpeakerVolume.setValue(57);

            bHelper().applyStereoExpansion(0);
            mStereoExpansion.setValue(0);
    }

    /**
     * Update values for Boeffla Sound or disable if not available
     */
    private void updateBoefflaSound() {
        if (bHelper().getBoefflaSound()) {
            int i = bHelper().readBoefflaSound();
            boolean b = (i != 0);
            mBoefflaSound.setChecked(b);
        } else {
            mBoefflaSound.setEnabled(false);
        }
    }

    /**
     * Update values for DAC Direct or disable if not available
     */
    private void updateDACDirect() {
        if (bHelper().getDACDirect()) {
            int i = bHelper().readDACDirect();
            boolean b = (i != 0);
            mDACDirect.setChecked(b);
        } else {
            mDACDirect.setEnabled(false);
        }
    }

    /**
     * Update values for DAC Oversampling or disable if not available
     */
    private void updateDACOversampling() {
        if (bHelper().getDACOversampling()) {
            int i = bHelper().readDACOversampling();
            boolean b = (i != 0);
            mDACOversampling.setChecked(b);
        } else {
            mDACOversampling.setEnabled(false);
        }
    }

    /**
     * Update values for FLL Tuning or disable if not available
     */
    private void updateFLLTuning() {
        if (bHelper().getFLLTuning()) {
            int i = bHelper().readFLLTuning();
            boolean b = (i != 0);
            mFLLTuning.setChecked(b);
        } else {
            mFLLTuning.setEnabled(false);
        }
    }

    /**
     * Update values for Headphone Volume (left) or disable if not available
     */
    private void updateHeadphoneVolumeLeft() {
        if (bHelper().getHeadphoneVolumeLeft()) {
            int i = bHelper().readHeadphoneVolumeLeft();
            mHeadphoneVolumeLeft.setValue(i);
        } else {
            mHeadphoneVolumeLeft.setEnabled(false);
        }
    }

    /**
     * Update values for Headphone Volume (right) or disable if not available
     */
    private void updateHeadphoneVolumeRight() {
        if (bHelper().getHeadphoneVolumeRight()) {
            int i = bHelper().readHeadphoneVolumeRight();
            mHeadphoneVolumeRight.setValue(i);
        } else {
            mHeadphoneVolumeRight.setEnabled(false);
        }
    }

    /**
     * Update values for Microphone Call or disable if not available
     */
    private void updateMicrophoneCall() {
        if (bHelper().getMicrophoneCall()) {
            int i = bHelper().readMicrophoneCall();
            mMicrophoneCall.setValue(i);
        } else {
            mMicrophoneCall.setEnabled(false);
        }
    }

    /**
     * Update values for Microphone General or disable if not available
     */
    private void updateMicrophoneGeneral() {
        if (bHelper().getMicrophoneGeneral()) {
            int i = bHelper().readMicrophoneGeneral();
            mMicrophoneGeneral.setValue(i);
        } else {
            mMicrophoneGeneral.setEnabled(false);
        }
    }

    /**
     * Update values for Mono Downmix or disable if not available
     */
    private void updateMonoDownmix() {
        if (bHelper().getMonoDownmix()) {
            int i = bHelper().readMonoDownmix();
            boolean b = (i != 0);
            mMonoDownmix.setChecked(b);
        } else {
            mMonoDownmix.setEnabled(false);
        }
    }

    /**
     * Update values for Over Saturation Suppress or disable if not available
     */
    private void updateOverSaturationSuppress() {
        if (bHelper().getOverSaturationSuppress()) {
            int i = bHelper().readOverSaturationSuppress();
            boolean b = (i != 0);
            mOverSaturationSuppress.setChecked(b);
        } else {
            mOverSaturationSuppress.setEnabled(false);
        }
    }

    /**
     * Update values for Privacy Mode or disable if not available
     */
    private void updatePrivacyMode() {
        if (bHelper().getPrivacyMode()) {
            int i = bHelper().readPrivacyMode();
            boolean b = (i != 0);
            mPrivacyMode.setChecked(b);
        } else {
            mPrivacyMode.setEnabled(false);
        }
    }

    /**
     * Update values for Speaker Tuning or disable if not available
     */
    private void updateSpeakerTuning() {
        if (bHelper().getSpeakerTuning()) {
            int i = bHelper().readSpeakerTuning();
            boolean b = (i != 0);
            mSpeakerTuning.setChecked(b);
        } else {
            mSpeakerTuning.setEnabled(false);
        }
    }

    /**
     * Update values for Speaker Volume or disable if not available
     */
    private void updateSpeakerVolume() {
        if (bHelper().getSpeakerVolume()) {
            int i = bHelper().readSpeakerVolume();
            mSpeakerVolume.setValue(i);
        } else {
            mSpeakerVolume.setEnabled(false);
        }
    }

    /**
     * Update values for Stereo Expansion or disable if not available
     */
    private void updateStereoExpansion() {
        if (bHelper().getStereoExpansion()) {
            int i = bHelper().readStereoExpansion();
            mStereoExpansion.setValue(i);
        } else {
            mStereoExpansion.setEnabled(false);
        }
    }

    /**
     * Everyone hates typing much, so we created a method for doing the same with less typing.
     *
     * @return An instance of the boeffla Control Helper
     */
    private BoefflaSoundControlHelper bHelper() {
        return BoefflaSoundControlHelper.getBoefflaSoundControlHelper(getActivity());
    }
}
