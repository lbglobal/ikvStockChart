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
 * <p>BOLL 指标</p>
 * <p>Date: 2017/3/16</p>
 *
 * @author afon
 */

public class StockBOLLIndex extends StockIndex {

    public StockBOLLIndex() {
        super(STANDARD_HEIGHT);
    }

    public StockBOLLIndex(int height) {
        super(height);
    }

    @Override
    public void computeMinMax(int currentIndex, Entry entry) {
        if (entry.getMb() < getMinY()) {
            setMinY(entry.getMb());
        }

        if (entry.getMb() > getMaxY()) {
            setMaxY(entry.getMb());
        }

        if (currentIndex > 0) {
            if (entry.getUp() < getMinY()) {
                setMinY(entry.getUp());
            }
            if (entry.getDn() < getMinY()) {
                setMinY(entry.getDn());
            }

            if (entry.getUp() > getMaxY()) {
                setMaxY(entry.getUp());
            }
            if (entry.getDn() > getMaxY()) {
                setMaxY(entry.getDn());
            }
        }
    }
}
