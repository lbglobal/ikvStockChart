### 与轴、网格有关的属性和方法

| XML                  | Java Code           | Comment                     | Since |
| -------------------- | ------------------- | --------------------------- | ----- |
| app:xLabelSize       | setXLabelSize       | X 轴标签字符大小                   | 0.1.0 |
| app:xLabelColor      | setXLabelColor      | X 轴标签字符颜色                   | 0.1.0 |
| app:xLabelViewHeight | setXLabelViewHeight | X 轴 Label 区域的高度             | 0.1.0 |
| app:yLabelSize       | setYLabelSize       | Y 轴标签字符大小                   | 0.1.0 |
| app:yLabelColor      | setYLabelColor      | Y 轴标签字符颜色                   | 0.1.0 |
| app:yLabelAlign      | setYLabelAlign      | Y 轴标签对齐方向 1: left, 2: right | 0.1.2 |
| app:axisSize         | setAxisSize         | 轴线条大小                       | 0.1.0 |
| app:axisColor        | setAxisColor        | 轴线条颜色                       | 0.1.0 |
| app:gridSize         | setGridSize         | 网格线大小                       | 0.1.0 |
| app:gridColor        | setGridColor        | 网格线颜色                       | 0.1.0 |



### 与高亮、MarkerView 有关的属性和方法

| XML                   | Java Code            | Comment             | Since |
| --------------------- | -------------------- | ------------------- | ----- |
| app:highlightSize     | setHighlightSize     | 高亮线条大小              | 0.1.0 |
| app:highlightColor    | setHighlightColor    | 高亮线条颜色              | 0.1.0 |
| app:markerBorderSize  | setMarkerBorderSize  | MarkerView 边框大小     | 0.1.0 |
| app:markerBorderColor | setMarkerBorderColor | MarkerView 边框颜色     | 0.1.0 |
| app:markerTextSize    | setMarkerTextSize    | MarkerView 字符大小     | 0.1.0 |
| app:markerTextColor   | setMarkerTextColor   | MarkerView 字符颜色     | 0.1.0 |
| app:xMarkerAlign      | setXMarkerAlign      | X 轴 MarkerView 对齐方向 | 0.1.3 |
| app:yMarkerAlign      | setYMarkerAlign      | Y 轴 MarkerView 对齐方向 | 0.1.3 |



### 与分时图有关的属性和方法

| XML               | Java Code        | Comment | Since |
| ----------------- | ---------------- | ------- | ----- |
| app:timeLineSize  | setTimeLineSize  | 分时线大小   | 0.1.0 |
| app:timeLineColor | setTimeLineColor | 分时线颜色   | 0.1.0 |



### 与蜡烛图有关的属性和方法

| XML                             | Java Code                      | Comment      | Since |
| ------------------------------- | ------------------------------ | ------------ | ----- |
| app:candleBorderSize            | setCandleBorderSize            | 蜡烛图矩形边框大小    | 0.1.0 |
| app:candleExtremumLabelSize     | setCandleExtremumLabelSize     | 蜡烛图极值字符大小    | 0.1.0 |
| app:candleExtremumLableColor    | setCandleExtremumLableColor    | 蜡烛图极值字符颜色    | 0.1.0 |
| app:shadowSize                  | setShadowSize                  | 影线大小         | 0.1.0 |
| app:increasingColor             | setIncreasingColor             | 上涨颜色         | 0.1.0 |
| app:decreasingColor             | setDecreasingColor             | 下跌颜色         | 0.1.0 |
| app:neutralColor                | setNeutralColor                | 不涨不跌颜色       | 0.1.0 |
| app:portraitDefaultVisibleCount | setPortraitDefaultVisibleCount | 竖屏默认显示多少个蜡烛图 | 0.1.0 |
| app:zoomInTimes                 | setZoomInTimes                 | 最多放大次数       | 0.1.0 |
| app:zoomOutTimes                | setZoomOutTimes                | 最多缩小次数       | 0.1.0 |



### 与股票指标有关的属性和方法

