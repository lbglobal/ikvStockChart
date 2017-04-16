/*
 * Copyright (C) 2017 WordPlat Open Source Project
 *
 *      https://wordplat.com/InteractiveKLineView/
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
 */

package com.wordplat.ikvstockchart.entry;

/**
 * <p>MACD 指标</p>
 * <p>Date: 2017/3/16</p>
 *
 * @author afon
 */

public class StockMACDIndex extends StockIndex {

    public StockMACDIndex() {
        super(STANDARD_HEIGHT);
    }

    public StockMACDIndex(int height) {
        super(height);
    }

    @Override
    public void computeMinMax(int currentIndex, Entry entry) {
        if (entry.getMacd() < getMinY()) {
            setMinY(entry.getMacd());
        }
        if (entry.getDea() < getMinY()) {
            setMinY(entry.getDea());
        }
        if (entry.getDiff() < getMinY()) {
            setMinY(entry.getDiff());
        }

        if (entry.getMacd() > getMaxY()) {
            setMaxY(entry.getMacd());
        }
        if (entry.getDea() > getMaxY()) {
            setMaxY(entry.getDea());
        }
        if (entry.getDiff() > getMaxY()) {
            setMaxY(entry.getDiff());
        }
    }
}
