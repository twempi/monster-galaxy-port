package com.badlogic.gdx.audio.analysis;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FFT extends FourierTransform {
    private float[] coslookup;
    private int[] reverse;
    private float[] sinlookup;

    public FFT(int timeSize, float sampleRate) {
        super(timeSize, sampleRate);
        if (((timeSize - 1) & timeSize) != 0) {
            throw new IllegalArgumentException("FFT: timeSize must be a power of two.");
        }
        buildReverseTable();
        buildTrigTables();
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    protected void allocateArrays() {
        this.spectrum = new float[(this.timeSize / 2) + 1];
        this.real = new float[this.timeSize];
        this.imag = new float[this.timeSize];
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void scaleBand(int i, float s) {
        if (s < 0.0f) {
            throw new IllegalArgumentException("Can't scale a frequency band by a negative value.");
        }
        if (this.spectrum[i] != 0.0f) {
            float[] fArr = this.real;
            fArr[i] = fArr[i] / this.spectrum[i];
            float[] fArr2 = this.imag;
            fArr2[i] = fArr2[i] / this.spectrum[i];
            float[] fArr3 = this.spectrum;
            fArr3[i] = fArr3[i] * s;
            float[] fArr4 = this.real;
            fArr4[i] = fArr4[i] * this.spectrum[i];
            float[] fArr5 = this.imag;
            fArr5[i] = fArr5[i] * this.spectrum[i];
        }
        if (i != 0 && i != this.timeSize / 2) {
            this.real[this.timeSize - i] = this.real[i];
            this.imag[this.timeSize - i] = -this.imag[i];
        }
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void setBand(int i, float a) {
        if (a < 0.0f) {
            throw new IllegalArgumentException("Can't set a frequency band to a negative value.");
        }
        if (this.real[i] == 0.0f && this.imag[i] == 0.0f) {
            this.real[i] = a;
            this.spectrum[i] = a;
        } else {
            float[] fArr = this.real;
            fArr[i] = fArr[i] / this.spectrum[i];
            float[] fArr2 = this.imag;
            fArr2[i] = fArr2[i] / this.spectrum[i];
            this.spectrum[i] = a;
            float[] fArr3 = this.real;
            fArr3[i] = fArr3[i] * this.spectrum[i];
            float[] fArr4 = this.imag;
            fArr4[i] = fArr4[i] * this.spectrum[i];
        }
        if (i != 0 && i != this.timeSize / 2) {
            this.real[this.timeSize - i] = this.real[i];
            this.imag[this.timeSize - i] = -this.imag[i];
        }
    }

    private void fft() {
        for (int halfSize = 1; halfSize < this.real.length; halfSize *= 2) {
            float phaseShiftStepR = cos(halfSize);
            float phaseShiftStepI = sin(halfSize);
            float currentPhaseShiftR = 1.0f;
            float currentPhaseShiftI = 0.0f;
            for (int fftStep = 0; fftStep < halfSize; fftStep++) {
                for (int i = fftStep; i < this.real.length; i += halfSize * 2) {
                    int off = i + halfSize;
                    float tr = (this.real[off] * currentPhaseShiftR) - (this.imag[off] * currentPhaseShiftI);
                    float ti = (this.imag[off] * currentPhaseShiftR) + (this.real[off] * currentPhaseShiftI);
                    this.real[off] = this.real[i] - tr;
                    this.imag[off] = this.imag[i] - ti;
                    float[] fArr = this.real;
                    fArr[i] = fArr[i] + tr;
                    float[] fArr2 = this.imag;
                    fArr2[i] = fArr2[i] + ti;
                }
                float tmpR = currentPhaseShiftR;
                currentPhaseShiftR = (tmpR * phaseShiftStepR) - (currentPhaseShiftI * phaseShiftStepI);
                currentPhaseShiftI = (tmpR * phaseShiftStepI) + (currentPhaseShiftI * phaseShiftStepR);
            }
        }
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void forward(float[] buffer) {
        if (buffer.length != this.timeSize) {
            throw new IllegalArgumentException("FFT.forward: The length of the passed sample buffer must be equal to timeSize().");
        }
        doWindow(buffer);
        bitReverseSamples(buffer);
        fft();
        fillSpectrum();
    }

    public void forward(float[] buffReal, float[] buffImag) {
        if (buffReal.length != this.timeSize || buffImag.length != this.timeSize) {
            throw new IllegalArgumentException("FFT.forward: The length of the passed buffers must be equal to timeSize().");
        }
        setComplex(buffReal, buffImag);
        bitReverseComplex();
        fft();
        fillSpectrum();
    }

    @Override // com.badlogic.gdx.audio.analysis.FourierTransform
    public void inverse(float[] buffer) {
        if (buffer.length > this.real.length) {
            throw new IllegalArgumentException("FFT.inverse: the passed array's length must equal FFT.timeSize().");
        }
        for (int i = 0; i < this.timeSize; i++) {
            float[] fArr = this.imag;
            fArr[i] = fArr[i] * (-1.0f);
        }
        bitReverseComplex();
        fft();
        for (int i2 = 0; i2 < buffer.length; i2++) {
            buffer[i2] = this.real[i2] / this.real.length;
        }
    }

    private void buildReverseTable() {
        int N = this.timeSize;
        this.reverse = new int[N];
        this.reverse[0] = 0;
        int limit = 1;
        int bit = N / 2;
        while (limit < N) {
            for (int i = 0; i < limit; i++) {
                this.reverse[i + limit] = this.reverse[i] + bit;
            }
            limit <<= 1;
            bit >>= 1;
        }
    }

    private void bitReverseSamples(float[] samples) {
        for (int i = 0; i < samples.length; i++) {
            this.real[i] = samples[this.reverse[i]];
            this.imag[i] = 0.0f;
        }
    }

    private void bitReverseComplex() {
        float[] revReal = new float[this.real.length];
        float[] revImag = new float[this.imag.length];
        for (int i = 0; i < this.real.length; i++) {
            revReal[i] = this.real[this.reverse[i]];
            revImag[i] = this.imag[this.reverse[i]];
        }
        this.real = revReal;
        this.imag = revImag;
    }

    private float sin(int i) {
        return this.sinlookup[i];
    }

    private float cos(int i) {
        return this.coslookup[i];
    }

    private void buildTrigTables() {
        int N = this.timeSize;
        this.sinlookup = new float[N];
        this.coslookup = new float[N];
        for (int i = 0; i < N; i++) {
            this.sinlookup[i] = (float) Math.sin((-3.1415927f) / i);
            this.coslookup[i] = (float) Math.cos((-3.1415927f) / i);
        }
    }
}
