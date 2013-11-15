/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Per article 5 of the Apache 2.0 License, some modifications to this code
 * were made by the OmniROM Project.
 *
 * Modifications Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

#pragma once

#include <audio_effects/effect_stereowide.h>

#include "Biquad.h"
#include "Effect.h"
#include "FIR16.h"

/**
 * Effect that enhances stereo wideness
 * Based off OpenSLES Stereo Widener and ideas from Waves S1 Stereo Imager DSP plugin
 * See the CPP file for a detailed explanation of the implementation
 */

class EffectStereoWide : public Effect {
    private:
    int16_t mStrength;

    int64_t mDelayData;
    Biquad mHighPass;
    Biquad mBassTrim;

    // Matrix M (center channel) coefficient
    double mMatrixMCoeff;
    // Matrix S (side channel) coefficient
    double mMatrixSCoeff;
    // Split EQ HighPass on S coefficient
    double mSplitEQCoeff;
    // Split EQ HighPass compensation on M coefficient
    double mSplitEQCompCoeff;
    // Bass trim coefficient
    double mBassTrimCoeff;

    void refreshStrength();

    public:
    EffectStereoWide();

    int32_t command(uint32_t cmdCode, uint32_t cmdSize, void* pCmdData, uint32_t* replySize, void* pReplyData);
    int32_t process(audio_buffer_t *in, audio_buffer_t *out);
};
