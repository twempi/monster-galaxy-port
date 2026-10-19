package com.badlogic.gdx.math;

/* JADX INFO: loaded from: classes.dex */
public final class WindowedMean {
    int last_value;
    float[] values;
    int added_values = 0;
    float mean = 0.0f;
    boolean dirty = true;

    public WindowedMean(int window_size) {
        this.values = new float[window_size];
    }

    public boolean hasEnoughData() {
        return this.added_values >= this.values.length;
    }

    public void clear() {
        this.added_values = 0;
        this.last_value = 0;
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = 0.0f;
        }
        this.dirty = true;
    }

    public void addValue(float value) {
        this.added_values++;
        float[] fArr = this.values;
        int i = this.last_value;
        this.last_value = i + 1;
        fArr[i] = value;
        if (this.last_value > this.values.length - 1) {
            this.last_value = 0;
        }
        this.dirty = true;
    }

    public float getMean() {
        if (!hasEnoughData()) {
            return 0.0f;
        }
        if (this.dirty) {
            float mean = 0.0f;
            for (int i = 0; i < this.values.length; i++) {
                mean += this.values[i];
            }
            this.mean = mean / this.values.length;
            this.dirty = false;
        }
        return this.mean;
    }

    public float getOldest() {
        return this.last_value == this.values.length + (-1) ? this.values[0] : this.values[this.last_value + 1];
    }

    public float getLatest() {
        return this.values[this.last_value + (-1) == -1 ? this.values.length - 1 : this.last_value - 1];
    }

    public float standardDeviation() {
        if (!hasEnoughData()) {
            return 0.0f;
        }
        float mean = getMean();
        float sum = 0.0f;
        for (int i = 0; i < this.values.length; i++) {
            sum += (this.values[i] - mean) * (this.values[i] - mean);
        }
        return (float) Math.sqrt(sum / this.values.length);
    }
}