| XML                        | Java Code                 | Comment         | Since |
| -------------------------- | ------------------------- | --------------- | ----- |
| app:maLineSize             | setMaLineSize             | MA 平均线大小        | 0.1.0 |
| app:ma5Color               | setMa5Color               | MA5 平均线颜色       | 0.1.0 |
| app:ma10Color              | setMa10Color              | MA10 平均线颜色      | 0.1.0 |
| app:ma20Color              | setMa20Color              | MA20 平均线颜色      | 0.1.0 |
| app:bollLineSize           | setBollLineSize           | BOLL 线条大小       | 0.1.0 |
| app:bollMidLineColor       | setBollMidLineColor       | BOLL MID 线条颜色   | 0.1.0 |
| app:bollUpperLineColor     | setBollUpperLineColor     | BOLL UPPER 线条颜色 | 0.1.0 |
| app:bollLowerLineColor     | setBollLowerLineColor     | BOLL LOWER 线条颜色 | 0.1.0 |
| app:kdjLineSize            | setKdjLineSize            | KDJ 线条大小        | 0.1.0 |
| app:kdjKLineColor          | setKdjKLineColor          | KDJ K 线条颜色      | 0.1.0 |
| app:kdjDLineColor          | setKdjDLineColor          | KDJ D 线条颜色      | 0.1.0 |
| app:kdjJLineColor          | setKdjJLineColor          | KDJ J 线条颜色      | 0.1.0 |
| app:macdLineSize           | setMacdLineSize           | MACD 两条线大小      | 0.1.0 |
| app:macdHighlightTextColor | setMacdHighlightTextColor | 高亮的 MACD 字符颜色   | 0.1.0 |
| app:deaLineColor           | setDeaLineColor           | DEA 线条颜色        | 0.1.0 |
| app:diffLineColor          | setDiffLineColor          | DIFF 线条颜色       | 0.1.0 |
| app:rsiLineSize            | setRsiLineSize            | RSI 线条大小        | 0.1.0 |
| app:rsi1LineColor          | setRsi1LineColor          | RSI 第一条线颜色      | 0.1.0 |
| app:rsi2LineColor          | setRsi2LineColor          | RSI 第二条线颜色      | 0.1.0 |
| app:rsi3LineColor          | setRsi3LineColor          | RSI 第三条线颜色      | 0.1.0 |
| app:maTextSize             | setMaTextSize             | MA 字符大小         | 0.1.0 |
| app:maTextColor            | setMaTextColor            | MA 字符颜色         | 0.1.0 |
| app:bollTextSize           | setBollTextSize           | BOLL 字符大小       | 0.1.0 |
| app:bollTextColor          | setBollTextColor          | BOLL 字符颜色       | 0.1.0 |
| app:kdjTextSize            | setKdjTextSize            | KDJ 字符大小        | 0.1.0 |
| app:kdjTextColor           | setKdjTextColor           | KDJ 字符颜色        | 0.1.0 |
| app:macdTextSize           | setMacdTextSize           | MACD 字符大小       | 0.1.0 |
| app:macdTextColor          | setMacdTextColor          | MACD 字符颜色       | 0.1.0 |
| app:rsiTextSize            | setRsiTextSize            | RSI 字符大小        | 0.1.0 |
| app:rsiTextColor           | setRsiTextColor           | RSI 字符颜色        | 0.1.0 |



### 其它

| XML                  | Java Code           | Comment      | Since |
| -------------------- | ------------------- | ------------ | ----- |
| app:loadingTextSize  | setLoadingTextSize  | loading 字符大小 | 0.1.0 |
| app:loadingTextColor | setLoadingTextColor | loading 字符颜色 | 0.1.0 |
| app:loadingText      | setLoadingText      | loading 字符   | 0.1.0 |
| app:errorTextSize    | setErrorTextSize    | error 字符大小   | 0.1.0 |
| app:errorTextColor   | setErrorTextColor   | error 字符颜色   | 0.1.0 |
| app:errorText        | setErrorText        | error 字符     | 0.1.0 |