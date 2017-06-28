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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>EntrySet</p>
 * <p>Date: 2017/3/1</p>
 *
 * @author afon
 */

public class EntrySet {

    /**
     * Y 轴上 entry 的最大值
     */
    private float maxY;

    /**
     * Y 轴上 entry 的最小值
     */
    private float minY;

    /**
     * Y 轴上 entry 的最大值索引
     */
    private int maxYIndex;

    /**
     * Y 轴上 entry 的最小值索引
     */
    private int minYIndex;

    /**
     * 高亮的 entry 索引
     */
    private int highlightIndex;

    /**
     * entry 列表
     */
    private final List<Entry> entries = new ArrayList<>();

    /**
     * 列表第一个 entry 的昨日收盘价，用于判断当第一个 entry 的收盘价等于开盘价时，
     * 不好判断是涨停还是跌停还是不涨不跌，此值默认设置0，即一律视为涨停
     */
    private float preClose = 0;

    /**
     * 是否在加载中状态
     */
    private boolean loadingStatus = true;

    /**
     * 添加一个 entry 到尾部
     */
    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    /**
     * 添加一组 entry 到尾部
     */
    public void addEntries(List<Entry> entries) {
        this.entries.addAll(entries);
    }

    /**
     * 添加一个 entry 到头部
     */
    public void insertFirst(Entry entry) {
        entries.add(0, entry);
    }

    /**
     * 添加一组 entry 到头部
     */
    public void insertFirst(List<Entry> entries) {
        this.entries.addAll(0, entries);
    }

    public List<Entry> getEntryList() {
        return entries;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getDeltaY() {
        return maxY - minY;
    }

    public int getMinYIndex() {
        return minYIndex;
    }

    public int getMaxYIndex() {
        return maxYIndex;
    }

    public int getHighlightIndex() {
        return highlightIndex;
    }

    public void setHighlightIndex(int highlightIndex) {
        this.highlightIndex = highlightIndex;
    }

    public Entry getHighlightEntry() {
        if (0 < highlightIndex && highlightIndex < entries.size()) {
            return entries.get(highlightIndex);
        }
        return null;
    }

    public float getPreClose() {
        return preClose;
    }

    public void setPreClose(float preClose) {
        this.preClose = preClose;
    }

    public boolean isLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(boolean loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    /**
     * 在给定的范围内，计算分时图 entries 的最大值和最小值
     *
     * @param start
     * @param end
     */
    public void computeTimeLineMinMax(int start, int end) {
        int endValue;
        if (end < 2 || end >= entries.size()) {
            endValue = entries.size() - 1;
        } else {
            endValue = end - 1; // 减去 1 是为了把边缘的 entry 排除
        }

        minY = Float.MAX_VALUE;
        maxY = -Float.MAX_VALUE;

        for (int i = start; i <= endValue; i++) {
            Entry entry = entries.get(i);

            if (entry.getClose() < minY) {
                minY = entry.getClose();
                minYIndex = i;
            }

            if (entry.getClose() > maxY) {
                maxY = entry.getClose();
                maxYIndex = i;
            }
        }
    }

    /**
     * 在给定的范围内，计算 K 线图 entries 的最大值和最小值
     */
    public void computeMinMax(int start, int end, List<StockIndex> stockIndexList) {
        int endValue;
        if (end < 2 || end >= entries.size()) {
            endValue = entries.size() - 1;
        } else {
            endValue = end - 1; // 减去 1 是为了把边缘的 entry 排除
        }

        minY = Float.MAX_VALUE;
        maxY = -Float.MAX_VALUE;

        if (stockIndexList != null) {
            for (StockIndex stockIndex : stockIndexList) {
                if (stockIndex.isEnable()) {
                    stockIndex.resetMinMax();
                }
            }
        }

        for (int i = start; i <= endValue; i++) {
            Entry entry = entries.get(i);

            if (entry.getLow() < minY) {
                minY = entry.getLow();
                minYIndex = i;
            }
            minY = Math.min(minY, entry.getMa5());
            minY = Math.min(minY, entry.getMa10());
            minY = Math.min(minY, entry.getMa20());

            if (entry.getHigh() > maxY) {
                maxY = entry.getHigh();
                maxYIndex = i;
            }
            maxY = Math.max(maxY, entry.getMa5());
            maxY = Math.max(maxY, entry.getMa10());
            maxY = Math.max(maxY, entry.getMa20());

            if (stockIndexList != null) {
                for (StockIndex stockIndex : stockIndexList) {
                    if (stockIndex.isEnable()) {
                        stockIndex.computeMinMax(i, entry);
                    }
                }
            }
        }
    }

    /**
     * 计算 MA MACD BOLL RSI KDJ 指标
     */
    public void computeStockIndex() {
        computeMA();
        computeMACD();
        computeBOLL();
        computeRSI();
        computeKDJ();
    }

    /**
     * 计算 MA
     */
    private void computeMA() {
        float ma5 = 0;
        float ma10 = 0;
        float ma20 = 0;
        float volumeMa5 = 0;
        float volumeMa10 = 0;

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            ma5 += entry.getClose();
            ma10 += entry.getClose();
            ma20 += entry.getClose();

            volumeMa5 += entry.getVolume();
            volumeMa10 += entry.getVolume();

            if (i >= 5) {
                ma5 -= entries.get(i - 5).getClose();
                entry.setMa5(ma5 / 5f);

                volumeMa5 -= entries.get(i - 5).getVolume();
                entry.setVolumeMa5(volumeMa5 / 5f);
            } else {
                entry.setMa5(ma5 / (i + 1f));

                entry.setVolumeMa5(volumeMa5 / (i + 1f));
            }

            if (i >= 10) {
                ma10 -= entries.get(i - 10).getClose();
                entry.setMa10(ma10 / 10f);

                volumeMa10 -= entries.get(i - 10).getVolume();
                entry.setVolumeMa10(volumeMa10 / 5f);
            } else {
                entry.setMa10(ma10 / (i + 1f));

                entry.setVolumeMa10(volumeMa10 / (i + 1f));
            }

            if (i >= 20) {
                ma20 -= entries.get(i - 20).getClose();
                entry.setMa20(ma20 / 20f);
            } else {
                entry.setMa20(ma20 / (i + 1f));
            }
        }
    }

    /**
     * 计算 MACD
     */
    private void computeMACD() {
        float ema12 = 0;
        float ema26 = 0;
        float diff = 0;
        float dea = 0;
        float macd = 0;

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            if (i == 0) {
                ema12 = entry.getClose();
                ema26 = entry.getClose();
            } else {
                // EMA（12） = 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
                // EMA（26） = 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
                ema12 = ema12 * 11f / 13f + entry.getClose() * 2f / 13f;
                ema26 = ema26 * 25f / 27f + entry.getClose() * 2f / 27f;
            }

            // DIF = EMA（12） - EMA（26） 。
            // 今日DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
            // 用（DIF-DEA）*2 即为 MACD 柱状图。
            diff = ema12 - ema26;
            dea = dea * 8f / 10f + diff * 2f / 10f;
            macd = (diff - dea) * 2f;

            entry.setDiff(diff);
            entry.setDea(dea);
            entry.setMacd(macd);
        }
    }

