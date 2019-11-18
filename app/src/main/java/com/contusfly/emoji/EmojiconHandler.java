/*
 * Copyright 2014 Ankush Sachdeva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.contusfly.emoji;

import android.content.Context;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.SparseIntArray;

import com.polls.polls.R;

/**
 * The Class EmojiconHandler.
 *
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class EmojiconHandler {

    /**
     * Instantiates a new emojicon handler.
     */
    private EmojiconHandler() {
    }

    /**
     * The Constant mEmojiMap.
     */
    private static final SparseIntArray mEmojiMap = new SparseIntArray(846);

    /**
     * The Constant mBankMap.
     */
    private static final SparseIntArray mBankMap = new SparseIntArray(471);

    static {
        // People
        mEmojiMap.put(0x1f604, R.drawable.emoji_1f604);
        mEmojiMap.put(0x1f603, R.drawable.emoji_1f603);
        mEmojiMap.put(0x1f600, R.drawable.emoji_1f600);
        mEmojiMap.put(0x1f60a, R.drawable.emoji_1f60a);
        mEmojiMap.put(0x263a, R.drawable.emoji_263a);
        mEmojiMap.put(0x1f609, R.drawable.emoji_1f609);
        mEmojiMap.put(0x1f60d, R.drawable.emoji_1f60d);
        mEmojiMap.put(0x1f618, R.drawable.emoji_1f618);
        mEmojiMap.put(0x1f61a, R.drawable.emoji_1f61a);
        mEmojiMap.put(0x1f617, R.drawable.emoji_1f617);
        mEmojiMap.put(0x1f619, R.drawable.emoji_1f619);
        mEmojiMap.put(0x1f61c, R.drawable.emoji_1f61c);
        mEmojiMap.put(0x1f61d, R.drawable.emoji_1f61d);
        mEmojiMap.put(0x1f61b, R.drawable.emoji_1f61b);
        mEmojiMap.put(0x1f633, R.drawable.emoji_1f633);
        mEmojiMap.put(0x1f601, R.drawable.emoji_1f601);
        mEmojiMap.put(0x1f614, R.drawable.emoji_1f614);
        mEmojiMap.put(0x1f60c, R.drawable.emoji_1f60c);
        mEmojiMap.put(0x1f612, R.drawable.emoji_1f612);
        mEmojiMap.put(0x1f61e, R.drawable.emoji_1f61e);
        mEmojiMap.put(0x1f623, R.drawable.emoji_1f623);
        mEmojiMap.put(0x1f622, R.drawable.emoji_1f622);
        mEmojiMap.put(0x1f602, R.drawable.emoji_1f602);
        mEmojiMap.put(0x1f62d, R.drawable.emoji_1f62d);
        mEmojiMap.put(0x1f62a, R.drawable.emoji_1f62a);
        mEmojiMap.put(0x1f625, R.drawable.emoji_1f625);
        mEmojiMap.put(0x1f630, R.drawable.emoji_1f630);
        mEmojiMap.put(0x1f605, R.drawable.emoji_1f605);
        mEmojiMap.put(0x1f613, R.drawable.emoji_1f613);
        mEmojiMap.put(0x1f629, R.drawable.emoji_1f629);
        mEmojiMap.put(0x1f62b, R.drawable.emoji_1f62b);
        mEmojiMap.put(0x1f628, R.drawable.emoji_1f628);
        mEmojiMap.put(0x1f631, R.drawable.emoji_1f631);
        mEmojiMap.put(0x1f620, R.drawable.emoji_1f620);
        mEmojiMap.put(0x1f621, R.drawable.emoji_1f621);
        mEmojiMap.put(0x1f624, R.drawable.emoji_1f624);
        mEmojiMap.put(0x1f616, R.drawable.emoji_1f616);
        mEmojiMap.put(0x1f606, R.drawable.emoji_1f606);
        mEmojiMap.put(0x1f60b, R.drawable.emoji_1f60b);
        mEmojiMap.put(0x1f637, R.drawable.emoji_1f637);
        mEmojiMap.put(0x1f60e, R.drawable.emoji_1f60e);
        mEmojiMap.put(0x1f634, R.drawable.emoji_1f634);
        mEmojiMap.put(0x1f635, R.drawable.emoji_1f635);
        mEmojiMap.put(0x1f632, R.drawable.emoji_1f632);
        mEmojiMap.put(0x1f61f, R.drawable.emoji_1f61f);
        mEmojiMap.put(0x1f626, R.drawable.emoji_1f626);
        mEmojiMap.put(0x1f627, R.drawable.emoji_1f627);
        mEmojiMap.put(0x1f608, R.drawable.emoji_1f608);
        mEmojiMap.put(0x1f47f, R.drawable.emoji_1f47f);
        mEmojiMap.put(0x1f62e, R.drawable.emoji_1f62e);
        mEmojiMap.put(0x1f62c, R.drawable.emoji_1f62c);
        mEmojiMap.put(0x1f610, R.drawable.emoji_1f610);
        mEmojiMap.put(0x1f615, R.drawable.emoji_1f615);
        mEmojiMap.put(0x1f62f, R.drawable.emoji_1f62f);
        mEmojiMap.put(0x1f636, R.drawable.emoji_1f636);
        mEmojiMap.put(0x1f607, R.drawable.emoji_1f607);
        mEmojiMap.put(0x1f60f, R.drawable.emoji_1f60f);
        mEmojiMap.put(0x1f611, R.drawable.emoji_1f611);
        mEmojiMap.put(0x1f472, R.drawable.emoji_1f472);
        mEmojiMap.put(0x1f473, R.drawable.emoji_1f473);
        mEmojiMap.put(0x1f46e, R.drawable.emoji_1f46e);
        mEmojiMap.put(0x1f477, R.drawable.emoji_1f477);
        mEmojiMap.put(0x1f482, R.drawable.emoji_1f482);
        mEmojiMap.put(0x1f476, R.drawable.emoji_1f476);
        mEmojiMap.put(0x1f466, R.drawable.emoji_1f466);
        mEmojiMap.put(0x1f467, R.drawable.emoji_1f467);
        mEmojiMap.put(0x1f468, R.drawable.emoji_1f468);
        mEmojiMap.put(0x1f469, R.drawable.emoji_1f469);
        mEmojiMap.put(0x1f474, R.drawable.emoji_1f474);
        mEmojiMap.put(0x1f475, R.drawable.emoji_1f475);
        mEmojiMap.put(0x1f471, R.drawable.emoji_1f471);
        mEmojiMap.put(0x1f47c, R.drawable.emoji_1f47c);
        mEmojiMap.put(0x1f478, R.drawable.emoji_1f478);
        mEmojiMap.put(0x1f63a, R.drawable.emoji_1f63a);
        mEmojiMap.put(0x1f638, R.drawable.emoji_1f638);
        mEmojiMap.put(0x1f63b, R.drawable.emoji_1f63b);
        mEmojiMap.put(0x1f63d, R.drawable.emoji_1f63d);
        mEmojiMap.put(0x1f63c, R.drawable.emoji_1f63c);
        mEmojiMap.put(0x1f640, R.drawable.emoji_1f640);
        mEmojiMap.put(0x1f63f, R.drawable.emoji_1f63f);
        mEmojiMap.put(0x1f639, R.drawable.emoji_1f639);
        mEmojiMap.put(0x1f63e, R.drawable.emoji_1f63e);
        mEmojiMap.put(0x1f479, R.drawable.emoji_1f479);
        mEmojiMap.put(0x1f47a, R.drawable.emoji_1f47a);
        mEmojiMap.put(0x1f648, R.drawable.emoji_1f648);
        mEmojiMap.put(0x1f649, R.drawable.emoji_1f649);
        mEmojiMap.put(0x1f64a, R.drawable.emoji_1f64a);
        mEmojiMap.put(0x1f480, R.drawable.emoji_1f480);
        mEmojiMap.put(0x1f47d, R.drawable.emoji_1f47d);
        mEmojiMap.put(0x1f4a9, R.drawable.emoji_1f4a9);
        mEmojiMap.put(0x1f525, R.drawable.emoji_1f525);
        mEmojiMap.put(0x2728, R.drawable.emoji_2728);
        mEmojiMap.put(0x1f31f, R.drawable.emoji_1f31f);
        mEmojiMap.put(0x1f4ab, R.drawable.emoji_1f4ab);
        mEmojiMap.put(0x1f4a5, R.drawable.emoji_1f4a5);
        mEmojiMap.put(0x1f4a2, R.drawable.emoji_1f4a2);
        mEmojiMap.put(0x1f4a6, R.drawable.emoji_1f4a6);
        mEmojiMap.put(0x1f4a7, R.drawable.emoji_1f4a7);
        mEmojiMap.put(0x1f4a4, R.drawable.emoji_1f4a4);
        mEmojiMap.put(0x1f4a8, R.drawable.emoji_1f4a8);
        mEmojiMap.put(0x1f442, R.drawable.emoji_1f442);
        mEmojiMap.put(0x1f440, R.drawable.emoji_1f440);
        mEmojiMap.put(0x1f443, R.drawable.emoji_1f443);
        mEmojiMap.put(0x1f445, R.drawable.emoji_1f445);
        mEmojiMap.put(0x1f444, R.drawable.emoji_1f444);
        mEmojiMap.put(0x1f44d, R.drawable.emoji_1f44d);
        mEmojiMap.put(0x1f44e, R.drawable.emoji_1f44e);
        mEmojiMap.put(0x1f44c, R.drawable.emoji_1f44c);
        mEmojiMap.put(0x1f44a, R.drawable.emoji_1f44a);
        mEmojiMap.put(0x270a, R.drawable.emoji_270a);
        mEmojiMap.put(0x270c, R.drawable.emoji_270c);
        mEmojiMap.put(0x1f44b, R.drawable.emoji_1f44b);
        mEmojiMap.put(0x270b, R.drawable.emoji_270b);
        mEmojiMap.put(0x1f450, R.drawable.emoji_1f450);
        mEmojiMap.put(0x1f446, R.drawable.emoji_1f446);
        mEmojiMap.put(0x1f447, R.drawable.emoji_1f447);
        mEmojiMap.put(0x1f449, R.drawable.emoji_1f449);
        mEmojiMap.put(0x1f448, R.drawable.emoji_1f448);
        mEmojiMap.put(0x1f64c, R.drawable.emoji_1f64c);
        mEmojiMap.put(0x1f64f, R.drawable.emoji_1f64f);
        mEmojiMap.put(0x261d, R.drawable.emoji_261d);
        mEmojiMap.put(0x1f44f, R.drawable.emoji_1f44f);
        mEmojiMap.put(0x1f4aa, R.drawable.emoji_1f4aa);
        mEmojiMap.put(0x1f6b6, R.drawable.emoji_1f6b6);
        mEmojiMap.put(0x1f3c3, R.drawable.emoji_1f3c3);
        mEmojiMap.put(0x1f483, R.drawable.emoji_1f483);
        mEmojiMap.put(0x1f46b, R.drawable.emoji_1f46b);
        mEmojiMap.put(0x1f46a, R.drawable.emoji_1f46a);
        mEmojiMap.put(0x1f46c, R.drawable.emoji_1f46c);
        mEmojiMap.put(0x1f46d, R.drawable.emoji_1f46d);
        mEmojiMap.put(0x1f48f, R.drawable.emoji_1f48f);
        mEmojiMap.put(0x1f491, R.drawable.emoji_1f491);
        mEmojiMap.put(0x1f46f, R.drawable.emoji_1f46f);
        mEmojiMap.put(0x1f646, R.drawable.emoji_1f646);
        mEmojiMap.put(0x1f645, R.drawable.emoji_1f645);
        mEmojiMap.put(0x1f481, R.drawable.emoji_1f481);
        mEmojiMap.put(0x1f64b, R.drawable.emoji_1f64b);
        mEmojiMap.put(0x1f486, R.drawable.emoji_1f486);
        mEmojiMap.put(0x1f487, R.drawable.emoji_1f487);
        mEmojiMap.put(0x1f485, R.drawable.emoji_1f485);
        mEmojiMap.put(0x1f470, R.drawable.emoji_1f470);
        mEmojiMap.put(0x1f64e, R.drawable.emoji_1f64e);
        mEmojiMap.put(0x1f64d, R.drawable.emoji_1f64d);
        mEmojiMap.put(0x1f647, R.drawable.emoji_1f647);
        mEmojiMap.put(0x1f3a9, R.drawable.emoji_1f3a9);
        mEmojiMap.put(0x1f451, R.drawable.emoji_1f451);
        mEmojiMap.put(0x1f452, R.drawable.emoji_1f452);
        mEmojiMap.put(0x1f45f, R.drawable.emoji_1f45f);
        mEmojiMap.put(0x1f45e, R.drawable.emoji_1f45e);
        mEmojiMap.put(0x1f461, R.drawable.emoji_1f461);
        mEmojiMap.put(0x1f460, R.drawable.emoji_1f460);
        mEmojiMap.put(0x1f462, R.drawable.emoji_1f462);
        mEmojiMap.put(0x1f455, R.drawable.emoji_1f455);
        mEmojiMap.put(0x1f454, R.drawable.emoji_1f454);
        mEmojiMap.put(0x1f45a, R.drawable.emoji_1f45a);
        mEmojiMap.put(0x1f457, R.drawable.emoji_1f457);
        mEmojiMap.put(0x1f3bd, R.drawable.emoji_1f3bd);
        mEmojiMap.put(0x1f456, R.drawable.emoji_1f456);
        mEmojiMap.put(0x1f458, R.drawable.emoji_1f458);
        mEmojiMap.put(0x1f459, R.drawable.emoji_1f459);
        mEmojiMap.put(0x1f4bc, R.drawable.emoji_1f4bc);
        mEmojiMap.put(0x1f45c, R.drawable.emoji_1f45c);
        mEmojiMap.put(0x1f45d, R.drawable.emoji_1f45d);
        mEmojiMap.put(0x1f45b, R.drawable.emoji_1f45b);
        mEmojiMap.put(0x1f453, R.drawable.emoji_1f453);
        mEmojiMap.put(0x1f380, R.drawable.emoji_1f380);
        mEmojiMap.put(0x1f302, R.drawable.emoji_1f302);
        mEmojiMap.put(0x1f484, R.drawable.emoji_1f484);
        mEmojiMap.put(0x1f49b, R.drawable.emoji_1f49b);
        mEmojiMap.put(0x1f499, R.drawable.emoji_1f499);
        mEmojiMap.put(0x1f49c, R.drawable.emoji_1f49c);
        mEmojiMap.put(0x1f49a, R.drawable.emoji_1f49a);
        mEmojiMap.put(0x2764, R.drawable.emoji_2764);
        mEmojiMap.put(0x1f494, R.drawable.emoji_1f494);
        mEmojiMap.put(0x1f497, R.drawable.emoji_1f497);
        mEmojiMap.put(0x1f493, R.drawable.emoji_1f493);
        mEmojiMap.put(0x1f495, R.drawable.emoji_1f495);
        mEmojiMap.put(0x1f496, R.drawable.emoji_1f496);
        mEmojiMap.put(0x1f49e, R.drawable.emoji_1f49e);
        mEmojiMap.put(0x1f498, R.drawable.emoji_1f498);
        mEmojiMap.put(0x1f48c, R.drawable.emoji_1f48c);
        mEmojiMap.put(0x1f48b, R.drawable.emoji_1f48b);
        mEmojiMap.put(0x1f48d, R.drawable.emoji_1f48d);
        mEmojiMap.put(0x1f48e, R.drawable.emoji_1f48e);
        mEmojiMap.put(0x1f464, R.drawable.emoji_1f464);
        mEmojiMap.put(0x1f465, R.drawable.emoji_1f465);
        mEmojiMap.put(0x1f4ac, R.drawable.emoji_1f4ac);
        mEmojiMap.put(0x1f463, R.drawable.emoji_1f463);
        mEmojiMap.put(0x1f4ad, R.drawable.emoji_1f4ad);

        // Nature
        mEmojiMap.put(0x1f436, R.drawable.emoji_1f436);
        mEmojiMap.put(0x1f43a, R.drawable.emoji_1f43a);
        mEmojiMap.put(0x1f431, R.drawable.emoji_1f431);
        mEmojiMap.put(0x1f42d, R.drawable.emoji_1f42d);
        mEmojiMap.put(0x1f439, R.drawable.emoji_1f439);
        mEmojiMap.put(0x1f430, R.drawable.emoji_1f430);
        mEmojiMap.put(0x1f438, R.drawable.emoji_1f438);
        mEmojiMap.put(0x1f42f, R.drawable.emoji_1f42f);
        mEmojiMap.put(0x1f428, R.drawable.emoji_1f428);
        mEmojiMap.put(0x1f43b, R.drawable.emoji_1f43b);
        mEmojiMap.put(0x1f437, R.drawable.emoji_1f437);
        mEmojiMap.put(0x1f43d, R.drawable.emoji_1f43d);
        mEmojiMap.put(0x1f42e, R.drawable.emoji_1f42e);
        mEmojiMap.put(0x1f417, R.drawable.emoji_1f417);
        mEmojiMap.put(0x1f435, R.drawable.emoji_1f435);
        mEmojiMap.put(0x1f412, R.drawable.emoji_1f412);
        mEmojiMap.put(0x1f434, R.drawable.emoji_1f434);
        mEmojiMap.put(0x1f411, R.drawable.emoji_1f411);
        mEmojiMap.put(0x1f418, R.drawable.emoji_1f418);
        mEmojiMap.put(0x1f43c, R.drawable.emoji_1f43c);
        mEmojiMap.put(0x1f427, R.drawable.emoji_1f427);
        mEmojiMap.put(0x1f426, R.drawable.emoji_1f426);
        mEmojiMap.put(0x1f424, R.drawable.emoji_1f424);
        mEmojiMap.put(0x1f425, R.drawable.emoji_1f425);
        mEmojiMap.put(0x1f423, R.drawable.emoji_1f423);
        mEmojiMap.put(0x1f414, R.drawable.emoji_1f414);
        mEmojiMap.put(0x1f40d, R.drawable.emoji_1f40d);
        mEmojiMap.put(0x1f422, R.drawable.emoji_1f422);
        mEmojiMap.put(0x1f41b, R.drawable.emoji_1f41b);
        mEmojiMap.put(0x1f41d, R.drawable.emoji_1f41d);
        mEmojiMap.put(0x1f41c, R.drawable.emoji_1f41c);
        mEmojiMap.put(0x1f41e, R.drawable.emoji_1f41e);
        mEmojiMap.put(0x1f40c, R.drawable.emoji_1f40c);
        mEmojiMap.put(0x1f419, R.drawable.emoji_1f419);
        mEmojiMap.put(0x1f41a, R.drawable.emoji_1f41a);
        mEmojiMap.put(0x1f420, R.drawable.emoji_1f420);
        mEmojiMap.put(0x1f41f, R.drawable.emoji_1f41f);
        mEmojiMap.put(0x1f42c, R.drawable.emoji_1f42c);
        mEmojiMap.put(0x1f433, R.drawable.emoji_1f433);
        mEmojiMap.put(0x1f40b, R.drawable.emoji_1f40b);
        mEmojiMap.put(0x1f404, R.drawable.emoji_1f404);
        mEmojiMap.put(0x1f40f, R.drawable.emoji_1f40f);
        mEmojiMap.put(0x1f400, R.drawable.emoji_1f400);
        mEmojiMap.put(0x1f403, R.drawable.emoji_1f403);
        mEmojiMap.put(0x1f405, R.drawable.emoji_1f405);
        mEmojiMap.put(0x1f407, R.drawable.emoji_1f407);
        mEmojiMap.put(0x1f409, R.drawable.emoji_1f409);
        mEmojiMap.put(0x1f40e, R.drawable.emoji_1f40e);
        mEmojiMap.put(0x1f410, R.drawable.emoji_1f410);
        mEmojiMap.put(0x1f413, R.drawable.emoji_1f413);
        mEmojiMap.put(0x1f415, R.drawable.emoji_1f415);
        mEmojiMap.put(0x1f416, R.drawable.emoji_1f416);
        mEmojiMap.put(0x1f401, R.drawable.emoji_1f401);
        mEmojiMap.put(0x1f402, R.drawable.emoji_1f402);
        mEmojiMap.put(0x1f432, R.drawable.emoji_1f432);
        mEmojiMap.put(0x1f421, R.drawable.emoji_1f421);
        mEmojiMap.put(0x1f40a, R.drawable.emoji_1f40a);
        mEmojiMap.put(0x1f42b, R.drawable.emoji_1f42b);
        mEmojiMap.put(0x1f42a, R.drawable.emoji_1f42a);
        mEmojiMap.put(0x1f406, R.drawable.emoji_1f406);
        mEmojiMap.put(0x1f408, R.drawable.emoji_1f408);
        mEmojiMap.put(0x1f429, R.drawable.emoji_1f429);
        mEmojiMap.put(0x1f43e, R.drawable.emoji_1f43e);
        mEmojiMap.put(0x1f490, R.drawable.emoji_1f490);
        mEmojiMap.put(0x1f338, R.drawable.emoji_1f338);
        mEmojiMap.put(0x1f337, R.drawable.emoji_1f337);
        mEmojiMap.put(0x1f340, R.drawable.emoji_1f340);
        mEmojiMap.put(0x1f339, R.drawable.emoji_1f339);
        mEmojiMap.put(0x1f33b, R.drawable.emoji_1f33b);
        mEmojiMap.put(0x1f33a, R.drawable.emoji_1f33a);
        mEmojiMap.put(0x1f341, R.drawable.emoji_1f341);
        mEmojiMap.put(0x1f343, R.drawable.emoji_1f343);
        mEmojiMap.put(0x1f342, R.drawable.emoji_1f342);
        mEmojiMap.put(0x1f33f, R.drawable.emoji_1f33f);
        mEmojiMap.put(0x1f33e, R.drawable.emoji_1f33e);
        mEmojiMap.put(0x1f344, R.drawable.emoji_1f344);
        mEmojiMap.put(0x1f335, R.drawable.emoji_1f335);
        mEmojiMap.put(0x1f334, R.drawable.emoji_1f334);
        mEmojiMap.put(0x1f332, R.drawable.emoji_1f332);
        mEmojiMap.put(0x1f333, R.drawable.emoji_1f333);
        mEmojiMap.put(0x1f330, R.drawable.emoji_1f330);
        mEmojiMap.put(0x1f331, R.drawable.emoji_1f331);
        mEmojiMap.put(0x1f33c, R.drawable.emoji_1f33c);
        mEmojiMap.put(0x1f310, R.drawable.emoji_1f310);
        mEmojiMap.put(0x1f31e, R.drawable.emoji_1f31e);
        mEmojiMap.put(0x1f31d, R.drawable.emoji_1f31d);
        mEmojiMap.put(0x1f31a, R.drawable.emoji_1f31a);
        mEmojiMap.put(0x1f311, R.drawable.emoji_1f311);
        mEmojiMap.put(0x1f312, R.drawable.emoji_1f312);
        mEmojiMap.put(0x1f313, R.drawable.emoji_1f313);
        mEmojiMap.put(0x1f314, R.drawable.emoji_1f314);
        mEmojiMap.put(0x1f315, R.drawable.emoji_1f315);
        mEmojiMap.put(0x1f316, R.drawable.emoji_1f316);
        mEmojiMap.put(0x1f317, R.drawable.emoji_1f317);
        mEmojiMap.put(0x1f318, R.drawable.emoji_1f318);
        mEmojiMap.put(0x1f31c, R.drawable.emoji_1f31c);
        mEmojiMap.put(0x1f31b, R.drawable.emoji_1f31b);
        mEmojiMap.put(0x1f319, R.drawable.emoji_1f319);
        mEmojiMap.put(0x1f30d, R.drawable.emoji_1f30d);
        mEmojiMap.put(0x1f30e, R.drawable.emoji_1f30e);
        mEmojiMap.put(0x1f30f, R.drawable.emoji_1f30f);
        mEmojiMap.put(0x1f30b, R.drawable.emoji_1f30b);
        mEmojiMap.put(0x1f30c, R.drawable.emoji_1f30c);
        mEmojiMap.put(0x1f320, R.drawable.emoji_1f303);
        mEmojiMap.put(0x2b50, R.drawable.emoji_2b50);
        mEmojiMap.put(0x2600, R.drawable.emoji_2600);
        mEmojiMap.put(0x26c5, R.drawable.emoji_26c5);
        mEmojiMap.put(0x2601, R.drawable.emoji_2601);
        mEmojiMap.put(0x26a1, R.drawable.emoji_26a1);
        mEmojiMap.put(0x2614, R.drawable.emoji_2614);
        mEmojiMap.put(0x2744, R.drawable.emoji_2744);
        mEmojiMap.put(0x26c4, R.drawable.emoji_26c4);
        mEmojiMap.put(0x1f300, R.drawable.emoji_1f300);
        mEmojiMap.put(0x1f301, R.drawable.emoji_1f301);
        mEmojiMap.put(0x1f308, R.drawable.emoji_1f308);
        mEmojiMap.put(0x1f30a, R.drawable.emoji_1f30a);

        // Objects
        mEmojiMap.put(0x1f38d, R.drawable.emoji_1f38d);
        mEmojiMap.put(0x1f49d, R.drawable.emoji_1f49d);
        mEmojiMap.put(0x1f38e, R.drawable.emoji_1f38e);
        mEmojiMap.put(0x1f392, R.drawable.emoji_1f392);
        mEmojiMap.put(0x1f393, R.drawable.emoji_1f393);
        mEmojiMap.put(0x1f38f, R.drawable.emoji_1f38f);
        mEmojiMap.put(0x1f386, R.drawable.emoji_1f386);
        mEmojiMap.put(0x1f387, R.drawable.emoji_1f387);
        mEmojiMap.put(0x1f390, R.drawable.emoji_1f390);
        mEmojiMap.put(0x1f391, R.drawable.emoji_1f391);
        mEmojiMap.put(0x1f383, R.drawable.emoji_1f383);
        mEmojiMap.put(0x1f47b, R.drawable.emoji_1f47b);
        mEmojiMap.put(0x1f385, R.drawable.emoji_1f385);
        mEmojiMap.put(0x1f384, R.drawable.emoji_1f384);
        mEmojiMap.put(0x1f381, R.drawable.emoji_1f381);
        mEmojiMap.put(0x1f38b, R.drawable.emoji_1f38b);
        mEmojiMap.put(0x1f389, R.drawable.emoji_1f389);
        mEmojiMap.put(0x1f38a, R.drawable.emoji_1f38a);
        mEmojiMap.put(0x1f388, R.drawable.emoji_1f388);
        mEmojiMap.put(0x1f38c, R.drawable.emoji_1f38c);
        mEmojiMap.put(0x1f52e, R.drawable.emoji_1f52e);
        mEmojiMap.put(0x1f3a5, R.drawable.emoji_1f3a5);
        mEmojiMap.put(0x1f4f7, R.drawable.emoji_1f4f7);
        mEmojiMap.put(0x1f4f9, R.drawable.emoji_1f4f9);
        mEmojiMap.put(0x1f4fc, R.drawable.emoji_1f4fc);
        mEmojiMap.put(0x1f4bf, R.drawable.emoji_1f4bf);
        mEmojiMap.put(0x1f4c0, R.drawable.emoji_1f4c0);
        mEmojiMap.put(0x1f4bd, R.drawable.emoji_1f4bd);
        mEmojiMap.put(0x1f4be, R.drawable.emoji_1f4be);
        mEmojiMap.put(0x1f4bb, R.drawable.emoji_1f4bb);
        mEmojiMap.put(0x1f4f1, R.drawable.emoji_1f4f1);
        mEmojiMap.put(0x260e, R.drawable.emoji_260e);
        mEmojiMap.put(0x1f4de, R.drawable.emoji_1f4de);
        mEmojiMap.put(0x1f4df, R.drawable.emoji_1f4df);
        mEmojiMap.put(0x1f4e0, R.drawable.emoji_1f4e0);
        mEmojiMap.put(0x1f4e1, R.drawable.emoji_1f4e1);
        mEmojiMap.put(0x1f4fa, R.drawable.emoji_1f4fa);
        mEmojiMap.put(0x1f4fb, R.drawable.emoji_1f4fb);
        mEmojiMap.put(0x1f50a, R.drawable.emoji_1f50a);
        mEmojiMap.put(0x1f509, R.drawable.emoji_1f509);
        mEmojiMap.put(0x1f508, R.drawable.emoji_1f508);
        mEmojiMap.put(0x1f507, R.drawable.emoji_1f507);
        mEmojiMap.put(0x1f514, R.drawable.emoji_1f514);
        mEmojiMap.put(0x1f515, R.drawable.emoji_1f515);
        mEmojiMap.put(0x1f4e2, R.drawable.emoji_1f4e2);
        mEmojiMap.put(0x1f4e3, R.drawable.emoji_1f4e3);
        mEmojiMap.put(0x23f3, R.drawable.emoji_23f3);
        mEmojiMap.put(0x231b, R.drawable.emoji_231b);
        mEmojiMap.put(0x23f0, R.drawable.emoji_23f0);
        mEmojiMap.put(0x231a, R.drawable.emoji_231a);
        mEmojiMap.put(0x1f513, R.drawable.emoji_1f513);
        mEmojiMap.put(0x1f512, R.drawable.emoji_1f512);
        mEmojiMap.put(0x1f50f, R.drawable.emoji_1f50f);
        mEmojiMap.put(0x1f510, R.drawable.emoji_1f510);
        mEmojiMap.put(0x1f511, R.drawable.emoji_1f511);
        mEmojiMap.put(0x1f50e, R.drawable.emoji_1f50e);
        mEmojiMap.put(0x1f4a1, R.drawable.emoji_1f4a1);
        mEmojiMap.put(0x1f526, R.drawable.emoji_1f526);
        mEmojiMap.put(0x1f506, R.drawable.emoji_1f506);
        mEmojiMap.put(0x1f505, R.drawable.emoji_1f505);
        mEmojiMap.put(0x1f50c, R.drawable.emoji_1f50c);
        mEmojiMap.put(0x1f50b, R.drawable.emoji_1f50b);
        mEmojiMap.put(0x1f50d, R.drawable.emoji_1f50d);
        mEmojiMap.put(0x1f6c1, R.drawable.emoji_1f6c1);
        mEmojiMap.put(0x1f6c0, R.drawable.emoji_1f6c0);
        mEmojiMap.put(0x1f6bf, R.drawable.emoji_1f6bf);
        mEmojiMap.put(0x1f6bd, R.drawable.emoji_1f6bd);
        mEmojiMap.put(0x1f527, R.drawable.emoji_1f527);
        mEmojiMap.put(0x1f529, R.drawable.emoji_1f529);
        mEmojiMap.put(0x1f528, R.drawable.emoji_1f528);
        mEmojiMap.put(0x1f6aa, R.drawable.emoji_1f6aa);
        mEmojiMap.put(0x1f6ac, R.drawable.emoji_1f6ac);
        mEmojiMap.put(0x1f4a3, R.drawable.emoji_1f4a3);
        mEmojiMap.put(0x1f52b, R.drawable.emoji_1f52b);
        mEmojiMap.put(0x1f52a, R.drawable.emoji_1f52a);
        mEmojiMap.put(0x1f48a, R.drawable.emoji_1f48a);
        mEmojiMap.put(0x1f489, R.drawable.emoji_1f489);
        mEmojiMap.put(0x1f4b0, R.drawable.emoji_1f4b0);
        mEmojiMap.put(0x1f4b4, R.drawable.emoji_1f4b4);
        mEmojiMap.put(0x1f4b5, R.drawable.emoji_1f4b5);
        mEmojiMap.put(0x1f4b7, R.drawable.emoji_1f4b7);
        mEmojiMap.put(0x1f4b6, R.drawable.emoji_1f4b6);
        mEmojiMap.put(0x1f4b3, R.drawable.emoji_1f4b3);
        mEmojiMap.put(0x1f4b8, R.drawable.emoji_1f4b8);
        mEmojiMap.put(0x1f4f2, R.drawable.emoji_1f4f2);
        mEmojiMap.put(0x1f4e7, R.drawable.emoji_1f4e7);
        mEmojiMap.put(0x1f4e5, R.drawable.emoji_1f4e5);
        mEmojiMap.put(0x1f4e4, R.drawable.emoji_1f4e4);
        mEmojiMap.put(0x2709, R.drawable.emoji_2709);
        mEmojiMap.put(0x1f4e9, R.drawable.emoji_1f4e9);
        mEmojiMap.put(0x1f4e8, R.drawable.emoji_1f4e8);
        mEmojiMap.put(0x1f4ef, R.drawable.emoji_1f4ef);
        mEmojiMap.put(0x1f4eb, R.drawable.emoji_1f4eb);
        mEmojiMap.put(0x1f4ea, R.drawable.emoji_1f4ea);
        mEmojiMap.put(0x1f4ec, R.drawable.emoji_1f4ec);
        mEmojiMap.put(0x1f4ed, R.drawable.emoji_1f4ed);
        mEmojiMap.put(0x1f4ee, R.drawable.emoji_1f4ee);
        mEmojiMap.put(0x1f4e6, R.drawable.emoji_1f4e6);
        mEmojiMap.put(0x1f4dd, R.drawable.emoji_1f4dd);
        mEmojiMap.put(0x1f4c4, R.drawable.emoji_1f4c4);
        mEmojiMap.put(0x1f4c3, R.drawable.emoji_1f4c3);
        mEmojiMap.put(0x1f4d1, R.drawable.emoji_1f4d1);
        mEmojiMap.put(0x1f4ca, R.drawable.emoji_1f4ca);
        mEmojiMap.put(0x1f4c8, R.drawable.emoji_1f4c8);
        mEmojiMap.put(0x1f4c9, R.drawable.emoji_1f4c9);
        mEmojiMap.put(0x1f4dc, R.drawable.emoji_1f4dc);
        mEmojiMap.put(0x1f4cb, R.drawable.emoji_1f4cb);
        mEmojiMap.put(0x1f4c5, R.drawable.emoji_1f4c5);
        mEmojiMap.put(0x1f4c6, R.drawable.emoji_1f4c6);
        mEmojiMap.put(0x1f4c7, R.drawable.emoji_1f4c7);
        mEmojiMap.put(0x1f4c1, R.drawable.emoji_1f4c1);
        mEmojiMap.put(0x1f4c2, R.drawable.emoji_1f4c2);
        mEmojiMap.put(0x2702, R.drawable.emoji_2702);
        mEmojiMap.put(0x1f4cc, R.drawable.emoji_1f4cc);
        mEmojiMap.put(0x1f4ce, R.drawable.emoji_1f4ce);
        mEmojiMap.put(0x2712, R.drawable.emoji_2712);
        mEmojiMap.put(0x270f, R.drawable.emoji_270f);
        mEmojiMap.put(0x1f4cf, R.drawable.emoji_1f4cf);
        mEmojiMap.put(0x1f4d0, R.drawable.emoji_1f4d0);
        mEmojiMap.put(0x1f4d5, R.drawable.emoji_1f4d5);
        mEmojiMap.put(0x1f4d7, R.drawable.emoji_1f4d7);
        mEmojiMap.put(0x1f4d8, R.drawable.emoji_1f4d8);
        mEmojiMap.put(0x1f4d9, R.drawable.emoji_1f4d9);
        mEmojiMap.put(0x1f4d3, R.drawable.emoji_1f4d3);
        mEmojiMap.put(0x1f4d4, R.drawable.emoji_1f4d4);
        mEmojiMap.put(0x1f4d2, R.drawable.emoji_1f4d2);
        mEmojiMap.put(0x1f4da, R.drawable.emoji_1f4da);
        mEmojiMap.put(0x1f4d6, R.drawable.emoji_1f4d6);
        mEmojiMap.put(0x1f516, R.drawable.emoji_1f516);
        mEmojiMap.put(0x1f4db, R.drawable.emoji_1f4db);
        mEmojiMap.put(0x1f52c, R.drawable.emoji_1f52c);
        mEmojiMap.put(0x1f52d, R.drawable.emoji_1f52d);
        mEmojiMap.put(0x1f4f0, R.drawable.emoji_1f4f0);
        mEmojiMap.put(0x1f3a8, R.drawable.emoji_1f3a8);
        mEmojiMap.put(0x1f3ac, R.drawable.emoji_1f3ac);
        mEmojiMap.put(0x1f3a4, R.drawable.emoji_1f3a4);
        mEmojiMap.put(0x1f3a7, R.drawable.emoji_1f3a7);
        mEmojiMap.put(0x1f3bc, R.drawable.emoji_1f3bc);
        mEmojiMap.put(0x1f3b5, R.drawable.emoji_1f3b5);
        mEmojiMap.put(0x1f3b6, R.drawable.emoji_1f3b6);
        mEmojiMap.put(0x1f3b9, R.drawable.emoji_1f3b9);
        mEmojiMap.put(0x1f3bb, R.drawable.emoji_1f3bb);
        mEmojiMap.put(0x1f3ba, R.drawable.emoji_1f3ba);
        mEmojiMap.put(0x1f3b7, R.drawable.emoji_1f3b7);
        mEmojiMap.put(0x1f3b8, R.drawable.emoji_1f3b8);
        mEmojiMap.put(0x1f47e, R.drawable.emoji_1f47e);
        mEmojiMap.put(0x1f3ae, R.drawable.emoji_1f3ae);
        mEmojiMap.put(0x1f0cf, R.drawable.emoji_1f0cf);
        mEmojiMap.put(0x1f3b4, R.drawable.emoji_1f3b4);
        mEmojiMap.put(0x1f004, R.drawable.emoji_1f004);
        mEmojiMap.put(0x1f3b2, R.drawable.emoji_1f3b2);
        mEmojiMap.put(0x1f3af, R.drawable.emoji_1f3af);
        mEmojiMap.put(0x1f3c8, R.drawable.emoji_1f3c8);
        mEmojiMap.put(0x1f3c0, R.drawable.emoji_1f3c0);
        mEmojiMap.put(0x26bd, R.drawable.emoji_26bd);
        mEmojiMap.put(0x26be, R.drawable.emoji_26be);
        mEmojiMap.put(0x1f3be, R.drawable.emoji_1f3be);
        mEmojiMap.put(0x1f3b1, R.drawable.emoji_1f3b1);
        mEmojiMap.put(0x1f3c9, R.drawable.emoji_1f3c9);
        mEmojiMap.put(0x1f3b3, R.drawable.emoji_1f3b3);
        mEmojiMap.put(0x26f3, R.drawable.emoji_26f3);
        mEmojiMap.put(0x1f6b5, R.drawable.emoji_1f6b5);
        mEmojiMap.put(0x1f6b4, R.drawable.emoji_1f6b4);
        mEmojiMap.put(0x1f3c1, R.drawable.emoji_1f3c1);
        mEmojiMap.put(0x1f3c7, R.drawable.emoji_1f3c7);
        mEmojiMap.put(0x1f3c6, R.drawable.emoji_1f3c6);
        mEmojiMap.put(0x1f3bf, R.drawable.emoji_1f3bf);
        mEmojiMap.put(0x1f3c2, R.drawable.emoji_1f3c2);
        mEmojiMap.put(0x1f3ca, R.drawable.emoji_1f3ca);
        mEmojiMap.put(0x1f3c4, R.drawable.emoji_1f3c4);
        mEmojiMap.put(0x1f3a3, R.drawable.emoji_1f3a3);
        mEmojiMap.put(0x2615, R.drawable.emoji_2615);
        mEmojiMap.put(0x1f375, R.drawable.emoji_1f375);
        mEmojiMap.put(0x1f376, R.drawable.emoji_1f376);
        mEmojiMap.put(0x1f37c, R.drawable.emoji_1f37c);
        mEmojiMap.put(0x1f37a, R.drawable.emoji_1f37a);
        mEmojiMap.put(0x1f37b, R.drawable.emoji_1f37b);
        mEmojiMap.put(0x1f378, R.drawable.emoji_1f378);
        mEmojiMap.put(0x1f379, R.drawable.emoji_1f379);
        mEmojiMap.put(0x1f377, R.drawable.emoji_1f377);
        mEmojiMap.put(0x1f374, R.drawable.emoji_1f374);
        mEmojiMap.put(0x1f355, R.drawable.emoji_1f355);
        mEmojiMap.put(0x1f354, R.drawable.emoji_1f354);
        mEmojiMap.put(0x1f35f, R.drawable.emoji_1f35f);
        mEmojiMap.put(0x1f357, R.drawable.emoji_1f357);
        mEmojiMap.put(0x1f356, R.drawable.emoji_1f356);
        mEmojiMap.put(0x1f35d, R.drawable.emoji_1f35d);
        mEmojiMap.put(0x1f35b, R.drawable.emoji_1f35b);
        mEmojiMap.put(0x1f364, R.drawable.emoji_1f364);
        mEmojiMap.put(0x1f371, R.drawable.emoji_1f371);
        mEmojiMap.put(0x1f363, R.drawable.emoji_1f363);
        mEmojiMap.put(0x1f365, R.drawable.emoji_1f365);
        mEmojiMap.put(0x1f359, R.drawable.emoji_1f359);
        mEmojiMap.put(0x1f358, R.drawable.emoji_1f358);
        mEmojiMap.put(0x1f35a, R.drawable.emoji_1f35a);
        mEmojiMap.put(0x1f35c, R.drawable.emoji_1f35c);
        mEmojiMap.put(0x1f372, R.drawable.emoji_1f372);
        mEmojiMap.put(0x1f362, R.drawable.emoji_1f362);
        mEmojiMap.put(0x1f361, R.drawable.emoji_1f361);
        mEmojiMap.put(0x1f373, R.drawable.emoji_1f373);
        mEmojiMap.put(0x1f35e, R.drawable.emoji_1f35e);
        mEmojiMap.put(0x1f369, R.drawable.emoji_1f369);
        mEmojiMap.put(0x1f36e, R.drawable.emoji_1f36e);
        mEmojiMap.put(0x1f366, R.drawable.emoji_1f366);
        mEmojiMap.put(0x1f368, R.drawable.emoji_1f368);
        mEmojiMap.put(0x1f367, R.drawable.emoji_1f367);
        mEmojiMap.put(0x1f382, R.drawable.emoji_1f382);
        mEmojiMap.put(0x1f370, R.drawable.emoji_1f370);
        mEmojiMap.put(0x1f36a, R.drawable.emoji_1f36a);
        mEmojiMap.put(0x1f36b, R.drawable.emoji_1f36b);
        mEmojiMap.put(0x1f36c, R.drawable.emoji_1f36c);
        mEmojiMap.put(0x1f36d, R.drawable.emoji_1f36d);
        mEmojiMap.put(0x1f36f, R.drawable.emoji_1f36f);
        mEmojiMap.put(0x1f34e, R.drawable.emoji_1f34e);
        mEmojiMap.put(0x1f34f, R.drawable.emoji_1f34f);
        mEmojiMap.put(0x1f34a, R.drawable.emoji_1f34a);
        mEmojiMap.put(0x1f34b, R.drawable.emoji_1f34b);
        mEmojiMap.put(0x1f352, R.drawable.emoji_1f352);
        mEmojiMap.put(0x1f347, R.drawable.emoji_1f347);
        mEmojiMap.put(0x1f349, R.drawable.emoji_1f349);
        mEmojiMap.put(0x1f353, R.drawable.emoji_1f353);
        mEmojiMap.put(0x1f351, R.drawable.emoji_1f351);
        mEmojiMap.put(0x1f348, R.drawable.emoji_1f348);
        mEmojiMap.put(0x1f34c, R.drawable.emoji_1f34c);
        mEmojiMap.put(0x1f350, R.drawable.emoji_1f350);
        mEmojiMap.put(0x1f34d, R.drawable.emoji_1f34d);
        mEmojiMap.put(0x1f360, R.drawable.emoji_1f360);
        mEmojiMap.put(0x1f346, R.drawable.emoji_1f346);
        mEmojiMap.put(0x1f345, R.drawable.emoji_1f345);
        mEmojiMap.put(0x1f33d, R.drawable.emoji_1f33d);

        // Places
        mEmojiMap.put(0x1f3e0, R.drawable.emoji_1f3e0);
        mEmojiMap.put(0x1f3e1, R.drawable.emoji_1f3e1);
        mEmojiMap.put(0x1f3eb, R.drawable.emoji_1f3eb);
        mEmojiMap.put(0x1f3e2, R.drawable.emoji_1f3e2);
        mEmojiMap.put(0x1f3e3, R.drawable.emoji_1f3e3);
        mEmojiMap.put(0x1f3e5, R.drawable.emoji_1f3e5);
        mEmojiMap.put(0x1f3e6, R.drawable.emoji_1f3e6);
        mEmojiMap.put(0x1f3ea, R.drawable.emoji_1f3ea);
        mEmojiMap.put(0x1f3e9, R.drawable.emoji_1f3e9);
        mEmojiMap.put(0x1f3e8, R.drawable.emoji_1f3e8);
        mEmojiMap.put(0x1f492, R.drawable.emoji_1f492);
        mEmojiMap.put(0x26ea, R.drawable.emoji_26ea);
        mEmojiMap.put(0x1f3ec, R.drawable.emoji_1f3ec);
        mEmojiMap.put(0x1f3e4, R.drawable.emoji_1f3e4);
        mEmojiMap.put(0x1f307, R.drawable.emoji_1f307);
        mEmojiMap.put(0x1f306, R.drawable.emoji_1f306);
        mEmojiMap.put(0x1f3ef, R.drawable.emoji_1f3ef);
        mEmojiMap.put(0x1f3f0, R.drawable.emoji_1f3f0);
        mEmojiMap.put(0x26fa, R.drawable.emoji_26fa);
        mEmojiMap.put(0x1f3ed, R.drawable.emoji_1f3ed);
        mEmojiMap.put(0x1f5fc, R.drawable.emoji_1f5fc);
        mEmojiMap.put(0x1f5fe, R.drawable.emoji_1f5fe);
        mEmojiMap.put(0x1f5fb, R.drawable.emoji_1f5fb);
        mEmojiMap.put(0x1f304, R.drawable.emoji_1f304);
        mEmojiMap.put(0x1f305, R.drawable.emoji_1f305);
        mEmojiMap.put(0x1f303, R.drawable.emoji_1f303);
        mEmojiMap.put(0x1f5fd, R.drawable.emoji_1f5fd);
        mEmojiMap.put(0x1f309, R.drawable.emoji_1f309);
        mEmojiMap.put(0x1f3a0, R.drawable.emoji_1f3a0);
        mEmojiMap.put(0x1f3a1, R.drawable.emoji_1f3a1);
        mEmojiMap.put(0x26f2, R.drawable.emoji_26f2);
        mEmojiMap.put(0x1f3a2, R.drawable.emoji_1f3a2);
        mEmojiMap.put(0x1f6a2, R.drawable.emoji_1f6a2);
        mEmojiMap.put(0x26f5, R.drawable.emoji_26f5);
        mEmojiMap.put(0x1f6a4, R.drawable.emoji_1f6a4);
        mEmojiMap.put(0x1f6a3, R.drawable.emoji_1f6a3);
        mEmojiMap.put(0x2693, R.drawable.emoji_2693);
        mEmojiMap.put(0x1f680, R.drawable.emoji_1f680);
        mEmojiMap.put(0x2708, R.drawable.emoji_2708);
        mEmojiMap.put(0x1f4ba, R.drawable.emoji_1f4ba);
        mEmojiMap.put(0x1f681, R.drawable.emoji_1f681);
        mEmojiMap.put(0x1f682, R.drawable.emoji_1f682);
        mEmojiMap.put(0x1f68a, R.drawable.emoji_1f68a);
        mEmojiMap.put(0x1f689, R.drawable.emoji_1f689);
        mEmojiMap.put(0x1f69e, R.drawable.emoji_1f69e);
        mEmojiMap.put(0x1f686, R.drawable.emoji_1f686);
        mEmojiMap.put(0x1f684, R.drawable.emoji_1f684);
        mEmojiMap.put(0x1f685, R.drawable.emoji_1f685);
        mEmojiMap.put(0x1f688, R.drawable.emoji_1f688);
        mEmojiMap.put(0x1f687, R.drawable.emoji_1f687);
        mEmojiMap.put(0x1f69d, R.drawable.emoji_1f69d);
        mEmojiMap.put(0x1f68b, R.drawable.emoji_1f68b);
        mEmojiMap.put(0x1f683, R.drawable.emoji_1f683);
        mEmojiMap.put(0x1f68e, R.drawable.emoji_1f68e);
        mEmojiMap.put(0x1f68c, R.drawable.emoji_1f68c);
        mEmojiMap.put(0x1f68d, R.drawable.emoji_1f68d);
        mEmojiMap.put(0x1f699, R.drawable.emoji_1f699);
        mEmojiMap.put(0x1f698, R.drawable.emoji_1f698);
        mEmojiMap.put(0x1f697, R.drawable.emoji_1f697);
        mEmojiMap.put(0x1f695, R.drawable.emoji_1f695);
        mEmojiMap.put(0x1f696, R.drawable.emoji_1f696);
        mEmojiMap.put(0x1f69b, R.drawable.emoji_1f69b);
        mEmojiMap.put(0x1f69a, R.drawable.emoji_1f69a);
        mEmojiMap.put(0x1f6a8, R.drawable.emoji_1f6a8);
        mEmojiMap.put(0x1f693, R.drawable.emoji_1f693);
        mEmojiMap.put(0x1f694, R.drawable.emoji_1f694);
        mEmojiMap.put(0x1f692, R.drawable.emoji_1f692);
        mEmojiMap.put(0x1f691, R.drawable.emoji_1f691);
        mEmojiMap.put(0x1f690, R.drawable.emoji_1f690);
        mEmojiMap.put(0x1f6b2, R.drawable.emoji_1f6b2);
        mEmojiMap.put(0x1f6a1, R.drawable.emoji_1f6a1);
        mEmojiMap.put(0x1f69f, R.drawable.emoji_1f69f);
        mEmojiMap.put(0x1f6a0, R.drawable.emoji_1f6a0);
        mEmojiMap.put(0x1f69c, R.drawable.emoji_1f69c);
        mEmojiMap.put(0x1f488, R.drawable.emoji_1f488);
        mEmojiMap.put(0x1f68f, R.drawable.emoji_1f68f);
        mEmojiMap.put(0x1f3ab, R.drawable.emoji_1f3ab);
        mEmojiMap.put(0x1f6a6, R.drawable.emoji_1f6a6);
        mEmojiMap.put(0x1f6a5, R.drawable.emoji_1f6a5);
        mEmojiMap.put(0x26a0, R.drawable.emoji_26a0);
        mEmojiMap.put(0x1f6a7, R.drawable.emoji_1f6a7);
        mEmojiMap.put(0x1f530, R.drawable.emoji_1f530);
        mEmojiMap.put(0x26fd, R.drawable.emoji_26fd);
        mEmojiMap.put(0x1f3ee, R.drawable.emoji_1f3ee);
        mEmojiMap.put(0x1f3b0, R.drawable.emoji_1f3b0);
        mEmojiMap.put(0x2668, R.drawable.emoji_2668);
        mEmojiMap.put(0x1f5ff, R.drawable.emoji_1f5ff);
        mEmojiMap.put(0x1f3aa, R.drawable.emoji_1f3aa);
        mEmojiMap.put(0x1f3ad, R.drawable.emoji_1f3ad);
        mEmojiMap.put(0x1f4cd, R.drawable.emoji_1f4cd);
        mEmojiMap.put(0x1f6a9, R.drawable.emoji_1f6a9);
        mEmojiMap.put(0x1f51f, R.drawable.emoji_1f51f);
        mEmojiMap.put(0x1f522, R.drawable.emoji_1f522);
        mEmojiMap.put(0x1f523, R.drawable.emoji_1f523);
        mEmojiMap.put(0x2b06, R.drawable.emoji_2b06);
        mEmojiMap.put(0x2b07, R.drawable.emoji_2b07);
        mEmojiMap.put(0x2b05, R.drawable.emoji_2b05);
        mEmojiMap.put(0x27a1, R.drawable.emoji_27a1);
        mEmojiMap.put(0x1f520, R.drawable.emoji_1f520);
        mEmojiMap.put(0x1f521, R.drawable.emoji_1f521);
        mEmojiMap.put(0x1f524, R.drawable.emoji_1f524);
        mEmojiMap.put(0x2197, R.drawable.emoji_2197);
        mEmojiMap.put(0x2196, R.drawable.emoji_2196);
        mEmojiMap.put(0x2198, R.drawable.emoji_2198);
        mEmojiMap.put(0x2199, R.drawable.emoji_2199);
        mEmojiMap.put(0x2194, R.drawable.emoji_2194);
        mEmojiMap.put(0x2195, R.drawable.emoji_2195);
        mEmojiMap.put(0x1f504, R.drawable.emoji_1f504);
        mEmojiMap.put(0x25c0, R.drawable.emoji_25c0);
        mEmojiMap.put(0x25b6, R.drawable.emoji_25b6);
        mEmojiMap.put(0x1f53c, R.drawable.emoji_1f53c);
        mEmojiMap.put(0x1f53d, R.drawable.emoji_1f53d);
        mEmojiMap.put(0x21a9, R.drawable.emoji_21a9);
        mEmojiMap.put(0x21aa, R.drawable.emoji_21aa);
        mEmojiMap.put(0x2139, R.drawable.emoji_2139);
        mEmojiMap.put(0x23ea, R.drawable.emoji_23ea);
        mEmojiMap.put(0x23e9, R.drawable.emoji_23e9);
        mEmojiMap.put(0x23eb, R.drawable.emoji_23eb);
        mEmojiMap.put(0x23ec, R.drawable.emoji_23ec);
        mEmojiMap.put(0x2935, R.drawable.emoji_2935);
        mEmojiMap.put(0x2934, R.drawable.emoji_2934);
        mEmojiMap.put(0x1f197, R.drawable.emoji_1f197);
        mEmojiMap.put(0x1f500, R.drawable.emoji_1f500);
        mEmojiMap.put(0x1f501, R.drawable.emoji_1f501);
        mEmojiMap.put(0x1f502, R.drawable.emoji_1f502);
        mEmojiMap.put(0x1f195, R.drawable.emoji_1f195);
        mEmojiMap.put(0x1f199, R.drawable.emoji_1f199);
        mEmojiMap.put(0x1f192, R.drawable.emoji_1f192);
        mEmojiMap.put(0x1f193, R.drawable.emoji_1f193);
        mEmojiMap.put(0x1f196, R.drawable.emoji_1f196);
        mEmojiMap.put(0x1f4f6, R.drawable.emoji_1f4f6);
        mEmojiMap.put(0x1f3a6, R.drawable.emoji_1f3a6);
        mEmojiMap.put(0x1f201, R.drawable.emoji_1f201);
        mEmojiMap.put(0x1f22f, R.drawable.emoji_1f22f);
        mEmojiMap.put(0x1f233, R.drawable.emoji_1f233);
        mEmojiMap.put(0x1f235, R.drawable.emoji_1f235);
        mEmojiMap.put(0x1f234, R.drawable.emoji_1f234);
        mEmojiMap.put(0x1f232, R.drawable.emoji_1f232);
        mEmojiMap.put(0x1f250, R.drawable.emoji_1f250);
        mEmojiMap.put(0x1f239, R.drawable.emoji_1f239);
        mEmojiMap.put(0x1f23a, R.drawable.emoji_1f23a);
        mEmojiMap.put(0x1f236, R.drawable.emoji_1f236);
        mEmojiMap.put(0x1f21a, R.drawable.emoji_1f21a);
        mEmojiMap.put(0x1f6bb, R.drawable.emoji_1f6bb);
        mEmojiMap.put(0x1f6b9, R.drawable.emoji_1f6b9);
        mEmojiMap.put(0x1f6ba, R.drawable.emoji_1f6ba);
        mEmojiMap.put(0x1f6bc, R.drawable.emoji_1f6bc);
        mEmojiMap.put(0x1f6be, R.drawable.emoji_1f6be);
        mEmojiMap.put(0x1f6b0, R.drawable.emoji_1f6b0);
        mEmojiMap.put(0x1f6ae, R.drawable.emoji_1f6ae);
        mEmojiMap.put(0x1f17f, R.drawable.emoji_1f17f);
        mEmojiMap.put(0x267f, R.drawable.emoji_267f);
        mEmojiMap.put(0x1f6ad, R.drawable.emoji_1f6ad);
        mEmojiMap.put(0x1f237, R.drawable.emoji_1f237);
        mEmojiMap.put(0x1f238, R.drawable.emoji_1f238);
        mEmojiMap.put(0x1f202, R.drawable.emoji_1f202);
        mEmojiMap.put(0x24c2, R.drawable.emoji_24c2);
        mEmojiMap.put(0x1f6c2, R.drawable.emoji_1f6c2);
        mEmojiMap.put(0x1f6c4, R.drawable.emoji_1f6c4);
        mEmojiMap.put(0x1f6c5, R.drawable.emoji_1f6c5);
        mEmojiMap.put(0x1f6c3, R.drawable.emoji_1f6c3);
        mEmojiMap.put(0x1f251, R.drawable.emoji_1f251);
        mEmojiMap.put(0x3299, R.drawable.emoji_3299);
        mEmojiMap.put(0x3297, R.drawable.emoji_3297);
        mEmojiMap.put(0x1f191, R.drawable.emoji_1f191);
        mEmojiMap.put(0x1f198, R.drawable.emoji_1f198);
        mEmojiMap.put(0x1f194, R.drawable.emoji_1f194);
        mEmojiMap.put(0x1f6ab, R.drawable.emoji_1f6ab);
        mEmojiMap.put(0x1f51e, R.drawable.emoji_1f51e);
        mEmojiMap.put(0x1f4f5, R.drawable.emoji_1f4f5);
        mEmojiMap.put(0x1f6af, R.drawable.emoji_1f6af);
        mEmojiMap.put(0x1f6b1, R.drawable.emoji_1f6b1);
        mEmojiMap.put(0x1f6b3, R.drawable.emoji_1f6b3);
        mEmojiMap.put(0x1f6b7, R.drawable.emoji_1f6b7);
        mEmojiMap.put(0x1f6b8, R.drawable.emoji_1f6b8);
        mEmojiMap.put(0x26d4, R.drawable.emoji_26d4);
        mEmojiMap.put(0x2733, R.drawable.emoji_2733);
        mEmojiMap.put(0x2747, R.drawable.emoji_2747);
        mEmojiMap.put(0x274e, R.drawable.emoji_274e);
        mEmojiMap.put(0x2705, R.drawable.emoji_2705);
        mEmojiMap.put(0x2734, R.drawable.emoji_2734);
        mEmojiMap.put(0x1f49f, R.drawable.emoji_1f49f);
        mEmojiMap.put(0x1f19a, R.drawable.emoji_1f19a);
        mEmojiMap.put(0x1f4f3, R.drawable.emoji_1f4f3);
        mEmojiMap.put(0x1f4f4, R.drawable.emoji_1f4f4);
        mEmojiMap.put(0x1f170, R.drawable.emoji_1f170);
        mEmojiMap.put(0x1f171, R.drawable.emoji_1f171);
        mEmojiMap.put(0x1f18e, R.drawable.emoji_1f18e);
        mEmojiMap.put(0x1f17e, R.drawable.emoji_1f17e);
        mEmojiMap.put(0x1f4a0, R.drawable.emoji_1f4a0);
        mEmojiMap.put(0x27bf, R.drawable.emoji_27bf);
        mEmojiMap.put(0x267b, R.drawable.emoji_267b);
        mEmojiMap.put(0x2648, R.drawable.emoji_2648);
        mEmojiMap.put(0x2649, R.drawable.emoji_2649);
        mEmojiMap.put(0x264a, R.drawable.emoji_264a);
        mEmojiMap.put(0x264b, R.drawable.emoji_264b);
        mEmojiMap.put(0x264c, R.drawable.emoji_264c);
        mEmojiMap.put(0x264d, R.drawable.emoji_264d);
        mEmojiMap.put(0x264e, R.drawable.emoji_264e);
        mEmojiMap.put(0x264f, R.drawable.emoji_264f);
        mEmojiMap.put(0x2650, R.drawable.emoji_2650);
        mEmojiMap.put(0x2651, R.drawable.emoji_2651);
        mEmojiMap.put(0x2652, R.drawable.emoji_2652);
        mEmojiMap.put(0x2653, R.drawable.emoji_2653);
        mEmojiMap.put(0x26ce, R.drawable.emoji_26ce);
        mEmojiMap.put(0x1f52f, R.drawable.emoji_1f52f);
        mEmojiMap.put(0x1f3e7, R.drawable.emoji_1f3e7);
        mEmojiMap.put(0x1f4b9, R.drawable.emoji_1f4b9);
        mEmojiMap.put(0x1f4b2, R.drawable.emoji_1f4b2);
        mEmojiMap.put(0x1f4b1, R.drawable.emoji_1f4b1);
        mEmojiMap.put(0x00a9, R.drawable.emoji_00a9);
        mEmojiMap.put(0x00ae, R.drawable.emoji_00ae);
        mEmojiMap.put(0x2122, R.drawable.emoji_2122);
        mEmojiMap.put(0x274c, R.drawable.emoji_274c);
        mEmojiMap.put(0x203c, R.drawable.emoji_203c);
        mEmojiMap.put(0x2049, R.drawable.emoji_2049);
        mEmojiMap.put(0x2757, R.drawable.emoji_2757);
        mEmojiMap.put(0x2753, R.drawable.emoji_2753);
        mEmojiMap.put(0x2755, R.drawable.emoji_2755);
        mEmojiMap.put(0x2754, R.drawable.emoji_2754);
        mEmojiMap.put(0x2b55, R.drawable.emoji_2b55);
        mEmojiMap.put(0x1f51d, R.drawable.emoji_1f51d);
        mEmojiMap.put(0x1f51a, R.drawable.emoji_1f51a);
        mEmojiMap.put(0x1f519, R.drawable.emoji_1f519);
        mEmojiMap.put(0x1f51b, R.drawable.emoji_1f51b);
        mEmojiMap.put(0x1f51c, R.drawable.emoji_1f51c);
        mEmojiMap.put(0x1f503, R.drawable.emoji_1f503);
        mEmojiMap.put(0x1f55b, R.drawable.emoji_1f55b);
        mEmojiMap.put(0x1f567, R.drawable.emoji_1f567);
        mEmojiMap.put(0x1f550, R.drawable.emoji_1f550);
        mEmojiMap.put(0x1f55c, R.drawable.emoji_1f55c);
        mEmojiMap.put(0x1f551, R.drawable.emoji_1f551);
        mEmojiMap.put(0x1f55d, R.drawable.emoji_1f55d);
        mEmojiMap.put(0x1f552, R.drawable.emoji_1f552);
        mEmojiMap.put(0x1f55e, R.drawable.emoji_1f55e);
        mEmojiMap.put(0x1f553, R.drawable.emoji_1f553);
        mEmojiMap.put(0x1f55f, R.drawable.emoji_1f55f);
        mEmojiMap.put(0x1f554, R.drawable.emoji_1f554);
        mEmojiMap.put(0x1f560, R.drawable.emoji_1f560);
        mEmojiMap.put(0x1f555, R.drawable.emoji_1f555);
        mEmojiMap.put(0x1f556, R.drawable.emoji_1f556);
        mEmojiMap.put(0x1f557, R.drawable.emoji_1f557);
        mEmojiMap.put(0x1f558, R.drawable.emoji_1f558);
        mEmojiMap.put(0x1f559, R.drawable.emoji_1f559);
        mEmojiMap.put(0x1f55a, R.drawable.emoji_1f55a);
        mEmojiMap.put(0x1f561, R.drawable.emoji_1f561);
        mEmojiMap.put(0x1f562, R.drawable.emoji_1f562);
        mEmojiMap.put(0x1f563, R.drawable.emoji_1f563);
        mEmojiMap.put(0x1f564, R.drawable.emoji_1f564);
        mEmojiMap.put(0x1f565, R.drawable.emoji_1f565);
        mEmojiMap.put(0x1f566, R.drawable.emoji_1f566);
        mEmojiMap.put(0x2716, R.drawable.emoji_2716);
        mEmojiMap.put(0x2795, R.drawable.emoji_2795);
        mEmojiMap.put(0x2796, R.drawable.emoji_2796);
        mEmojiMap.put(0x2797, R.drawable.emoji_2797);
        mEmojiMap.put(0x2660, R.drawable.emoji_2660);
        mEmojiMap.put(0x2665, R.drawable.emoji_2665);
        mEmojiMap.put(0x2663, R.drawable.emoji_2663);
        mEmojiMap.put(0x2666, R.drawable.emoji_2666);
        mEmojiMap.put(0x1f4ae, R.drawable.emoji_1f4ae);
        mEmojiMap.put(0x1f4af, R.drawable.emoji_1f4af);
        mEmojiMap.put(0x2714, R.drawable.emoji_2714);
        mEmojiMap.put(0x2611, R.drawable.emoji_2611);
        mEmojiMap.put(0x1f518, R.drawable.emoji_1f518);
        mEmojiMap.put(0x1f517, R.drawable.emoji_1f517);
        mEmojiMap.put(0x27b0, R.drawable.emoji_27b0);
        mEmojiMap.put(0x3030, R.drawable.emoji_3030);
        mEmojiMap.put(0x303d, R.drawable.emoji_303d);
        mEmojiMap.put(0x1f531, R.drawable.emoji_1f531);
        mEmojiMap.put(0x25fc, R.drawable.emoji_25fc);
        mEmojiMap.put(0x25fb, R.drawable.emoji_25fb);
        mEmojiMap.put(0x25fe, R.drawable.emoji_25fe);
        mEmojiMap.put(0x25fd, R.drawable.emoji_25fd);
        mEmojiMap.put(0x25aa, R.drawable.emoji_25aa);
        mEmojiMap.put(0x25ab, R.drawable.emoji_25ab);
        mEmojiMap.put(0x1f53a, R.drawable.emoji_1f53a);
        mEmojiMap.put(0x1f532, R.drawable.emoji_1f532);
        mEmojiMap.put(0x1f533, R.drawable.emoji_1f533);
        mEmojiMap.put(0x26ab, R.drawable.emoji_26ab);
        mEmojiMap.put(0x26aa, R.drawable.emoji_26aa);
        mEmojiMap.put(0x1f534, R.drawable.emoji_1f534);
        mEmojiMap.put(0x1f535, R.drawable.emoji_1f535);
        mEmojiMap.put(0x1f53b, R.drawable.emoji_1f53b);
        mEmojiMap.put(0x2b1c, R.drawable.emoji_2b1c);
        mEmojiMap.put(0x2b1b, R.drawable.emoji_2b1b);
        mEmojiMap.put(0x1f536, R.drawable.emoji_1f536);
        mEmojiMap.put(0x1f537, R.drawable.emoji_1f537);
        mEmojiMap.put(0x1f538, R.drawable.emoji_1f538);
        mEmojiMap.put(0x1f539, R.drawable.emoji_1f539);

        mBankMap.put(0xe001, R.drawable.emoji_1f466);
        mBankMap.put(0xe002, R.drawable.emoji_1f467);
        mBankMap.put(0xe003, R.drawable.emoji_1f48b);
        mBankMap.put(0xe004, R.drawable.emoji_1f468);
        mBankMap.put(0xe005, R.drawable.emoji_1f469);
        mBankMap.put(0xe006, R.drawable.emoji_1f455);
        mBankMap.put(0xe007, R.drawable.emoji_1f45e);
        mBankMap.put(0xe008, R.drawable.emoji_1f4f7);
        mBankMap.put(0xe009, R.drawable.emoji_1f4de);
        mBankMap.put(0xe00a, R.drawable.emoji_1f4f1);
        mBankMap.put(0xe00b, R.drawable.emoji_1f4e0);
        mBankMap.put(0xe00c, R.drawable.emoji_1f4bb);
        mBankMap.put(0xe00d, R.drawable.emoji_1f44a);
        mBankMap.put(0xe00e, R.drawable.emoji_1f44d);
        mBankMap.put(0xe00f, R.drawable.emoji_261d);
        mBankMap.put(0xe010, R.drawable.emoji_270a);
        mBankMap.put(0xe011, R.drawable.emoji_270c);
        mBankMap.put(0xe012, R.drawable.emoji_1f64b);
        mBankMap.put(0xe013, R.drawable.emoji_1f3bf);
        mBankMap.put(0xe014, R.drawable.emoji_26f3);
        mBankMap.put(0xe015, R.drawable.emoji_1f3be);
        mBankMap.put(0xe016, R.drawable.emoji_26be);
        mBankMap.put(0xe017, R.drawable.emoji_1f3c4);
        mBankMap.put(0xe018, R.drawable.emoji_26bd);
        mBankMap.put(0xe019, R.drawable.emoji_1f3a3);
        mBankMap.put(0xe01a, R.drawable.emoji_1f434);
        mBankMap.put(0xe01b, R.drawable.emoji_1f697);
        mBankMap.put(0xe01c, R.drawable.emoji_26f5);
        mBankMap.put(0xe01d, R.drawable.emoji_2708);
        mBankMap.put(0xe01e, R.drawable.emoji_1f683);
        mBankMap.put(0xe01f, R.drawable.emoji_1f685);
        mBankMap.put(0xe020, R.drawable.emoji_2753);
        mBankMap.put(0xe021, R.drawable.emoji_2757);
        mBankMap.put(0xe022, R.drawable.emoji_2764);
        mBankMap.put(0xe023, R.drawable.emoji_1f494);
        mBankMap.put(0xe024, R.drawable.emoji_1f550);
        mBankMap.put(0xe025, R.drawable.emoji_1f551);
        mBankMap.put(0xe026, R.drawable.emoji_1f552);
        mBankMap.put(0xe027, R.drawable.emoji_1f553);
        mBankMap.put(0xe028, R.drawable.emoji_1f554);
        mBankMap.put(0xe029, R.drawable.emoji_1f555);
        mBankMap.put(0xe02a, R.drawable.emoji_1f556);
        mBankMap.put(0xe02b, R.drawable.emoji_1f557);
        mBankMap.put(0xe02c, R.drawable.emoji_1f558);
        mBankMap.put(0xe02d, R.drawable.emoji_1f559);
        mBankMap.put(0xe02e, R.drawable.emoji_1f55a);
        mBankMap.put(0xe02f, R.drawable.emoji_1f55b);
        mBankMap.put(0xe030, R.drawable.emoji_1f338);
        mBankMap.put(0xe031, R.drawable.emoji_1f531);
        mBankMap.put(0xe032, R.drawable.emoji_1f339);
        mBankMap.put(0xe033, R.drawable.emoji_1f384);
        mBankMap.put(0xe034, R.drawable.emoji_1f48d);
        mBankMap.put(0xe035, R.drawable.emoji_1f48e);
        mBankMap.put(0xe036, R.drawable.emoji_1f3e0);
        mBankMap.put(0xe037, R.drawable.emoji_26ea);
        mBankMap.put(0xe038, R.drawable.emoji_1f3e2);
        mBankMap.put(0xe039, R.drawable.emoji_1f689);
        mBankMap.put(0xe03a, R.drawable.emoji_26fd);
        mBankMap.put(0xe03b, R.drawable.emoji_1f5fb);
        mBankMap.put(0xe03c, R.drawable.emoji_1f3a4);
        mBankMap.put(0xe03d, R.drawable.emoji_1f3a5);
        mBankMap.put(0xe03e, R.drawable.emoji_1f3b5);
        mBankMap.put(0xe03f, R.drawable.emoji_1f511);
        mBankMap.put(0xe040, R.drawable.emoji_1f3b7);
        mBankMap.put(0xe041, R.drawable.emoji_1f3b8);
        mBankMap.put(0xe042, R.drawable.emoji_1f3ba);
        mBankMap.put(0xe043, R.drawable.emoji_1f374);
        mBankMap.put(0xe044, R.drawable.emoji_1f377);
        mBankMap.put(0xe045, R.drawable.emoji_2615);
        mBankMap.put(0xe046, R.drawable.emoji_1f370);
        mBankMap.put(0xe047, R.drawable.emoji_1f37a);
        mBankMap.put(0xe048, R.drawable.emoji_26c4);
        mBankMap.put(0xe049, R.drawable.emoji_2601);
        mBankMap.put(0xe04a, R.drawable.emoji_2600);
        mBankMap.put(0xe04b, R.drawable.emoji_2614);
        mBankMap.put(0xe04c, R.drawable.emoji_1f313);
        mBankMap.put(0xe04d, R.drawable.emoji_1f304);
        mBankMap.put(0xe04e, R.drawable.emoji_1f47c);
        mBankMap.put(0xe04f, R.drawable.emoji_1f431);
        mBankMap.put(0xe050, R.drawable.emoji_1f42f);
        mBankMap.put(0xe051, R.drawable.emoji_1f43b);
        mBankMap.put(0xe052, R.drawable.emoji_1f429);
        mBankMap.put(0xe053, R.drawable.emoji_1f42d);
        mBankMap.put(0xe054, R.drawable.emoji_1f433);
        mBankMap.put(0xe055, R.drawable.emoji_1f427);
        mBankMap.put(0xe056, R.drawable.emoji_1f60a);
        mBankMap.put(0xe057, R.drawable.emoji_1f603);
        mBankMap.put(0xe058, R.drawable.emoji_1f61e);
        mBankMap.put(0xe059, R.drawable.emoji_1f620);
        mBankMap.put(0xe05a, R.drawable.emoji_1f4a9);
        mBankMap.put(0xe101, R.drawable.emoji_1f4ea);
        mBankMap.put(0xe102, R.drawable.emoji_1f4ee);
        mBankMap.put(0xe103, R.drawable.emoji_1f4e7);
        mBankMap.put(0xe104, R.drawable.emoji_1f4f2);
        mBankMap.put(0xe105, R.drawable.emoji_1f61c);
        mBankMap.put(0xe106, R.drawable.emoji_1f60d);
        mBankMap.put(0xe107, R.drawable.emoji_1f631);
        mBankMap.put(0xe108, R.drawable.emoji_1f613);
        mBankMap.put(0xe109, R.drawable.emoji_1f435);
        mBankMap.put(0xe10a, R.drawable.emoji_1f419);
        mBankMap.put(0xe10b, R.drawable.emoji_1f437);
        mBankMap.put(0xe10c, R.drawable.emoji_1f47d);
        mBankMap.put(0xe10d, R.drawable.emoji_1f680);
        mBankMap.put(0xe10e, R.drawable.emoji_1f451);
        mBankMap.put(0xe10f, R.drawable.emoji_1f4a1);
        mBankMap.put(0xe110, R.drawable.emoji_1f331);
        mBankMap.put(0xe111, R.drawable.emoji_1f48f);
        mBankMap.put(0xe112, R.drawable.emoji_1f381);
        mBankMap.put(0xe113, R.drawable.emoji_1f52b);
        mBankMap.put(0xe114, R.drawable.emoji_1f50d);
        mBankMap.put(0xe115, R.drawable.emoji_1f3c3);
        mBankMap.put(0xe116, R.drawable.emoji_1f528);
        mBankMap.put(0xe117, R.drawable.emoji_1f386);
        mBankMap.put(0xe118, R.drawable.emoji_1f341);
        mBankMap.put(0xe119, R.drawable.emoji_1f342);
        mBankMap.put(0xe11a, R.drawable.emoji_1f47f);
        mBankMap.put(0xe11b, R.drawable.emoji_1f47b);
        mBankMap.put(0xe11c, R.drawable.emoji_1f480);
        mBankMap.put(0xe11d, R.drawable.emoji_1f525);
        mBankMap.put(0xe11e, R.drawable.emoji_1f4bc);
        mBankMap.put(0xe11f, R.drawable.emoji_1f4ba);
        mBankMap.put(0xe120, R.drawable.emoji_1f354);
        mBankMap.put(0xe121, R.drawable.emoji_26f2);
        mBankMap.put(0xe122, R.drawable.emoji_26fa);
        mBankMap.put(0xe123, R.drawable.emoji_2668);
        mBankMap.put(0xe124, R.drawable.emoji_1f3a1);
        mBankMap.put(0xe125, R.drawable.emoji_1f3ab);
        mBankMap.put(0xe126, R.drawable.emoji_1f4bf);
        mBankMap.put(0xe127, R.drawable.emoji_1f4c0);
        mBankMap.put(0xe128, R.drawable.emoji_1f4fb);
        mBankMap.put(0xe129, R.drawable.emoji_1f4fc);
        mBankMap.put(0xe12a, R.drawable.emoji_1f4fa);
        mBankMap.put(0xe12b, R.drawable.emoji_1f47e);
        mBankMap.put(0xe12c, R.drawable.emoji_303d);
        mBankMap.put(0xe12d, R.drawable.emoji_1f004);
        mBankMap.put(0xe12e, R.drawable.emoji_1f19a);
        mBankMap.put(0xe12f, R.drawable.emoji_1f4b0);
        mBankMap.put(0xe130, R.drawable.emoji_1f3af);
        mBankMap.put(0xe131, R.drawable.emoji_1f3c6);
        mBankMap.put(0xe132, R.drawable.emoji_1f3c1);
        mBankMap.put(0xe133, R.drawable.emoji_1f3b0);
        mBankMap.put(0xe134, R.drawable.emoji_1f40e);
        mBankMap.put(0xe135, R.drawable.emoji_1f6a4);
        mBankMap.put(0xe136, R.drawable.emoji_1f6b2);
        mBankMap.put(0xe137, R.drawable.emoji_1f6a7);
        mBankMap.put(0xe138, R.drawable.emoji_1f6b9);
        mBankMap.put(0xe139, R.drawable.emoji_1f6ba);
        mBankMap.put(0xe13a, R.drawable.emoji_1f6bc);
        mBankMap.put(0xe13b, R.drawable.emoji_1f489);
        mBankMap.put(0xe13c, R.drawable.emoji_1f4a4);
        mBankMap.put(0xe13d, R.drawable.emoji_26a1);
        mBankMap.put(0xe13e, R.drawable.emoji_1f460);
        mBankMap.put(0xe13f, R.drawable.emoji_1f6c0);
        mBankMap.put(0xe140, R.drawable.emoji_1f6bd);
        mBankMap.put(0xe141, R.drawable.emoji_1f50a);
        mBankMap.put(0xe142, R.drawable.emoji_1f4e2);
        mBankMap.put(0xe143, R.drawable.emoji_1f38c);
        mBankMap.put(0xe144, R.drawable.emoji_1f50f);
        mBankMap.put(0xe145, R.drawable.emoji_1f513);
        mBankMap.put(0xe146, R.drawable.emoji_1f306);
        mBankMap.put(0xe147, R.drawable.emoji_1f373);
        mBankMap.put(0xe148, R.drawable.emoji_1f4c7);
        mBankMap.put(0xe149, R.drawable.emoji_1f4b1);
        mBankMap.put(0xe14a, R.drawable.emoji_1f4b9);
        mBankMap.put(0xe14b, R.drawable.emoji_1f4e1);
        mBankMap.put(0xe14c, R.drawable.emoji_1f4aa);
        mBankMap.put(0xe14d, R.drawable.emoji_1f3e6);
        mBankMap.put(0xe14e, R.drawable.emoji_1f6a5);
        mBankMap.put(0xe14f, R.drawable.emoji_1f17f);
        mBankMap.put(0xe150, R.drawable.emoji_1f68f);
        mBankMap.put(0xe151, R.drawable.emoji_1f6bb);
        mBankMap.put(0xe152, R.drawable.emoji_1f46e);
        mBankMap.put(0xe153, R.drawable.emoji_1f3e3);
        mBankMap.put(0xe154, R.drawable.emoji_1f3e7);
        mBankMap.put(0xe155, R.drawable.emoji_1f3e5);
        mBankMap.put(0xe156, R.drawable.emoji_1f3ea);
        mBankMap.put(0xe157, R.drawable.emoji_1f3eb);
        mBankMap.put(0xe158, R.drawable.emoji_1f3e8);
        mBankMap.put(0xe159, R.drawable.emoji_1f68c);
        mBankMap.put(0xe15a, R.drawable.emoji_1f695);
        mBankMap.put(0xe201, R.drawable.emoji_1f6b6);
        mBankMap.put(0xe202, R.drawable.emoji_1f6a2);
        mBankMap.put(0xe203, R.drawable.emoji_1f201);
        mBankMap.put(0xe204, R.drawable.emoji_1f49f);
        mBankMap.put(0xe205, R.drawable.emoji_2734);
        mBankMap.put(0xe206, R.drawable.emoji_2733);
        mBankMap.put(0xe207, R.drawable.emoji_1f51e);
        mBankMap.put(0xe208, R.drawable.emoji_1f6ad);
        mBankMap.put(0xe209, R.drawable.emoji_1f530);
        mBankMap.put(0xe20a, R.drawable.emoji_267f);
        mBankMap.put(0xe20b, R.drawable.emoji_1f4f6);
        mBankMap.put(0xe20c, R.drawable.emoji_2665);
        mBankMap.put(0xe20d, R.drawable.emoji_2666);
        mBankMap.put(0xe20e, R.drawable.emoji_2660);
        mBankMap.put(0xe20f, R.drawable.emoji_2663);
        mBankMap.put(0xe210, R.drawable.emoji_0023);
        mBankMap.put(0xe211, R.drawable.emoji_27bf);
        mBankMap.put(0xe212, R.drawable.emoji_1f195);
        mBankMap.put(0xe213, R.drawable.emoji_1f199);
        mBankMap.put(0xe214, R.drawable.emoji_1f192);
        mBankMap.put(0xe215, R.drawable.emoji_1f236);
        mBankMap.put(0xe216, R.drawable.emoji_1f21a);
        mBankMap.put(0xe217, R.drawable.emoji_1f237);
        mBankMap.put(0xe218, R.drawable.emoji_1f238);
        mBankMap.put(0xe219, R.drawable.emoji_1f534);
        mBankMap.put(0xe21a, R.drawable.emoji_1f532);
        mBankMap.put(0xe21b, R.drawable.emoji_1f533);
        mBankMap.put(0xe21c, R.drawable.emoji_0031);
        mBankMap.put(0xe21d, R.drawable.emoji_0032);
        mBankMap.put(0xe21e, R.drawable.emoji_0033);
        mBankMap.put(0xe21f, R.drawable.emoji_0034);
        mBankMap.put(0xe220, R.drawable.emoji_0035);
        mBankMap.put(0xe221, R.drawable.emoji_0036);
        mBankMap.put(0xe222, R.drawable.emoji_0037);
        mBankMap.put(0xe223, R.drawable.emoji_0038);
        mBankMap.put(0xe224, R.drawable.emoji_0039);
        mBankMap.put(0xe225, R.drawable.emoji_0030);
        mBankMap.put(0xe226, R.drawable.emoji_1f250);
        mBankMap.put(0xe227, R.drawable.emoji_1f239);
        mBankMap.put(0xe228, R.drawable.emoji_1f202);
        mBankMap.put(0xe229, R.drawable.emoji_1f194);
        mBankMap.put(0xe22a, R.drawable.emoji_1f235);
        mBankMap.put(0xe22b, R.drawable.emoji_1f233);
        mBankMap.put(0xe22c, R.drawable.emoji_1f22f);
        mBankMap.put(0xe22d, R.drawable.emoji_1f23a);
        mBankMap.put(0xe22e, R.drawable.emoji_1f446);
        mBankMap.put(0xe22f, R.drawable.emoji_1f447);
        mBankMap.put(0xe230, R.drawable.emoji_1f448);
        mBankMap.put(0xe231, R.drawable.emoji_1f449);
        mBankMap.put(0xe232, R.drawable.emoji_2b06);
        mBankMap.put(0xe233, R.drawable.emoji_2b07);
        mBankMap.put(0xe234, R.drawable.emoji_27a1);
        mBankMap.put(0xe235, R.drawable.emoji_1f519);
        mBankMap.put(0xe236, R.drawable.emoji_2197);
        mBankMap.put(0xe237, R.drawable.emoji_2196);
        mBankMap.put(0xe238, R.drawable.emoji_2198);
        mBankMap.put(0xe239, R.drawable.emoji_2199);
        mBankMap.put(0xe23a, R.drawable.emoji_25b6);
        mBankMap.put(0xe23b, R.drawable.emoji_25c0);
        mBankMap.put(0xe23c, R.drawable.emoji_23e9);
        mBankMap.put(0xe23d, R.drawable.emoji_23ea);
        mBankMap.put(0xe23e, R.drawable.emoji_1f52e);
        mBankMap.put(0xe23f, R.drawable.emoji_2648);
        mBankMap.put(0xe240, R.drawable.emoji_2649);
        mBankMap.put(0xe241, R.drawable.emoji_264a);
        mBankMap.put(0xe242, R.drawable.emoji_264b);
        mBankMap.put(0xe243, R.drawable.emoji_264c);
        mBankMap.put(0xe244, R.drawable.emoji_264d);
        mBankMap.put(0xe245, R.drawable.emoji_264e);
        mBankMap.put(0xe246, R.drawable.emoji_264f);
        mBankMap.put(0xe247, R.drawable.emoji_2650);
        mBankMap.put(0xe248, R.drawable.emoji_2651);
        mBankMap.put(0xe249, R.drawable.emoji_2652);
        mBankMap.put(0xe24a, R.drawable.emoji_2653);
        mBankMap.put(0xe24b, R.drawable.emoji_26ce);
        mBankMap.put(0xe24c, R.drawable.emoji_1f51d);
        mBankMap.put(0xe24d, R.drawable.emoji_1f197);
        mBankMap.put(0xe24e, R.drawable.emoji_00a9);
        mBankMap.put(0xe24f, R.drawable.emoji_00ae);
        mBankMap.put(0xe250, R.drawable.emoji_1f4f3);
        mBankMap.put(0xe251, R.drawable.emoji_1f4f4);
        mBankMap.put(0xe252, R.drawable.emoji_26a0);
        mBankMap.put(0xe253, R.drawable.emoji_1f481);
        mBankMap.put(0xe301, R.drawable.emoji_1f4c3);
        mBankMap.put(0xe302, R.drawable.emoji_1f454);
        mBankMap.put(0xe303, R.drawable.emoji_1f33a);
        mBankMap.put(0xe304, R.drawable.emoji_1f337);
        mBankMap.put(0xe305, R.drawable.emoji_1f33b);
        mBankMap.put(0xe306, R.drawable.emoji_1f490);
        mBankMap.put(0xe307, R.drawable.emoji_1f334);
        mBankMap.put(0xe308, R.drawable.emoji_1f335);
        mBankMap.put(0xe309, R.drawable.emoji_1f6be);
        mBankMap.put(0xe30a, R.drawable.emoji_1f3a7);
        mBankMap.put(0xe30b, R.drawable.emoji_1f376);
        mBankMap.put(0xe30c, R.drawable.emoji_1f37b);
        mBankMap.put(0xe30d, R.drawable.emoji_3297);
        mBankMap.put(0xe30e, R.drawable.emoji_1f6ac);
        mBankMap.put(0xe30f, R.drawable.emoji_1f48a);
        mBankMap.put(0xe310, R.drawable.emoji_1f388);
        mBankMap.put(0xe311, R.drawable.emoji_1f4a3);
        mBankMap.put(0xe312, R.drawable.emoji_1f389);
        mBankMap.put(0xe313, R.drawable.emoji_2702);
        mBankMap.put(0xe314, R.drawable.emoji_1f380);
        mBankMap.put(0xe315, R.drawable.emoji_3299);
        mBankMap.put(0xe316, R.drawable.emoji_1f4bd);
        mBankMap.put(0xe317, R.drawable.emoji_1f4e3);
        mBankMap.put(0xe318, R.drawable.emoji_1f452);
        mBankMap.put(0xe319, R.drawable.emoji_1f457);
        mBankMap.put(0xe31a, R.drawable.emoji_1f461);
        mBankMap.put(0xe31b, R.drawable.emoji_1f462);
        mBankMap.put(0xe31c, R.drawable.emoji_1f484);
        mBankMap.put(0xe31d, R.drawable.emoji_1f485);
        mBankMap.put(0xe31e, R.drawable.emoji_1f486);
        mBankMap.put(0xe31f, R.drawable.emoji_1f487);
        mBankMap.put(0xe320, R.drawable.emoji_1f488);
        mBankMap.put(0xe321, R.drawable.emoji_1f458);
        mBankMap.put(0xe322, R.drawable.emoji_1f459);
        mBankMap.put(0xe323, R.drawable.emoji_1f45c);
        mBankMap.put(0xe324, R.drawable.emoji_1f3ac);
        mBankMap.put(0xe325, R.drawable.emoji_1f514);
        mBankMap.put(0xe326, R.drawable.emoji_1f3b6);
        mBankMap.put(0xe327, R.drawable.emoji_1f493);
        mBankMap.put(0xe328, R.drawable.emoji_1f48c);
        mBankMap.put(0xe329, R.drawable.emoji_1f498);
        mBankMap.put(0xe32a, R.drawable.emoji_1f499);
        mBankMap.put(0xe32b, R.drawable.emoji_1f49a);
        mBankMap.put(0xe32c, R.drawable.emoji_1f49b);
        mBankMap.put(0xe32d, R.drawable.emoji_1f49c);
        mBankMap.put(0xe32e, R.drawable.emoji_2728);
        mBankMap.put(0xe32f, R.drawable.emoji_2b50);
        mBankMap.put(0xe330, R.drawable.emoji_1f4a8);
        mBankMap.put(0xe331, R.drawable.emoji_1f4a6);
        mBankMap.put(0xe332, R.drawable.emoji_2b55);
        mBankMap.put(0xe333, R.drawable.emoji_2716);
        mBankMap.put(0xe334, R.drawable.emoji_1f4a2);
        mBankMap.put(0xe335, R.drawable.emoji_1f31f);
        mBankMap.put(0xe336, R.drawable.emoji_2754);
        mBankMap.put(0xe337, R.drawable.emoji_2755);
        mBankMap.put(0xe338, R.drawable.emoji_1f375);
        mBankMap.put(0xe339, R.drawable.emoji_1f35e);
        mBankMap.put(0xe33a, R.drawable.emoji_1f366);
        mBankMap.put(0xe33b, R.drawable.emoji_1f35f);
        mBankMap.put(0xe33c, R.drawable.emoji_1f361);
        mBankMap.put(0xe33d, R.drawable.emoji_1f358);
        mBankMap.put(0xe33e, R.drawable.emoji_1f35a);
        mBankMap.put(0xe33f, R.drawable.emoji_1f35d);
        mBankMap.put(0xe340, R.drawable.emoji_1f35c);
        mBankMap.put(0xe341, R.drawable.emoji_1f35b);
        mBankMap.put(0xe342, R.drawable.emoji_1f359);
        mBankMap.put(0xe343, R.drawable.emoji_1f362);
        mBankMap.put(0xe344, R.drawable.emoji_1f363);
        mBankMap.put(0xe345, R.drawable.emoji_1f34e);
        mBankMap.put(0xe346, R.drawable.emoji_1f34a);
        mBankMap.put(0xe347, R.drawable.emoji_1f353);
        mBankMap.put(0xe348, R.drawable.emoji_1f349);
        mBankMap.put(0xe349, R.drawable.emoji_1f345);
        mBankMap.put(0xe34a, R.drawable.emoji_1f346);
        mBankMap.put(0xe34b, R.drawable.emoji_1f382);
        mBankMap.put(0xe34c, R.drawable.emoji_1f371);
        mBankMap.put(0xe34d, R.drawable.emoji_1f372);
        mBankMap.put(0xe401, R.drawable.emoji_1f625);
        mBankMap.put(0xe402, R.drawable.emoji_1f60f);
        mBankMap.put(0xe403, R.drawable.emoji_1f614);
        mBankMap.put(0xe404, R.drawable.emoji_1f601);
        mBankMap.put(0xe405, R.drawable.emoji_1f609);
        mBankMap.put(0xe406, R.drawable.emoji_1f623);
        mBankMap.put(0xe407, R.drawable.emoji_1f616);
        mBankMap.put(0xe408, R.drawable.emoji_1f62a);
        mBankMap.put(0xe409, R.drawable.emoji_1f445);
        mBankMap.put(0xe40a, R.drawable.emoji_1f606);
        mBankMap.put(0xe40b, R.drawable.emoji_1f628);
        mBankMap.put(0xe40c, R.drawable.emoji_1f637);
        mBankMap.put(0xe40d, R.drawable.emoji_1f633);
        mBankMap.put(0xe40e, R.drawable.emoji_1f612);
        mBankMap.put(0xe40f, R.drawable.emoji_1f630);
        mBankMap.put(0xe410, R.drawable.emoji_1f632);
        mBankMap.put(0xe411, R.drawable.emoji_1f62d);
        mBankMap.put(0xe412, R.drawable.emoji_1f602);
        mBankMap.put(0xe413, R.drawable.emoji_1f622);
        mBankMap.put(0xe414, R.drawable.emoji_263a);
        mBankMap.put(0xe415, R.drawable.emoji_1f605);
        mBankMap.put(0xe416, R.drawable.emoji_1f621);
        mBankMap.put(0xe417, R.drawable.emoji_1f61a);
        mBankMap.put(0xe418, R.drawable.emoji_1f618);
        mBankMap.put(0xe419, R.drawable.emoji_1f440);
        mBankMap.put(0xe41a, R.drawable.emoji_1f443);
        mBankMap.put(0xe41b, R.drawable.emoji_1f442);
        mBankMap.put(0xe41c, R.drawable.emoji_1f444);
        mBankMap.put(0xe41d, R.drawable.emoji_1f64f);
        mBankMap.put(0xe41e, R.drawable.emoji_1f44b);
        mBankMap.put(0xe41f, R.drawable.emoji_1f44f);
        mBankMap.put(0xe420, R.drawable.emoji_1f44c);
        mBankMap.put(0xe421, R.drawable.emoji_1f44e);
        mBankMap.put(0xe422, R.drawable.emoji_1f450);
        mBankMap.put(0xe423, R.drawable.emoji_1f645);
        mBankMap.put(0xe424, R.drawable.emoji_1f646);
        mBankMap.put(0xe425, R.drawable.emoji_1f491);
        mBankMap.put(0xe426, R.drawable.emoji_1f647);
        mBankMap.put(0xe427, R.drawable.emoji_1f64c);
        mBankMap.put(0xe428, R.drawable.emoji_1f46b);
        mBankMap.put(0xe429, R.drawable.emoji_1f46f);
        mBankMap.put(0xe42a, R.drawable.emoji_1f3c0);
        mBankMap.put(0xe42b, R.drawable.emoji_1f3c8);
        mBankMap.put(0xe42c, R.drawable.emoji_1f3b1);
        mBankMap.put(0xe42d, R.drawable.emoji_1f3ca);
        mBankMap.put(0xe42e, R.drawable.emoji_1f699);
        mBankMap.put(0xe42f, R.drawable.emoji_1f69a);
        mBankMap.put(0xe430, R.drawable.emoji_1f692);
        mBankMap.put(0xe431, R.drawable.emoji_1f691);
        mBankMap.put(0xe432, R.drawable.emoji_1f693);
        mBankMap.put(0xe433, R.drawable.emoji_1f3a2);
        mBankMap.put(0xe434, R.drawable.emoji_1f687);
        mBankMap.put(0xe435, R.drawable.emoji_1f684);
        mBankMap.put(0xe436, R.drawable.emoji_1f38d);
        mBankMap.put(0xe437, R.drawable.emoji_1f49d);
        mBankMap.put(0xe438, R.drawable.emoji_1f38e);
        mBankMap.put(0xe439, R.drawable.emoji_1f393);
        mBankMap.put(0xe43a, R.drawable.emoji_1f392);
        mBankMap.put(0xe43b, R.drawable.emoji_1f38f);
        mBankMap.put(0xe43c, R.drawable.emoji_1f302);
        mBankMap.put(0xe43d, R.drawable.emoji_1f492);
        mBankMap.put(0xe43e, R.drawable.emoji_1f30a);
        mBankMap.put(0xe43f, R.drawable.emoji_1f367);
        mBankMap.put(0xe440, R.drawable.emoji_1f387);
        mBankMap.put(0xe441, R.drawable.emoji_1f41a);
        mBankMap.put(0xe442, R.drawable.emoji_1f390);
        mBankMap.put(0xe443, R.drawable.emoji_1f300);
        mBankMap.put(0xe444, R.drawable.emoji_1f33e);
        mBankMap.put(0xe445, R.drawable.emoji_1f383);
        mBankMap.put(0xe446, R.drawable.emoji_1f391);
        mBankMap.put(0xe447, R.drawable.emoji_1f343);
        mBankMap.put(0xe448, R.drawable.emoji_1f385);
        mBankMap.put(0xe449, R.drawable.emoji_1f305);
        mBankMap.put(0xe44a, R.drawable.emoji_1f307);
        mBankMap.put(0xe44b, R.drawable.emoji_1f303);
        mBankMap.put(0xe44b, R.drawable.emoji_1f30c);
        mBankMap.put(0xe44c, R.drawable.emoji_1f308);
        mBankMap.put(0xe501, R.drawable.emoji_1f3e9);
        mBankMap.put(0xe502, R.drawable.emoji_1f3a8);
        mBankMap.put(0xe503, R.drawable.emoji_1f3a9);
        mBankMap.put(0xe504, R.drawable.emoji_1f3ec);
        mBankMap.put(0xe505, R.drawable.emoji_1f3ef);
        mBankMap.put(0xe506, R.drawable.emoji_1f3f0);
        mBankMap.put(0xe507, R.drawable.emoji_1f3a6);
        mBankMap.put(0xe508, R.drawable.emoji_1f3ed);
        mBankMap.put(0xe509, R.drawable.emoji_1f5fc);
        mBankMap.put(0xe50b, R.drawable.emoji_1f1ef_1f1f5);
        mBankMap.put(0xe50c, R.drawable.emoji_1f1fa_1f1f8);
        mBankMap.put(0xe50d, R.drawable.emoji_1f1eb_1f1f7);
        mBankMap.put(0xe50e, R.drawable.emoji_1f1e9_1f1ea);
        mBankMap.put(0xe50f, R.drawable.emoji_1f1ee_1f1f9);
        mBankMap.put(0xe510, R.drawable.emoji_1f1ec_1f1e7);
        mBankMap.put(0xe511, R.drawable.emoji_1f1ea_1f1f8);
        mBankMap.put(0xe512, R.drawable.emoji_1f1f7_1f1fa);
        mBankMap.put(0xe513, R.drawable.emoji_1f1e8_1f1f3);
        mBankMap.put(0xe514, R.drawable.emoji_1f1f0_1f1f7);
        mBankMap.put(0xe515, R.drawable.emoji_1f471);
        mBankMap.put(0xe516, R.drawable.emoji_1f472);
        mBankMap.put(0xe517, R.drawable.emoji_1f473);
        mBankMap.put(0xe518, R.drawable.emoji_1f474);
        mBankMap.put(0xe519, R.drawable.emoji_1f475);
        mBankMap.put(0xe51a, R.drawable.emoji_1f476);
        mBankMap.put(0xe51b, R.drawable.emoji_1f477);
        mBankMap.put(0xe51c, R.drawable.emoji_1f478);
        mBankMap.put(0xe51d, R.drawable.emoji_1f5fd);
        mBankMap.put(0xe51e, R.drawable.emoji_1f482);
        mBankMap.put(0xe51f, R.drawable.emoji_1f483);
        mBankMap.put(0xe520, R.drawable.emoji_1f42c);
        mBankMap.put(0xe521, R.drawable.emoji_1f426);
        mBankMap.put(0xe522, R.drawable.emoji_1f420);
        mBankMap.put(0xe523, R.drawable.emoji_1f423);
        mBankMap.put(0xe524, R.drawable.emoji_1f439);
        mBankMap.put(0xe525, R.drawable.emoji_1f41b);
        mBankMap.put(0xe526, R.drawable.emoji_1f418);
        mBankMap.put(0xe527, R.drawable.emoji_1f428);
        mBankMap.put(0xe528, R.drawable.emoji_1f412);
        mBankMap.put(0xe529, R.drawable.emoji_1f411);
        mBankMap.put(0xe52a, R.drawable.emoji_1f43a);
        mBankMap.put(0xe52b, R.drawable.emoji_1f42e);
        mBankMap.put(0xe52c, R.drawable.emoji_1f430);
        mBankMap.put(0xe52d, R.drawable.emoji_1f40d);
        mBankMap.put(0xe52e, R.drawable.emoji_1f414);
        mBankMap.put(0xe52f, R.drawable.emoji_1f417);
        mBankMap.put(0xe530, R.drawable.emoji_1f42b);
        mBankMap.put(0xe531, R.drawable.emoji_1f438);
        mBankMap.put(0xe532, R.drawable.emoji_1f170);
        mBankMap.put(0xe533, R.drawable.emoji_1f171);
        mBankMap.put(0xe534, R.drawable.emoji_1f18e);
        mBankMap.put(0xe535, R.drawable.emoji_1f17e);
        mBankMap.put(0xe536, R.drawable.emoji_1f43e);
        mBankMap.put(0xe537, R.drawable.emoji_2122);
    }

    /**
     * Checks if is soft bank emoji.
     *
     * @param c the c
     * @return true, if is soft bank emoji
     */
    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }

    /**
     * Gets the emoji resource.
     *
     * @param context   the context
     * @param codePoint the code point
     * @return the emoji resource
     */
    private static int getEmojiResource(Context context, int codePoint) {
        return mEmojiMap.get(codePoint);
    }

    /**
     * Gets the softbank emoji resource.
     *
     * @param c the c
     * @return the softbank emoji resource
     */
    private static int getSoftbankEmojiResource(char c) {
        return mBankMap.get(c);
    }

    public static int getSpannableLength(Spannable text) {
        int textLength = text.length();
        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
        ImageSpan[] imageSpans = text.getSpans(0, textLength, ImageSpan.class);
        return oldSpans.length + imageSpans.length;
    }

    public static int getLength(Spannable text) {
        int textLength = text.length();
        return textLength - getSpannableLength(text);
    }

    /**
     * Convert emoji characters of the given Spannable to the according
     * emojicon.
     *
     * @param context   the context
     * @param text      the text
     * @param emojiSize the emoji size
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize) {
        addEmojis(context, text, emojiSize, 0, -1);
    }

    /**
     * Convert emoji characters of the given Spannable to the according
     * emojicon.
     *
     * @param context   the context
     * @param text      the text
     * @param emojiSize the emoji size
     * @param index     the index
     * @param length    the length
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int length) {
        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length + index);

        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }
        int skip;
        for (int i = index; i < textLengthToProcess; i += skip) {
            skip = 0;
            int icon = 0;
            char c = text.charAt(i);
            if (isSoftBankEmoji(c)) {
                icon = getSoftbankEmojiResource(c);
                skip = icon == 0 ? 0 : 1;
            }

            if (icon == 0) {
                int unicode = Character.codePointAt(text, i);
                skip = Character.charCount(unicode);

                if (unicode > 0xff) {
                    icon = getEmojiResource(context, unicode);
                }

                if (icon == 0 && i + skip < textLengthToProcess) {
                    int followUnicode = Character.codePointAt(text, i + skip);
                    if (followUnicode == 0x20e3) {
                        int followSkip = Character.charCount(followUnicode);
                        switch (unicode) {
                            case 0x0031:
                                icon = R.drawable.emoji_0031;
                                break;
                            case 0x0032:
                                icon = R.drawable.emoji_0032;
                                break;
                            case 0x0033:
                                icon = R.drawable.emoji_0033;
                                break;
                            case 0x0034:
                                icon = R.drawable.emoji_0034;
                                break;
                            case 0x0035:
                                icon = R.drawable.emoji_0035;
                                break;
                            case 0x0036:
                                icon = R.drawable.emoji_0036;
                                break;
                            case 0x0037:
                                icon = R.drawable.emoji_0037;
                                break;
                            case 0x0038:
                                icon = R.drawable.emoji_0038;
                                break;
                            case 0x0039:
                                icon = R.drawable.emoji_0039;
                                break;
                            case 0x0030:
                                icon = R.drawable.emoji_0030;
                                break;
                            case 0x0023:
                                icon = R.drawable.emoji_0023;
                                break;
                            default:
                                followSkip = 0;
                                break;
                        }
                        skip += followSkip;
                    } else {
                        int followSkip = Character.charCount(followUnicode);
                        switch (unicode) {
                            case 0x1f1ef:
                                icon = (followUnicode == 0x1f1f5) ? R.drawable.emoji_1f1ef_1f1f5 : 0;
                                break;
                            case 0x1f1fa:
                                icon = (followUnicode == 0x1f1f8) ? R.drawable.emoji_1f1fa_1f1f8 : 0;
                                break;
                            case 0x1f1eb:
                                icon = (followUnicode == 0x1f1f7) ? R.drawable.emoji_1f1eb_1f1f7 : 0;
                                break;
                            case 0x1f1e9:
                                icon = (followUnicode == 0x1f1ea) ? R.drawable.emoji_1f1e9_1f1ea : 0;
                                break;
                            case 0x1f1ee:
                                icon = (followUnicode == 0x1f1f9) ? R.drawable.emoji_1f1ee_1f1f9 : 0;
                                break;
                            case 0x1f1ec:
                                icon = (followUnicode == 0x1f1e7) ? R.drawable.emoji_1f1ec_1f1e7 : 0;
                                break;
                            case 0x1f1ea:
                                icon = (followUnicode == 0x1f1f8) ? R.drawable.emoji_1f1ea_1f1f8 : 0;
                                break;
                            case 0x1f1f7:
                                icon = (followUnicode == 0x1f1fa) ? R.drawable.emoji_1f1f7_1f1fa : 0;
                                break;
                            case 0x1f1e8:
                                icon = (followUnicode == 0x1f1f3) ? R.drawable.emoji_1f1e8_1f1f3 : 0;
                                break;
                            case 0x1f1f0:
                                icon = (followUnicode == 0x1f1f7) ? R.drawable.emoji_1f1f0_1f1f7 : 0;
                                break;
                            default:
                                followSkip = 0;
                                break;
                        }
                        skip += followSkip;
                    }
                }
            }

            if (icon > 0) {
                text.setSpan(new EmojiconSpan(context, icon, emojiSize), i, i + skip,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
