package com.badlogic.gdx.audio.analysis;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DFT extends FourierTransform {
    private float[] coslookup;
    private float[] sinlookup;

    public DFT(int timeSize, float sampleRate) {
        super(timeSize, sampleRate);
        if (timeSize % 2 != 0) {
            throw new IllegalArgumentException("DFT: timeSize must be even.");
        }
        buildTrigTables();
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    protected void allocateArrays() {
        this.spectrum = new float[(this.timeSize / 2) + 1];
        this.real = new float[(this.timeSize / 2) + 1];
        this.imag = new float[(this.timeSize / 2) + 1];
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void scaleBand(int i, float s) {
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void setBand(int i, float a) {
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void forward(float[] samples) {
        if (samples.length != this.timeSize) {
            throw new IllegalArgumentException("DFT.forward: The length of the passed sample buffer must be equal to DFT.timeSize().");
        }
        doWindow(samples);
        int N = samples.length;
        for (int f = 0; f <= N / 2; f++) {
            this.real[f] = 0.0f;
            this.imag[f] = 0.0f;
            for (int t = 0; t < N; t++) {
                float[] fArr = this.real;
                fArr[f] = fArr[f] + (samples[t] * cos(t * f));
                float[] fArr2 = this.imag;
                fArr2[f] = fArr2[f] + (samples[t] * (-sin(t * f)));
            }
        }
        fillSpectrum();
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void inverse(float[] buffer) {
        int N = buffer.length;
        float[] fArr = this.real;
        fArr[0] = fArr[0] / N;
        this.imag[0] = (-this.imag[0]) / (N / 2);
        float[] fArr2 = this.real;
        int i = N / 2;
        fArr2[i] = fArr2[i] / N;
        this.imag[N / 2] = (-this.imag[0]) / (N / 2);
        for (int i2 = 0; i2 < N / 2; i2++) {
            float[] fArr3 = this.real;
            fArr3[i2] = fArr3[i2] / (N / 2);
            this.imag[i2] = (-this.imag[i2]) / (N / 2);
        }
        for (int t = 0; t < N; t++) {
            buffer[t] = 0.0f;
            for (int f = 0; f < N / 2; f++) {
                buffer[t] = buffer[t] + (this.real[f] * cos(t * f)) + (this.imag[f] * sin(t * f));
            }
        }
    }

    private void buildTrigTables() {
        int N = this.spectrum.length * this.timeSize;
        this.sinlookup = new float[N];
        this.coslookup = new float[N];
        for (int i = 0; i < N; i++) {
            this.sinlookup[i] = (float) Math.sin((i * 6.2831855f) / this.timeSize);
            this.coslookup[i] = (float) Math.cos((i * 6.2831855f) / this.timeSize);
        }
    }

    private float sin(int i) {
        return this.sinlookup[i];
    }

    private float cos(int i) {
        return this.coslookup[i];
    }
}
