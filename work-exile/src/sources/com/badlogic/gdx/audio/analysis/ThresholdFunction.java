package com.badlogic.gdx.audio.analysis;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ThresholdFunction {
    private final int historySize;
    private final float multiplier;

    public ThresholdFunction(int historySize, float multiplier) {
        this.historySize = historySize;
        this.multiplier = multiplier;
    }

    public List<Float> calculate(List<Float> spectralFlux) {
        ArrayList<Float> thresholds = new ArrayList<>(spectralFlux.size());
        for (int i = 0; i < spectralFlux.size(); i++) {
            float sum = 0.0f;
            int start = Math.max(0, i - (this.historySize / 2));
            int end = Math.min(spectralFlux.size() - 1, (this.historySize / 2) + i);
            for (int j = start; j <= end; j++) {
                sum += spectralFlux.get(j).floatValue();
            }
            thresholds.add(Float.valueOf((sum / (end - start)) * this.multiplier));
        }
        return thresholds;
    }
}