    /**
     * 计算 BOLL 需要在计算 MA 之后进行
     */
    private void computeBOLL() {
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            if (i == 0) {
                entry.setMb(entry.getClose());
                entry.setUp(Float.NaN);
                entry.setDn(Float.NaN);
            } else {
                int n = 20;
                if (i < 20) {
                    n = i + 1;
                }

                float md = 0;
                for (int j = i - n + 1; j <= i; j++) {
                    float c = entries.get(j).getClose();
                    float m = entry.getMa20();
                    float value = c - m;
                    md += value * value;
                }

                md = md / (n - 1);
                md = (float) Math.sqrt(md);

                entry.setMb(entry.getMa20());
                entry.setUp(entry.getMb() + 2f * md);
                entry.setDn(entry.getMb() - 2f * md);
            }
        }
    }

    /**
     * 计算 RSI
     */
    private void computeRSI() {
        float rsi1 = 0;
        float rsi2 = 0;
        float rsi3 = 0;
        float rsi1ABSEma = 0;
        float rsi2ABSEma = 0;
        float rsi3ABSEma = 0;
        float rsi1MaxEma = 0;
        float rsi2MaxEma = 0;
        float rsi3MaxEma = 0;

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            if (i == 0) {
                rsi1 = 0;
                rsi2 = 0;
                rsi3 = 0;
                rsi1ABSEma = 0;
                rsi2ABSEma = 0;
                rsi3ABSEma = 0;
                rsi1MaxEma = 0;
                rsi2MaxEma = 0;
                rsi3MaxEma = 0;
            } else {
                float Rmax = Math.max(0, entry.getClose() - entries.get(i - 1).getClose());
                float RAbs = Math.abs(entry.getClose() - entries.get(i - 1).getClose());

                rsi1MaxEma = (Rmax + (6f - 1) * rsi1MaxEma) / 6f;
                rsi1ABSEma = (RAbs + (6f - 1) * rsi1ABSEma) / 6f;

                rsi2MaxEma = (Rmax + (12f - 1) * rsi2MaxEma) / 12f;
                rsi2ABSEma = (RAbs + (12f - 1) * rsi2ABSEma) / 12f;

                rsi3MaxEma = (Rmax + (24f - 1) * rsi3MaxEma) / 24f;
                rsi3ABSEma = (RAbs + (24f - 1) * rsi3ABSEma) / 24f;

                rsi1 = (rsi1MaxEma / rsi1ABSEma) * 100;
                rsi2 = (rsi2MaxEma / rsi2ABSEma) * 100;
                rsi3 = (rsi3MaxEma / rsi3ABSEma) * 100;
            }

            entry.setRsi1(rsi1);
            entry.setRsi2(rsi2);
            entry.setRsi3(rsi3);
        }
    }

    /**
     * 计算 KDJ
     */
    private void computeKDJ() {
        float k = 0;
        float d = 0;

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            int startIndex = i - 8;
            if (startIndex < 0) {
                startIndex = 0;
            }

            float max9 = Float.MIN_VALUE;
            float min9 = Float.MAX_VALUE;
            for (int index = startIndex; index <= i; index++) {
                max9 = Math.max(max9, entries.get(index).getHigh());
                min9 = Math.min(min9, entries.get(index).getLow());
            }

            float rsv = 100f * (entry.getClose() - min9) / (max9 - min9);
            if (i == 0) {
                k = rsv;
                d = rsv;
            } else {
                k = (rsv + 2f * k) / 3f;
                d = (k + 2f * d) / 3f;
            }

            entry.setK(k);
            entry.setD(d);
            entry.setJ(3f * k - 2 * d);
        }
    }
}
