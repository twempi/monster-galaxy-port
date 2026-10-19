package com.gaiaonline.monstergalaxy.battle.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import java.util.BitSet;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CCParticleEmitter {
    private static final int MODE_GRAVITY = 0;
    private static final float SCALE_FACTOR = ResolutionManager.getScaleFactor();
    private boolean active;
    private BitSet activeBitSet;
    private float angle;
    private float angleVar;
    private float duration;
    private float emitCounter;
    private float[] endColor;
    private float[] endColorVar;
    private float finishSize;
    private float finishSizeVar;
    private boolean flipX;
    private boolean flipY;
    private String imagePath;
    private float life;
    private float lifeVar;
    private String name;
    private int particleCount;
    private Particle[] particles;
    private Vector2 posVar;
    private Sprite sprite;
    private float[] startColor;
    private float[] startColorVar;
    private float startSize;
    private float startSizeVar;
    private Color tint;
    private int totalParticles;
    private float x;
    private float y;
    private ModeA modeA = new ModeA();
    private int emitterMode = 0;
    private float elapsed = 0.0f;
    private boolean additive = true;
    private Vector2 tmp = new Vector2();
    private Vector2 radial = new Vector2();
    private Vector2 tangential = new Vector2();

    public CCParticleEmitter() {
        initialize();
    }

    private void initialize() {
        this.active = true;
        this.emitCounter = 0.0f;
    }

    public void setMaxParticleCount(int maxParticleCount) {
        this.totalParticles = maxParticleCount;
        this.activeBitSet = new BitSet(maxParticleCount);
        this.particleCount = 0;
        this.particles = new Particle[maxParticleCount];
    }

    private void addParticle() {
        if (this.particleCount < this.totalParticles) {
            BitSet active = this.activeBitSet;
            int index = active.nextClearBit(0);
            if (this.totalParticles > index) {
                activateParticle(index);
                active.set(index);
            }
            this.particleCount++;
        }
    }

    public void draw(SpriteBatch spriteBatch, float delta) {
        if (this.additive) {
            spriteBatch.setBlendFunction(770, 1);
        }
        Particle[] particles = this.particles;
        int activeCount = this.particleCount;
        int index = 0;
        while (true) {
            int index2 = this.activeBitSet.nextSetBit(index);
            if (index2 == -1) {
                break;
            }
            if (updateParticle(index2, delta)) {
                particles[index2].draw(spriteBatch);
            } else {
                this.activeBitSet.clear(index2);
                activeCount--;
            }
            index = index2 + 1;
        }
        this.particleCount = activeCount;
        if (this.additive) {
            spriteBatch.setBlendFunction(770, 771);
        }
        if (this.active && this.totalParticles != 0) {
            float rate = this.life / this.totalParticles;
            this.emitCounter += delta;
            while (this.particleCount < this.totalParticles && this.emitCounter > rate) {
                addParticle();
                this.emitCounter -= rate;
            }
            this.elapsed += delta;
            if (this.duration != -1.0f && this.duration < this.elapsed) {
                this.active = false;
            }
        }
    }

    public void start() {
        if (this.particles == null) {
            this.particles = new Particle[this.totalParticles];
        }
        this.elapsed = 0.0f;
        this.active = true;
    }

    private void activateParticle(int index) {
        if (this.particles != null && index < this.particles.length) {
            Particle particle = this.particles[index];
            if (particle == null) {
                Particle[] particleArr = this.particles;
                particle = new Particle(this.sprite);
                particleArr[index] = particle;
                particle.flip(this.flipX, this.flipY);
            }
            particle.timeToLive = Math.max(0.0f, this.life + (this.lifeVar * randomOneMinusOne()));
            particle.setPosition(this.x + (this.posVar.x * randomOneMinusOne() * SCALE_FACTOR), this.y + (this.posVar.y * randomOneMinusOne() * SCALE_FACTOR));
            float startS = Math.max(0.0f, this.startSize + (this.startSizeVar * randomOneMinusOne())) * SCALE_FACTOR;
            particle.size = startS;
            if (this.finishSize == this.startSize) {
                particle.deltaSize = 0.0f;
            } else {
                float endS = this.finishSize + (this.finishSizeVar * randomOneMinusOne());
                particle.deltaSize = (Math.max(0.0f, endS * SCALE_FACTOR) - startS) / particle.timeToLive;
            }
            particle.setBounds(particle.getX() - (particle.size / 2.0f), particle.getY() - (particle.size / 2.0f), particle.size, particle.size);
            particle.startPos.x = this.x;
            particle.startPos.y = this.y;
            float[] startC = new float[4];
            float[] endC = new float[4];
            if (this.tint != null) {
                startC[0] = this.tint.r;
                startC[1] = this.tint.g;
                startC[2] = this.tint.b;
                startC[3] = this.tint.a;
            } else {
                for (int i = 0; i < 4; i++) {
                    startC[i] = Math.min(1.0f, Math.max(0.0f, this.startColor[i] + (this.startColorVar[i] * randomOneMinusOne())));
                    endC[i] = Math.min(1.0f, Math.max(0.0f, this.endColor[i] + (this.endColorVar[i] * randomOneMinusOne())));
                    particle.deltaColor[i] = (endC[i] - startC[i]) / particle.timeToLive;
                }
            }
            particle.setColor(startC);
            float a = (float) Math.toRadians(this.angle + (this.angleVar * randomOneMinusOne()));
            if (this.emitterMode == 0) {
                float s = this.modeA.speed + (this.modeA.speedVar * randomOneMinusOne());
                if (particle.modeA == null) {
                    particle.modeA = new ParticleModeA(null);
                }
                particle.modeA.dir.set((float) Math.cos(a), (float) Math.sin(a));
                particle.modeA.dir.mul(s);
                particle.modeA.radialAccel = this.modeA.radialAccel + (this.modeA.radialAccelVar * randomOneMinusOne());
                particle.modeA.tangentialAccel = this.modeA.tangentialAccel + (this.modeA.tangentialAccelVar * randomOneMinusOne());
            }
        }
    }

    private boolean updateParticle(int index, float delta) {
        boolean retValue = false;
        if (this.particles != null && index < this.particles.length) {
            Particle particle = this.particles[index];
            if (particle == null) {
                return false;
            }
            particle.timeToLive -= delta;
            if (particle.timeToLive < 0.0f) {
                return false;
            }
            this.radial.set(0.0f, 0.0f);
            Vector2 pos = particle.getPos();
            if (pos.x != 0.0f || pos.y != 0.0f) {
                normalize(pos, this.radial);
            }
            this.tangential.set(this.radial);
            this.radial.mul(particle.modeA.radialAccel);
            float newy = this.tangential.x;
            this.tangential.x = -this.tangential.y;
            this.tangential.y = newy;
            this.tangential.mul(particle.modeA.tangentialAccel);
            this.tmp.set(this.radial);
            this.tmp.add(this.tangential);
            this.tmp.add(this.modeA.gravity);
            this.tmp.mul(delta);
            particle.modeA.dir.add(this.tmp);
            this.tmp.set(particle.modeA.dir);
            this.tmp.mul(delta);
            this.tmp.mul(SCALE_FACTOR);
            particle.translate(this.tmp.x, this.tmp.y);
            if (particle.deltaSize != 0.0f) {
                particle.size += particle.deltaSize * delta;
                particle.size = Math.max(0.0f, particle.size);
                particle.setBounds(particle.getX(), particle.getY(), particle.size, particle.size);
            }
            particle.translateColor(delta);
            retValue = true;
        }
        return retValue;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        if (sprite != null) {
            float originX = sprite.getOriginX();
            float originY = sprite.getOriginY();
            Texture texture = sprite.getTexture();
            int n = this.particles.length;
            for (int i = 0; i < n; i++) {
                Particle particle = this.particles[i];
                if (particle != null) {
                    particle.setTexture(texture);
                    particle.setOrigin(originX, originY);
                } else {
                    return;
                }
            }
        }
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isContinuous() {
        return this.duration == -1.0f;
    }

    public boolean isAdditive() {
        return this.additive;
    }

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    public boolean isComplete() {
        boolean value = this.particleCount == 0 && !this.active;
        if (value && this.particles != null) {
            if (this.particles != null) {
                for (int i = 0; i < this.particles.length; i++) {
                    this.particles[i] = null;
                }
            }
            this.particles = null;
        }
        return value;
    }

    public int getActiveCount() {
        return this.particleCount;
    }

    public int getDrawCount() {
        return this.activeBitSet.length();
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setFlip(boolean flipX, boolean flipY) {
        this.flipX = flipX;
        this.flipY = flipY;
        if (this.particles != null) {
            int n = this.particles.length;
            for (int i = 0; i < n; i++) {
                Particle particle = this.particles[i];
                if (particle != null) {
                    particle.flip(flipX, flipY);
                }
            }
        }
    }

    private static float randomOneMinusOne() {
        return MathUtils.random(-1.0f, 1.0f);
    }

    private static void normalize(Vector2 src, Vector2 dst) {
        float invLen = 1.0f / src.len();
        dst.set(src.x * invLen, src.y * invLen);
    }

    public void setLife(float life) {
        this.life = life;
    }

    public void setLifeVar(float lifeVar) {
        this.lifeVar = lifeVar;
    }

    public void setPosVar(Vector2 pv) {
        this.posVar = new Vector2(pv.x, pv.y);
    }

    public void setStartSize(float size) {
        this.startSize = size;
    }

    public void setStartSizeVar(float size) {
        this.startSizeVar = size;
    }

    public void setFinishSize(float size) {
        this.finishSize = size;
    }

    public void setFinishSizeVar(float size) {
        this.finishSizeVar = size;
    }

    public void setStartColor(float[] color) {
        this.startColor = color;
    }

    public void setFinishColor(float[] color) {
        this.endColor = color;
    }

    public void setStartColorVar(float[] color) {
        this.startColorVar = color;
    }

    public void setFinishColorVar(float[] color) {
        this.endColorVar = color;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setGravity(Vector2 g) {
        this.modeA.gravity.set(g);
    }

    public void setSpeed(float speed) {
        this.modeA.speed = speed;
    }

    public void setSpeedVar(float speedVar) {
        this.modeA.speedVar = speedVar;
    }

    public float getSpeedVar() {
        return this.modeA.speedVar;
    }

    public void setAngle(float a) {
        this.angle = a;
    }

    public void setAngleVar(float av) {
        this.angleVar = av;
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

    public void removeParticles() {
        this.particles = null;
    }

    private static class ParticleModeA {
        Vector2 dir;
        float radialAccel;
        float tangentialAccel;

        private ParticleModeA() {
            this.dir = new Vector2();
        }

        /* synthetic */ ParticleModeA(ParticleModeA particleModeA) {
            this();
        }
    }

    static class Particle extends Sprite {
        float angle;
        float angleCos;
        float angleDiff;
        float angleSin;
        float[] color;
        int currentLife;
        float[] deltaColor;
        float deltaRotation;
        float deltaSize;
        float gravity;
        float gravityDiff;
        int life;
        ParticleModeA modeA;
        Vector2 relativePos;
        float rotation;
        float rotationDiff;
        float scale;
        float scaleDiff;
        float size;
        Vector2 startPos;
        float timeToLive;
        float[] tint;
        float transparency;
        float transparencyDiff;
        float velocity;
        float velocityDiff;
        float wind;
        float windDiff;

        public Particle(Sprite sprite) {
            super(sprite);
            this.deltaColor = new float[4];
            this.startPos = new Vector2();
            this.relativePos = new Vector2();
        }

        public Vector2 getPos() {
            this.relativePos.x = (getX() + (getWidth() / 2.0f)) - this.startPos.x;
            this.relativePos.y = (getY() + (getHeight() / 2.0f)) - this.startPos.y;
            return this.relativePos;
        }

        public void setColor(float[] color) {
            this.color = color;
            setColor(color[0], color[1], color[2], color[3]);
        }

        public void translateColor(float delta) {
            for (int i = 0; i < 4; i++) {
                float[] fArr = this.color;
                fArr[i] = fArr[i] + (this.deltaColor[i] * delta);
            }
            setColor(this.color[0], this.color[1], this.color[2], this.color[3]);
        }
    }

    class ModeA {
        Vector2 gravity = new Vector2(0.0f, 0.0f);
        float radialAccel;
        float radialAccelVar;
        float speed;
        float speedVar;
        float tangentialAccel;
        float tangentialAccelVar;

        ModeA() {
        }
    }

    class ModeB {
        float endRadius;
        float endRadiusVar;
        float rotatePerSecond;
        float rotatePerSecondVar;
        float startRadius;
        float startRadiusVar;

        ModeB() {
        }
    }
}
