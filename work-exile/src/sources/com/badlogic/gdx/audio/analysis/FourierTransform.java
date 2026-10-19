package com.badlogic.gdx.audio.analysis;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class FourierTransform {
    public static final int HAMMING = 1;
    protected static final int LINAVG = 2;
    protected static final int LOGAVG = 3;
    protected static final int NOAVG = 4;
    public static final int NONE = 0;
    protected static final float TWO_PI = 6.2831855f;
    protected float[] averages;
    protected int avgPerOctave;
    protected float bandWidth;
    protected float[] imag;
    protected int octaves;
    protected float[] real;
    protected int sampleRate;
    protected float[] spectrum;
    protected int timeSize;
    protected int whichAverage;
    protected int whichWindow;

    protected abstract void allocateArrays();

    public abstract void forward(float[] fArr);

    public abstract void inverse(float[] fArr);

    public abstract void scaleBand(int i, float f);

    public abstract void setBand(int i, float f);

    FourierTransform(int ts, float sr) {
        this.timeSize = ts;
        this.sampleRate = (int) sr;
        this.bandWidth = (2.0f / this.timeSize) * (this.sampleRate / 2.0f);
        noAverages();
        allocateArrays();
        this.whichWindow = 0;
    }

    protected void setComplex(float[] r, float[] i) {
        if (this.real.length != r.length && this.imag.length != i.length) {
            throw new IllegalArgumentException("This won't work");
        }
        System.arraycopy(r, 0, this.real, 0, r.length);
        System.arraycopy(i, 0, this.imag, 0, i.length);
    }

    protected void fillSpectrum() {
        float lowFreq;
        for (int i = 0; i < this.spectrum.length; i++) {
            this.spectrum[i] = (float) Math.sqrt((this.real[i] * this.real[i]) + (this.imag[i] * this.imag[i]));
        }
        if (this.whichAverage == 2) {
            int avgWidth = this.spectrum.length / this.averages.length;
            for (int i2 = 0; i2 < this.averages.length; i2++) {
                float avg = 0.0f;
                int j = 0;
                while (j < avgWidth) {
                    int offset = j + (i2 * avgWidth);
                    if (offset >= this.spectrum.length) {
                        break;
                    }
                    avg += this.spectrum[offset];
                    j++;
                }
                this.averages[i2] = avg / (j + 1);
            }
            return;
        }
        if (this.whichAverage == 3) {
            for (int i3 = 0; i3 < this.octaves; i3++) {
                if (i3 == 0) {
                    lowFreq = 0.0f;
                } else {
                    lowFreq = (this.sampleRate / 2) / ((float) Math.pow(2.0d, this.octaves - i3));
                }
                float hiFreq = (this.sampleRate / 2) / ((float) Math.pow(2.0d, (this.octaves - i3) - 1));
                float freqStep = (hiFreq - lowFreq) / this.avgPerOctave;
                float f = lowFreq;
                for (int j2 = 0; j2 < this.avgPerOctave; j2++) {
                    this.averages[j2 + (this.avgPerOctave * i3)] = calcAvg(f, f + freqStep);
                    f += freqStep;
                }
            }
        }
    }

    public void noAverages() {
        this.averages = new float[0];
        this.whichAverage = 4;
    }

    public void linAverages(int numAvg) {
        if (numAvg > this.spectrum.length / 2) {
            throw new IllegalArgumentException("The number of averages for this transform can be at most " + (this.spectrum.length / 2) + ".");
        }
        this.averages = new float[numAvg];
        this.whichAverage = 2;
    }

    public void logAverages(int minBandwidth, int bandsPerOctave) {
        float nyq = this.sampleRate / 2.0f;
        this.octaves = 1;
        while (true) {
            nyq /= 2.0f;
            if (nyq > minBandwidth) {
                this.octaves++;
            } else {
                this.avgPerOctave = bandsPerOctave;
                this.averages = new float[this.octaves * bandsPerOctave];
                this.whichAverage = 3;
                return;
            }
        }
    }

    public void window(int which) {
        if (which < 0 || which > 1) {
            throw new IllegalArgumentException("Invalid window type.");
        }
        this.whichWindow = which;
    }

    protected void doWindow(float[] samples) {
        switch (this.whichWindow) {
            case 1:
                hamming(samples);
                break;
        }
    }

    protected void hamming(float[] samples) {
        for (int i = 0; i < samples.length; i++) {
            samples[i] = (float) (((double) samples[i]) * (0.5400000214576721d - (0.46000000834465027d * Math.cos((TWO_PI * i) / (samples.length - 1)))));
        }
    }

    public int timeSize() {
        return this.timeSize;
    }

    public int specSize() {
        return this.spectrum.length;
    }

    public float getBand(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > this.spectrum.length - 1) {
            i = this.spectrum.length - 1;
        }
        return this.spectrum[i];
    }

    public float getBandWidth() {
        return this.bandWidth;
    }

    public int freqToIndex(float freq) {
        if (freq < getBandWidth() / 2.0f) {
            return 0;
        }
        if (freq > (this.sampleRate / 2) - (getBandWidth() / 2.0f)) {
            return this.spectrum.length - 1;
        }
        float fraction = freq / this.sampleRate;
        return Math.round(this.timeSize * fraction);
    }

    public float indexToFreq(int i) {
        float bw = getBandWidth();
        if (i == 0) {
            return bw * 0.25f;
        }
        if (i == this.spectrum.length - 1) {
            float lastBinBeginFreq = (this.sampleRate / 2) - (bw / 2.0f);
            float binHalfWidth = bw * 0.25f;
            return lastBinBeginFreq + binHalfWidth;
        }
        return i * bw;
    }

    public float getAverageCenterFrequency(int i) {
        float lowFreq;
        if (this.whichAverage == 2) {
            int avgWidth = this.spectrum.length / this.averages.length;
            int centerBinIndex = (i * avgWidth) + (avgWidth / 2);
            return indexToFreq(centerBinIndex);
        }
        if (this.whichAverage == 3) {
            int octave = i / this.avgPerOctave;
            int offset = i % this.avgPerOctave;
            if (octave == 0) {
                lowFreq = 0.0f;
            } else {
                lowFreq = (this.sampleRate / 2) / ((float) Math.pow(2.0d, this.octaves - octave));
            }
            float hiFreq = (this.sampleRate / 2) / ((float) Math.pow(2.0d, (this.octaves - octave) - 1));
            float freqStep = (hiFreq - lowFreq) / this.avgPerOctave;
            float f = lowFreq + (offset * freqStep);
            return (freqStep / 2.0f) + f;
        }
        return 0.0f;
    }

    public float getFreq(float freq) {
        return getBand(freqToIndex(freq));
    }

    public void setFreq(float freq, float a) {
        setBand(freqToIndex(freq), a);
    }

    public void scaleFreq(float freq, float s) {
        scaleBand(freqToIndex(freq), s);
    }

    public int avgSize() {
        return this.averages.length;
    }

    public float getAvg(int i) {
        if (this.averages.length > 0) {
            float ret = this.averages[i];
            return ret;
        }
        return 0.0f;
    }

    public float calcAvg(float lowFreq, float hiFreq) {
        int lowBound = freqToIndex(lowFreq);
        int hiBound = freqToIndex(hiFreq);
        float avg = 0.0f;
        for (int i = lowBound; i <= hiBound; i++) {
            avg += this.spectrum[i];
        }
        return avg / ((hiBound - lowBound) + 1);
    }

    public void forward(float[] buffer, int startAt) {
        if (buffer.length - startAt < this.timeSize) {
            throw new IllegalArgumentException("FourierTransform.forward: not enough samples in the buffer between " + startAt + " and " + buffer.length + " to perform a transform.");
        }
        float[] section = new float[this.timeSize];
        System.arraycopy(buffer, startAt, section, 0, section.length);
        forward(section);
    }

    public void inverse(float[] freqReal, float[] freqImag, float[] buffer) {
        setComplex(freqReal, freqImag);
        inverse(buffer);
    }

    public float[] getSpectrum() {
        return this.spectrum;
    }

    public float[] getRealPart() {
        return this.real;
    }

    public float[] getImaginaryPart() {
        return this.imag;
    }
}
