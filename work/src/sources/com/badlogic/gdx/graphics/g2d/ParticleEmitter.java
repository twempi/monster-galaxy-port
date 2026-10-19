package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: classes.dex */
public class ParticleEmitter {
    private static final int UPDATE_ANGLE = 2;
    private static final int UPDATE_GRAVITY = 32;
    private static final int UPDATE_ROTATION = 4;
    private static final int UPDATE_SCALE = 1;
    private static final int UPDATE_TINT = 64;
    private static final int UPDATE_VELOCITY = 8;
    private static final int UPDATE_WIND = 16;
    private float accumulator;
    private boolean[] active;
    private int activeCount;
    private boolean additive;
    private boolean aligned;
    private boolean allowCompletion;
    private ScaledNumericValue angleValue;
    private boolean attached;
    private boolean behind;
    private boolean continuous;
    private float delay;
    private float delayTimer;
    private RangedNumericValue delayValue;
    public float duration;
    public float durationTimer;
    private RangedNumericValue durationValue;
    private int emission;
    private int emissionDelta;
    private int emissionDiff;
    private ScaledNumericValue emissionValue;
    private boolean firstUpdate;
    private boolean flipX;
    private boolean flipY;
    private ScaledNumericValue gravityValue;
    private String imagePath;
    private int life;
    private int lifeDiff;
    private int lifeOffset;
    private int lifeOffsetDiff;
    private ScaledNumericValue lifeOffsetValue;
    private ScaledNumericValue lifeValue;
    private int maxParticleCount;
    private int minParticleCount;
    private String name;
    private Particle[] particles;
    private ScaledNumericValue rotationValue;
    private ScaledNumericValue scaleValue;
    private float spawnHeight;
    private float spawnHeightDiff;
    private ScaledNumericValue spawnHeightValue;
    private SpawnShapeValue spawnShapeValue;
    private float spawnWidth;
    private float spawnWidthDiff;
    private ScaledNumericValue spawnWidthValue;
    private Sprite sprite;
    private GradientColorValue tintValue;
    private ScaledNumericValue transparencyValue;
    private int updateFlags;
    private ScaledNumericValue velocityValue;
    private ScaledNumericValue windValue;
    private float x;
    private RangedNumericValue xOffsetValue;
    private float y;
    private RangedNumericValue yOffsetValue;

    public enum SpawnEllipseSide {
        both,
        top,
        bottom
    }

    public enum SpawnShape {
        point,
        line,
        square,
        ellipse
    }

    public ParticleEmitter() {
        this.delayValue = new RangedNumericValue();
        this.lifeOffsetValue = new ScaledNumericValue();
        this.durationValue = new RangedNumericValue();
        this.lifeValue = new ScaledNumericValue();
        this.emissionValue = new ScaledNumericValue();
        this.scaleValue = new ScaledNumericValue();
        this.rotationValue = new ScaledNumericValue();
        this.velocityValue = new ScaledNumericValue();
        this.angleValue = new ScaledNumericValue();
        this.windValue = new ScaledNumericValue();
        this.gravityValue = new ScaledNumericValue();
        this.transparencyValue = new ScaledNumericValue();
        this.tintValue = new GradientColorValue();
        this.xOffsetValue = new ScaledNumericValue();
        this.yOffsetValue = new ScaledNumericValue();
        this.spawnWidthValue = new ScaledNumericValue();
        this.spawnHeightValue = new ScaledNumericValue();
        this.spawnShapeValue = new SpawnShapeValue();
        this.maxParticleCount = 4;
        this.duration = 1.0f;
        this.additive = true;
        initialize();
    }

    public ParticleEmitter(BufferedReader reader) throws IOException {
        this.delayValue = new RangedNumericValue();
        this.lifeOffsetValue = new ScaledNumericValue();
        this.durationValue = new RangedNumericValue();
        this.lifeValue = new ScaledNumericValue();
        this.emissionValue = new ScaledNumericValue();
        this.scaleValue = new ScaledNumericValue();
        this.rotationValue = new ScaledNumericValue();
        this.velocityValue = new ScaledNumericValue();
        this.angleValue = new ScaledNumericValue();
        this.windValue = new ScaledNumericValue();
        this.gravityValue = new ScaledNumericValue();
        this.transparencyValue = new ScaledNumericValue();
        this.tintValue = new GradientColorValue();
        this.xOffsetValue = new ScaledNumericValue();
        this.yOffsetValue = new ScaledNumericValue();
        this.spawnWidthValue = new ScaledNumericValue();
        this.spawnHeightValue = new ScaledNumericValue();
        this.spawnShapeValue = new SpawnShapeValue();
        this.maxParticleCount = 4;
        this.duration = 1.0f;
        this.additive = true;
        initialize();
        load(reader);
    }

    public ParticleEmitter(ParticleEmitter emitter) {
        this.delayValue = new RangedNumericValue();
        this.lifeOffsetValue = new ScaledNumericValue();
        this.durationValue = new RangedNumericValue();
        this.lifeValue = new ScaledNumericValue();
        this.emissionValue = new ScaledNumericValue();
        this.scaleValue = new ScaledNumericValue();
        this.rotationValue = new ScaledNumericValue();
        this.velocityValue = new ScaledNumericValue();
        this.angleValue = new ScaledNumericValue();
        this.windValue = new ScaledNumericValue();
        this.gravityValue = new ScaledNumericValue();
        this.transparencyValue = new ScaledNumericValue();
        this.tintValue = new GradientColorValue();
        this.xOffsetValue = new ScaledNumericValue();
        this.yOffsetValue = new ScaledNumericValue();
        this.spawnWidthValue = new ScaledNumericValue();
        this.spawnHeightValue = new ScaledNumericValue();
        this.spawnShapeValue = new SpawnShapeValue();
        this.maxParticleCount = 4;
        this.duration = 1.0f;
        this.additive = true;
        this.sprite = emitter.sprite;
        this.name = emitter.name;
        setMaxParticleCount(emitter.maxParticleCount);
        this.minParticleCount = emitter.minParticleCount;
        this.delayValue.load(emitter.delayValue);
        this.durationValue.load(emitter.durationValue);
        this.emissionValue.load(emitter.emissionValue);
        this.lifeValue.load(emitter.lifeValue);
        this.lifeOffsetValue.load(emitter.lifeOffsetValue);
        this.scaleValue.load(emitter.scaleValue);
        this.rotationValue.load(emitter.rotationValue);
        this.velocityValue.load(emitter.velocityValue);
        this.angleValue.load(emitter.angleValue);
        this.windValue.load(emitter.windValue);
        this.gravityValue.load(emitter.gravityValue);
        this.transparencyValue.load(emitter.transparencyValue);
        this.tintValue.load(emitter.tintValue);
        this.xOffsetValue.load(emitter.xOffsetValue);
        this.yOffsetValue.load(emitter.yOffsetValue);
        this.spawnWidthValue.load(emitter.spawnWidthValue);
        this.spawnHeightValue.load(emitter.spawnHeightValue);
        this.spawnShapeValue.load(emitter.spawnShapeValue);
        this.attached = emitter.attached;
        this.continuous = emitter.continuous;
        this.aligned = emitter.aligned;
        this.behind = emitter.behind;
        this.additive = emitter.additive;
    }

    private void initialize() {
        this.durationValue.setAlwaysActive(true);
        this.emissionValue.setAlwaysActive(true);
        this.lifeValue.setAlwaysActive(true);
        this.scaleValue.setAlwaysActive(true);
        this.transparencyValue.setAlwaysActive(true);
        this.spawnShapeValue.setAlwaysActive(true);
        this.spawnWidthValue.setAlwaysActive(true);
        this.spawnHeightValue.setAlwaysActive(true);
    }

    public void setMaxParticleCount(int maxParticleCount) {
        this.maxParticleCount = maxParticleCount;
        this.active = new boolean[maxParticleCount];
        this.activeCount = 0;
        this.particles = new Particle[maxParticleCount];
    }

    public void addParticle() {
        int activeCount = this.activeCount;
        if (activeCount != this.maxParticleCount) {
            boolean[] active = this.active;
            int n = active.length;
            for (int i = 0; i < n; i++) {
                if (!active[i]) {
                    activateParticle(i);
                    active[i] = true;
                    this.activeCount = activeCount + 1;
                    return;
                }
            }
        }
    }

    public void addParticles(int count) {
        int count2 = Math.min(count, this.maxParticleCount - this.activeCount);
        if (count2 != 0) {
            boolean[] active = this.active;
            int index = 0;
            int n = active.length;
            loop0: for (int i = 0; i < count2; i++) {
                int index2 = index;
                while (true) {
                    if (index2 >= n) {
                        break loop0;
                    } else if (!active[index2]) {
                        break;
                    } else {
                        index2++;
                    }
                }
                activateParticle(index2);
                index = index2 + 1;
                active[index2] = true;
            }
            this.activeCount += count2;
        }
    }

    public void update(float delta) {
        this.accumulator += Math.min(delta * 1000.0f, 250.0f);
        if (this.accumulator >= 1.0f) {
            int deltaMillis = (int) this.accumulator;
            this.accumulator -= deltaMillis;
            boolean[] active = this.active;
            int activeCount = this.activeCount;
            int n = active.length;
            for (int i = 0; i < n; i++) {
                if (active[i] && !updateParticle(this.particles[i], delta, deltaMillis)) {
                    active[i] = false;
                    activeCount--;
                }
            }
            this.activeCount = activeCount;
            if (this.delayTimer < this.delay) {
                this.delayTimer += deltaMillis;
                return;
            }
            if (this.firstUpdate) {
                this.firstUpdate = false;
                addParticle();
            }
            if (this.durationTimer < this.duration) {
                this.durationTimer += deltaMillis;
            } else if (this.continuous && !this.allowCompletion) {
                restart();
            } else {
                return;
            }
            this.emissionDelta += deltaMillis;
            float emissionTime = this.emission + (this.emissionDiff * this.emissionValue.getScale(this.durationTimer / this.duration));
            if (emissionTime > 0.0f) {
                float emissionTime2 = 1000.0f / emissionTime;
                if (this.emissionDelta >= emissionTime2) {
                    int emitCount = Math.min((int) (this.emissionDelta / emissionTime2), this.maxParticleCount - activeCount);
                    this.emissionDelta = (int) (this.emissionDelta - (emitCount * emissionTime2));
                    this.emissionDelta = (int) (this.emissionDelta % emissionTime2);
                    addParticles(emitCount);
                }
            }
            if (activeCount < this.minParticleCount) {
                addParticles(this.minParticleCount - activeCount);
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        if (this.additive) {
            spriteBatch.setBlendFunction(770, 1);
        }
        Particle[] particles = this.particles;
        boolean[] active = this.active;
        int activeCount = this.activeCount;
        int n = active.length;
        for (int i = 0; i < n; i++) {
            if (active[i]) {
                particles[i].draw(spriteBatch);
            }
        }
        this.activeCount = activeCount;
        if (this.additive) {
            spriteBatch.setBlendFunction(770, 771);
        }
    }

    public void draw(SpriteBatch spriteBatch, float delta) {
        this.accumulator += Math.min(1000.0f * delta, 250.0f);
        if (this.accumulator < 1.0f) {
            draw(spriteBatch);
            return;
        }
        int deltaMillis = (int) this.accumulator;
        this.accumulator -= deltaMillis;
        if (this.additive) {
            spriteBatch.setBlendFunction(770, 1);
        }
        Particle[] particles = this.particles;
        boolean[] active = this.active;
        int activeCount = this.activeCount;
        int n = active.length;
        for (int i = 0; i < n; i++) {
            if (active[i]) {
                Particle particle = particles[i];
                if (updateParticle(particle, delta, deltaMillis)) {
                    particle.draw(spriteBatch);
                } else {
                    active[i] = false;
                    activeCount--;
                }
            }
        }
        this.activeCount = activeCount;
        if (this.additive) {
            spriteBatch.setBlendFunction(770, 771);
        }
        if (this.delayTimer < this.delay) {
            this.delayTimer += deltaMillis;
            return;
        }
        if (this.firstUpdate) {
            this.firstUpdate = false;
            addParticle();
        }
        if (this.durationTimer < this.duration) {
            this.durationTimer += deltaMillis;
        } else if (this.continuous && !this.allowCompletion) {
            restart();
        } else {
            return;
        }
        this.emissionDelta += deltaMillis;
        float emissionTime = this.emission + (this.emissionDiff * this.emissionValue.getScale(this.durationTimer / this.duration));
        if (emissionTime > 0.0f) {
            float emissionTime2 = 1000.0f / emissionTime;
            if (this.emissionDelta >= emissionTime2) {
                int emitCount = Math.min((int) (this.emissionDelta / emissionTime2), this.maxParticleCount - activeCount);
                this.emissionDelta = (int) (this.emissionDelta - (emitCount * emissionTime2));
                this.emissionDelta = (int) (this.emissionDelta % emissionTime2);
                addParticles(emitCount);
            }
        }
        if (activeCount < this.minParticleCount) {
            addParticles(this.minParticleCount - activeCount);
        }
    }

    public void start() {
        this.firstUpdate = true;
        this.allowCompletion = false;
        restart();
    }

    public void reset() {
        this.emissionDelta = 0;
        this.durationTimer = this.duration;
        start();
    }

    private void restart() {
        this.delay = this.delayValue.active ? this.delayValue.newLowValue() : 0.0f;
        this.delayTimer = 0.0f;
        this.durationTimer -= this.duration;
        this.duration = this.durationValue.newLowValue();
        this.emission = (int) this.emissionValue.newLowValue();
        this.emissionDiff = (int) this.emissionValue.newHighValue();
        if (!this.emissionValue.isRelative()) {
            this.emissionDiff -= this.emission;
        }
        this.life = (int) this.lifeValue.newLowValue();
        this.lifeDiff = (int) this.lifeValue.newHighValue();
        if (!this.lifeValue.isRelative()) {
            this.lifeDiff -= this.life;
        }
        this.lifeOffset = this.lifeOffsetValue.active ? (int) this.lifeOffsetValue.newLowValue() : 0;
        this.lifeOffsetDiff = (int) this.lifeOffsetValue.newHighValue();
        if (!this.lifeOffsetValue.isRelative()) {
            this.lifeOffsetDiff -= this.lifeOffset;
        }
        this.spawnWidth = this.spawnWidthValue.newLowValue();
        this.spawnWidthDiff = this.spawnWidthValue.newHighValue();
        if (!this.spawnWidthValue.isRelative()) {
            this.spawnWidthDiff -= this.spawnWidth;
        }
        this.spawnHeight = this.spawnHeightValue.newLowValue();
        this.spawnHeightDiff = this.spawnHeightValue.newHighValue();
        if (!this.spawnHeightValue.isRelative()) {
            this.spawnHeightDiff -= this.spawnHeight;
        }
        this.updateFlags = 0;
        if (this.angleValue.active && this.angleValue.timeline.length > 1) {
            this.updateFlags |= 2;
        }
        if (this.velocityValue.active && this.velocityValue.active) {
            this.updateFlags |= 8;
        }
        if (this.scaleValue.timeline.length > 1) {
            this.updateFlags |= 1;
        }
        if (this.rotationValue.active && this.rotationValue.timeline.length > 1) {
            this.updateFlags |= 4;
        }
        if (this.windValue.active) {
            this.updateFlags |= 16;
        }
        if (this.gravityValue.active) {
            this.updateFlags |= 32;
        }
        if (this.tintValue.timeline.length > 1) {
            this.updateFlags |= 64;
        }
    }

    protected Particle newParticle(Sprite sprite) {
        return new Particle(sprite);
    }

    private void activateParticle(int index) {
        float px;
        float py;
        float spawnAngle;
        Particle particle = this.particles[index];
        if (particle == null) {
            Particle[] particleArr = this.particles;
            particle = newParticle(this.sprite);
            particleArr[index] = particle;
            particle.flip(this.flipX, this.flipY);
        }
        float percent = this.durationTimer / this.duration;
        int updateFlags = this.updateFlags;
        int scale = this.life + ((int) (this.lifeDiff * this.lifeValue.getScale(percent)));
        particle.life = scale;
        particle.currentLife = scale;
        if (this.velocityValue.active) {
            particle.velocity = this.velocityValue.newLowValue();
            particle.velocityDiff = this.velocityValue.newHighValue();
            if (!this.velocityValue.isRelative()) {
                particle.velocityDiff -= particle.velocity;
            }
        }
        particle.angle = this.angleValue.newLowValue();
        particle.angleDiff = this.angleValue.newHighValue();
        if (!this.angleValue.isRelative()) {
            particle.angleDiff -= particle.angle;
        }
        float angle = 0.0f;
        if ((updateFlags & 2) == 0) {
            angle = particle.angle + (particle.angleDiff * this.angleValue.getScale(0.0f));
            particle.angle = angle;
            particle.angleCos = MathUtils.cosDeg(angle);
            particle.angleSin = MathUtils.sinDeg(angle);
        }
        float spriteWidth = this.sprite.getWidth();
        particle.scale = this.scaleValue.newLowValue() / spriteWidth;
        particle.scaleDiff = this.scaleValue.newHighValue() / spriteWidth;
        if (!this.scaleValue.isRelative()) {
            particle.scaleDiff -= particle.scale;
        }
        particle.setScale(particle.scale + (particle.scaleDiff * this.scaleValue.getScale(0.0f)));
        if (this.rotationValue.active) {
            particle.rotation = this.rotationValue.newLowValue();
            particle.rotationDiff = this.rotationValue.newHighValue();
            if (!this.rotationValue.isRelative()) {
                particle.rotationDiff -= particle.rotation;
            }
            float rotation = particle.rotation + (particle.rotationDiff * this.rotationValue.getScale(0.0f));
            if (this.aligned) {
                rotation += angle;
            }
            particle.setRotation(rotation);
        }
        if (this.windValue.active) {
            particle.wind = this.windValue.newLowValue();
            particle.windDiff = this.windValue.newHighValue();
            if (!this.windValue.isRelative()) {
                particle.windDiff -= particle.wind;
            }
        }
        if (this.gravityValue.active) {
            particle.gravity = this.gravityValue.newLowValue();
            particle.gravityDiff = this.gravityValue.newHighValue();
            if (!this.gravityValue.isRelative()) {
                particle.gravityDiff -= particle.gravity;
            }
        }
        float[] color = particle.tint;
        if (color == null) {
            color = new float[3];
            particle.tint = color;
        }
        float[] temp = this.tintValue.getColor(0.0f);
        color[0] = temp[0];
        color[1] = temp[1];
        color[2] = temp[2];
        particle.transparency = this.transparencyValue.newLowValue();
        particle.transparencyDiff = this.transparencyValue.newHighValue() - particle.transparency;
        float x = this.x;
        if (this.xOffsetValue.active) {
            x += this.xOffsetValue.newLowValue();
        }
        float y = this.y;
        if (this.yOffsetValue.active) {
            y += this.yOffsetValue.newLowValue();
        }
        switch (this.spawnShapeValue.shape) {
            case square:
                float width = this.spawnWidth + (this.spawnWidthDiff * this.spawnWidthValue.getScale(percent));
                float height = this.spawnHeight + (this.spawnHeightDiff * this.spawnHeightValue.getScale(percent));
                x += MathUtils.random(width) - (width / 2.0f);
                y += MathUtils.random(height) - (height / 2.0f);
                break;
            case ellipse:
                float width2 = this.spawnWidth + (this.spawnWidthDiff * this.spawnWidthValue.getScale(percent));
                float radiusX = width2 / 2.0f;
                float radiusY = (this.spawnHeight + (this.spawnHeightDiff * this.spawnHeightValue.getScale(percent))) / 2.0f;
                if (radiusX != 0.0f && radiusY != 0.0f) {
                    float scaleY = radiusX / radiusY;
                    if (this.spawnShapeValue.edges) {
                        switch (this.spawnShapeValue.side) {
                            case top:
                                spawnAngle = -MathUtils.random(179.0f);
                                break;
                            case bottom:
                                spawnAngle = MathUtils.random(179.0f);
                                break;
                            default:
                                spawnAngle = MathUtils.random(360.0f);
                                break;
                        }
                        float cosDeg = MathUtils.cosDeg(spawnAngle);
                        float sinDeg = MathUtils.sinDeg(spawnAngle);
                        x += cosDeg * radiusX;
                        y += (sinDeg * radiusX) / scaleY;
                        if ((updateFlags & 2) == 0) {
                            particle.angle = spawnAngle;
                            particle.angleCos = cosDeg;
                            particle.angleSin = sinDeg;
                        }
                    } else {
                        float radius2 = radiusX * radiusX;
                        do {
                            px = MathUtils.random(width2) - radiusX;
                            py = MathUtils.random(width2) - radiusX;
                        } while ((px * px) + (py * py) > radius2);
                        x += px;
                        y += py / scaleY;
                    }
                }
                break;
            case line:
                float width3 = this.spawnWidth + (this.spawnWidthDiff * this.spawnWidthValue.getScale(percent));
                float height2 = this.spawnHeight + (this.spawnHeightDiff * this.spawnHeightValue.getScale(percent));
                if (width3 != 0.0f) {
                    float lineX = width3 * MathUtils.random();
                    x += lineX;
                    y += (height2 / width3) * lineX;
                } else {
                    y += MathUtils.random() * height2;
                }
                break;
        }
        float spriteHeight = this.sprite.getHeight();
        particle.setBounds(x - (spriteWidth / 2.0f), y - (spriteHeight / 2.0f), spriteWidth, spriteHeight);
        int offsetTime = (int) (this.lifeOffset + (this.lifeOffsetDiff * this.lifeOffsetValue.getScale(percent)));
        if (offsetTime > 0) {
            if (offsetTime >= particle.currentLife) {
                offsetTime = particle.currentLife - 1;
            }
            updateParticle(particle, offsetTime / 1000.0f, offsetTime);
        }
    }

    private boolean updateParticle(Particle particle, float delta, int deltaMillis) {
        float[] color;
        float velocityX;
        float velocityY;
        int life = particle.currentLife - deltaMillis;
        if (life <= 0) {
            return false;
        }
        particle.currentLife = life;
        float percent = 1.0f - (particle.currentLife / particle.life);
        int updateFlags = this.updateFlags;
        if ((updateFlags & 1) != 0) {
            particle.setScale(particle.scale + (particle.scaleDiff * this.scaleValue.getScale(percent)));
        }
        if ((updateFlags & 8) != 0) {
            float velocity = (particle.velocity + (particle.velocityDiff * this.velocityValue.getScale(percent))) * delta;
            if ((updateFlags & 2) != 0) {
                float angle = particle.angle + (particle.angleDiff * this.angleValue.getScale(percent));
                velocityX = velocity * MathUtils.cosDeg(angle);
                velocityY = velocity * MathUtils.sinDeg(angle);
                if ((updateFlags & 4) != 0) {
                    float rotation = particle.rotation + (particle.rotationDiff * this.rotationValue.getScale(percent));
                    if (this.aligned) {
                        rotation += angle;
                    }
                    particle.setRotation(rotation);
                }
            } else {
                velocityX = velocity * particle.angleCos;
                velocityY = velocity * particle.angleSin;
                if (this.aligned || (updateFlags & 4) != 0) {
                    float rotation2 = particle.rotation + (particle.rotationDiff * this.rotationValue.getScale(percent));
                    if (this.aligned) {
                        rotation2 += particle.angle;
                    }
                    particle.setRotation(rotation2);
                }
            }
            if ((updateFlags & 16) != 0) {
                velocityX += (particle.wind + (particle.windDiff * this.windValue.getScale(percent))) * delta;
            }
            if ((updateFlags & 32) != 0) {
                velocityY += (particle.gravity + (particle.gravityDiff * this.gravityValue.getScale(percent))) * delta;
            }
            particle.translate(velocityX, velocityY);
        } else if ((updateFlags & 4) != 0) {
            particle.setRotation(particle.rotation + (particle.rotationDiff * this.rotationValue.getScale(percent)));
        }
        if ((updateFlags & 64) != 0) {
            color = this.tintValue.getColor(percent);
        } else {
            color = particle.tint;
        }
        particle.setColor(color[0], color[1], color[2], particle.transparency + (particle.transparencyDiff * this.transparencyValue.getScale(percent)));
        return true;
    }

    public void setPosition(float x, float y) {
        if (this.attached) {
            float xAmount = x - this.x;
            float yAmount = y - this.y;
            boolean[] active = this.active;
            int n = active.length;
            for (int i = 0; i < n; i++) {
                if (active[i]) {
                    this.particles[i].translate(xAmount, yAmount);
                }
            }
        }
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

    public void allowCompletion() {
        this.allowCompletion = true;
        this.durationTimer = this.duration;
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

    public ScaledNumericValue getLife() {
        return this.lifeValue;
    }

    public ScaledNumericValue getScale() {
        return this.scaleValue;
    }

    public ScaledNumericValue getRotation() {
        return this.rotationValue;
    }

    public GradientColorValue getTint() {
        return this.tintValue;
    }

    public ScaledNumericValue getVelocity() {
        return this.velocityValue;
    }

    public ScaledNumericValue getWind() {
        return this.windValue;
    }

    public ScaledNumericValue getGravity() {
        return this.gravityValue;
    }

    public ScaledNumericValue getAngle() {
        return this.angleValue;
    }

    public ScaledNumericValue getEmission() {
        return this.emissionValue;
    }

    public ScaledNumericValue getTransparency() {
        return this.transparencyValue;
    }

    public RangedNumericValue getDuration() {
        return this.durationValue;
    }

    public RangedNumericValue getDelay() {
        return this.delayValue;
    }

    public ScaledNumericValue getLifeOffset() {
        return this.lifeOffsetValue;
    }

    public RangedNumericValue getXOffsetValue() {
        return this.xOffsetValue;
    }

    public RangedNumericValue getYOffsetValue() {
        return this.yOffsetValue;
    }

    public ScaledNumericValue getSpawnWidth() {
        return this.spawnWidthValue;
    }

    public ScaledNumericValue getSpawnHeight() {
        return this.spawnHeightValue;
    }

    public SpawnShapeValue getSpawnShape() {
        return this.spawnShapeValue;
    }

    public boolean isAttached() {
        return this.attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public boolean isContinuous() {
        return this.continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public boolean isAligned() {
        return this.aligned;
    }

    public void setAligned(boolean aligned) {
        this.aligned = aligned;
    }

    public boolean isAdditive() {
        return this.additive;
    }

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    public boolean isBehind() {
        return this.behind;
    }

    public void setBehind(boolean behind) {
        this.behind = behind;
    }

    public int getMinParticleCount() {
        return this.minParticleCount;
    }

    public void setMinParticleCount(int minParticleCount) {
        this.minParticleCount = minParticleCount;
    }

    public int getMaxParticleCount() {
        return this.maxParticleCount;
    }

    public boolean isComplete() {
        return this.delayTimer >= this.delay && this.durationTimer >= this.duration && this.activeCount == 0;
    }

    public float getPercentComplete() {
        if (this.delayTimer < this.delay) {
            return 0.0f;
        }
        return Math.min(1.0f, this.durationTimer / this.duration);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getActiveCount() {
        return this.activeCount;
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

    public void flipY() {
        this.angleValue.setHigh(-this.angleValue.getHighMin(), -this.angleValue.getHighMax());
        this.angleValue.setLow(-this.angleValue.getLowMin(), -this.angleValue.getLowMax());
        this.gravityValue.setHigh(-this.gravityValue.getHighMin(), -this.gravityValue.getHighMax());
        this.gravityValue.setLow(-this.gravityValue.getLowMin(), -this.gravityValue.getLowMax());
        this.windValue.setHigh(-this.windValue.getHighMin(), -this.windValue.getHighMax());
        this.windValue.setLow(-this.windValue.getLowMin(), -this.windValue.getLowMax());
        this.rotationValue.setHigh(-this.rotationValue.getHighMin(), -this.rotationValue.getHighMax());
        this.rotationValue.setLow(-this.rotationValue.getLowMin(), -this.rotationValue.getLowMax());
        this.yOffsetValue.setLow(-this.yOffsetValue.getLowMin(), -this.yOffsetValue.getLowMax());
    }

    public void save(Writer output) throws IOException {
        output.write(this.name + "\n");
        output.write("- Delay -\n");
        this.delayValue.save(output);
        output.write("- Duration - \n");
        this.durationValue.save(output);
        output.write("- Count - \n");
        output.write("min: " + this.minParticleCount + "\n");
        output.write("max: " + this.maxParticleCount + "\n");
        output.write("- Emission - \n");
        this.emissionValue.save(output);
        output.write("- Life - \n");
        this.lifeValue.save(output);
        output.write("- Life Offset - \n");
        this.lifeOffsetValue.save(output);
        output.write("- X Offset - \n");
        this.xOffsetValue.save(output);
        output.write("- Y Offset - \n");
        this.yOffsetValue.save(output);
        output.write("- Spawn Shape - \n");
        this.spawnShapeValue.save(output);
        output.write("- Spawn Width - \n");
        this.spawnWidthValue.save(output);
        output.write("- Spawn Height - \n");
        this.spawnHeightValue.save(output);
        output.write("- Scale - \n");
        this.scaleValue.save(output);
        output.write("- Velocity - \n");
        this.velocityValue.save(output);
        output.write("- Angle - \n");
        this.angleValue.save(output);
        output.write("- Rotation - \n");
        this.rotationValue.save(output);
        output.write("- Wind - \n");
        this.windValue.save(output);
        output.write("- Gravity - \n");
        this.gravityValue.save(output);
        output.write("- Tint - \n");
        this.tintValue.save(output);
        output.write("- Transparency - \n");
        this.transparencyValue.save(output);
        output.write("- Options - \n");
        output.write("attached: " + this.attached + "\n");
        output.write("continuous: " + this.continuous + "\n");
        output.write("aligned: " + this.aligned + "\n");
        output.write("additive: " + this.additive + "\n");
        output.write("behind: " + this.behind + "\n");
    }

    public void load(BufferedReader reader) throws IOException {
        try {
            this.name = readString(reader, "name");
            reader.readLine();
            this.delayValue.load(reader);
            reader.readLine();
            this.durationValue.load(reader);
            reader.readLine();
            setMinParticleCount(readInt(reader, "minParticleCount"));
            setMaxParticleCount(readInt(reader, "maxParticleCount"));
            reader.readLine();
            this.emissionValue.load(reader);
            reader.readLine();
            this.lifeValue.load(reader);
            reader.readLine();
            this.lifeOffsetValue.load(reader);
            reader.readLine();
            this.xOffsetValue.load(reader);
            reader.readLine();
            this.yOffsetValue.load(reader);
            reader.readLine();
            this.spawnShapeValue.load(reader);
            reader.readLine();
            this.spawnWidthValue.load(reader);
            reader.readLine();
            this.spawnHeightValue.load(reader);
            reader.readLine();
            this.scaleValue.load(reader);
            reader.readLine();
            this.velocityValue.load(reader);
            reader.readLine();
            this.angleValue.load(reader);
            reader.readLine();
            this.rotationValue.load(reader);
            reader.readLine();
            this.windValue.load(reader);
            reader.readLine();
            this.gravityValue.load(reader);
            reader.readLine();
            this.tintValue.load(reader);
            reader.readLine();
            this.transparencyValue.load(reader);
            reader.readLine();
            this.attached = readBoolean(reader, "attached");
            this.continuous = readBoolean(reader, "continuous");
            this.aligned = readBoolean(reader, "aligned");
            this.additive = readBoolean(reader, "additive");
            this.behind = readBoolean(reader, "behind");
        } catch (RuntimeException ex) {
            if (this.name != null) {
                throw new RuntimeException("Error parsing emitter: " + this.name, ex);
            }
            throw ex;
        }
    }

    static String readString(BufferedReader reader, String name) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Missing value: " + name);
        }
        return line.substring(line.indexOf(":") + 1).trim();
    }

    static boolean readBoolean(BufferedReader reader, String name) throws IOException {
        return Boolean.parseBoolean(readString(reader, name));
    }

    static int readInt(BufferedReader reader, String name) throws IOException {
        return Integer.parseInt(readString(reader, name));
    }

    static float readFloat(BufferedReader reader, String name) throws IOException {
        return Float.parseFloat(readString(reader, name));
    }

    static class Particle extends Sprite {
        float angle;
        float angleCos;
        float angleDiff;
        float angleSin;
        int currentLife;
        float gravity;
        float gravityDiff;
        int life;
        float rotation;
        float rotationDiff;
        float scale;
        float scaleDiff;
        float[] tint;
        float transparency;
        float transparencyDiff;
        float velocity;
        float velocityDiff;
        float wind;
        float windDiff;

        public Particle(Sprite sprite) {
            super(sprite);
        }
    }

    public static class ParticleValue {
        boolean active;
        boolean alwaysActive;

        public void setAlwaysActive(boolean alwaysActive) {
            this.alwaysActive = alwaysActive;
        }

        public boolean isAlwaysActive() {
            return this.alwaysActive;
        }

        public boolean isActive() {
            return this.alwaysActive || this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void save(Writer output) throws IOException {
            if (!this.alwaysActive) {
                output.write("active: " + this.active + "\n");
            } else {
                this.active = true;
            }
        }

        public void load(BufferedReader reader) throws IOException {
            if (!this.alwaysActive) {
                this.active = ParticleEmitter.readBoolean(reader, "active");
            } else {
                this.active = true;
            }
        }

        public void load(ParticleValue value) {
            this.active = value.active;
            this.alwaysActive = value.alwaysActive;
        }
    }

    public static class NumericValue extends ParticleValue {
        private float value;

        public float getValue() {
            return this.value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void save(Writer output) throws IOException {
            super.save(output);
            if (this.active) {
                output.write("value: " + this.value + "\n");
            }
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void load(BufferedReader reader) throws IOException {
            super.load(reader);
            if (this.active) {
                this.value = ParticleEmitter.readFloat(reader, "value");
            }
        }

        public void load(NumericValue value) {
            super.load((ParticleValue) value);
            this.value = value.value;
        }
    }

    public static class RangedNumericValue extends ParticleValue {
        private float lowMax;
        private float lowMin;

        public float newLowValue() {
            return this.lowMin + ((this.lowMax - this.lowMin) * MathUtils.random());
        }

        public void setLow(float value) {
            this.lowMin = value;
            this.lowMax = value;
        }

        public void setLow(float min, float max) {
            this.lowMin = min;
            this.lowMax = max;
        }

        public float getLowMin() {
            return this.lowMin;
        }

        public void setLowMin(float lowMin) {
            this.lowMin = lowMin;
        }

        public float getLowMax() {
            return this.lowMax;
        }

        public void setLowMax(float lowMax) {
            this.lowMax = lowMax;
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void save(Writer output) throws IOException {
            super.save(output);
            if (this.active) {
                output.write("lowMin: " + this.lowMin + "\n");
                output.write("lowMax: " + this.lowMax + "\n");
            }
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void load(BufferedReader reader) throws IOException {
            super.load(reader);
            if (this.active) {
                this.lowMin = ParticleEmitter.readFloat(reader, "lowMin");
                this.lowMax = ParticleEmitter.readFloat(reader, "lowMax");
            }
        }

        public void load(RangedNumericValue value) {
            super.load((ParticleValue) value);
            this.lowMax = value.lowMax;
            this.lowMin = value.lowMin;
        }
    }

    public static class ScaledNumericValue extends RangedNumericValue {
        private float highMax;
        private float highMin;
        private boolean relative;
        private float[] scaling = {1.0f};
        float[] timeline = {0.0f};

        public float newHighValue() {
            return this.highMin + ((this.highMax - this.highMin) * MathUtils.random());
        }

        public void setHigh(float value) {
            this.highMin = value;
            this.highMax = value;
        }

        public void setHigh(float min, float max) {
            this.highMin = min;
            this.highMax = max;
        }

        public float getHighMin() {
            return this.highMin;
        }

        public void setHighMin(float highMin) {
            this.highMin = highMin;
        }

        public float getHighMax() {
            return this.highMax;
        }

        public void setHighMax(float highMax) {
            this.highMax = highMax;
        }

        public float[] getScaling() {
            return this.scaling;
        }

        public void setScaling(float[] values) {
            this.scaling = values;
        }

        public float[] getTimeline() {
            return this.timeline;
        }

        public void setTimeline(float[] timeline) {
            this.timeline = timeline;
        }

        public boolean isRelative() {
            return this.relative;
        }

        public void setRelative(boolean relative) {
            this.relative = relative;
        }

        public float getScale(float percent) {
            int endIndex = -1;
            float[] timeline = this.timeline;
            int n = timeline.length;
            for (int i = 1; i < n; i++) {
                float t = timeline[i];
                if (t > percent) {
                    endIndex = i;
                    break;
                }
            }
            if (endIndex == -1) {
                return this.scaling[n - 1];
            }
            float[] scaling = this.scaling;
            int startIndex = endIndex - 1;
            float startValue = scaling[startIndex];
            float startTime = timeline[startIndex];
            return ((scaling[endIndex] - startValue) * ((percent - startTime) / (timeline[endIndex] - startTime))) + startValue;
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue, com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void save(Writer output) throws IOException {
            super.save(output);
            if (this.active) {
                output.write("highMin: " + this.highMin + "\n");
                output.write("highMax: " + this.highMax + "\n");
                output.write("relative: " + this.relative + "\n");
                output.write("scalingCount: " + this.scaling.length + "\n");
                for (int i = 0; i < this.scaling.length; i++) {
                    output.write("scaling" + i + ": " + this.scaling[i] + "\n");
                }
                output.write("timelineCount: " + this.timeline.length + "\n");
                for (int i2 = 0; i2 < this.timeline.length; i2++) {
                    output.write("timeline" + i2 + ": " + this.timeline[i2] + "\n");
                }
            }
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue, com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void load(BufferedReader reader) throws IOException {
            super.load(reader);
            if (this.active) {
                this.highMin = ParticleEmitter.readFloat(reader, "highMin");
                this.highMax = ParticleEmitter.readFloat(reader, "highMax");
                this.relative = ParticleEmitter.readBoolean(reader, "relative");
                this.scaling = new float[ParticleEmitter.readInt(reader, "scalingCount")];
                for (int i = 0; i < this.scaling.length; i++) {
                    this.scaling[i] = ParticleEmitter.readFloat(reader, "scaling" + i);
                }
                this.timeline = new float[ParticleEmitter.readInt(reader, "timelineCount")];
                for (int i2 = 0; i2 < this.timeline.length; i2++) {
                    this.timeline[i2] = ParticleEmitter.readFloat(reader, "timeline" + i2);
                }
            }
        }

        public void load(ScaledNumericValue value) {
            super.load((RangedNumericValue) value);
            this.highMax = value.highMax;
            this.highMin = value.highMin;
            this.scaling = new float[value.scaling.length];
            System.arraycopy(value.scaling, 0, this.scaling, 0, this.scaling.length);
            this.timeline = new float[value.timeline.length];
            System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
            this.relative = value.relative;
        }
    }

    public static class GradientColorValue extends ParticleValue {
        private static float[] temp = new float[4];
        private float[] colors = {1.0f, 1.0f, 1.0f};
        float[] timeline = {0.0f};

        public GradientColorValue() {
            this.alwaysActive = true;
        }

        public float[] getTimeline() {
            return this.timeline;
        }

        public void setTimeline(float[] timeline) {
            this.timeline = timeline;
        }

        public float[] getColors() {
            return this.colors;
        }

        public void setColors(float[] colors) {
            this.colors = colors;
        }

        public float[] getColor(float percent) {
            int startIndex = 0;
            int endIndex = -1;
            float[] timeline = this.timeline;
            int n = timeline.length;
            for (int i = 1; i < n; i++) {
                float t = timeline[i];
                if (t > percent) {
                    endIndex = i;
                    break;
                }
                startIndex = i;
            }
            float startTime = timeline[startIndex];
            int startIndex2 = startIndex * 3;
            float r1 = this.colors[startIndex2];
            float g1 = this.colors[startIndex2 + 1];
            float b1 = this.colors[startIndex2 + 2];
            if (endIndex == -1) {
                temp[0] = r1;
                temp[1] = g1;
                temp[2] = b1;
                return temp;
            }
            float factor = (percent - startTime) / (timeline[endIndex] - startTime);
            int endIndex2 = endIndex * 3;
            temp[0] = ((this.colors[endIndex2] - r1) * factor) + r1;
            temp[1] = ((this.colors[endIndex2 + 1] - g1) * factor) + g1;
            temp[2] = ((this.colors[endIndex2 + 2] - b1) * factor) + b1;
            return temp;
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void save(Writer output) throws IOException {
            super.save(output);
            if (this.active) {
                output.write("colorsCount: " + this.colors.length + "\n");
                for (int i = 0; i < this.colors.length; i++) {
                    output.write("colors" + i + ": " + this.colors[i] + "\n");
                }
                output.write("timelineCount: " + this.timeline.length + "\n");
                for (int i2 = 0; i2 < this.timeline.length; i2++) {
                    output.write("timeline" + i2 + ": " + this.timeline[i2] + "\n");
                }
            }
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void load(BufferedReader reader) throws IOException {
            super.load(reader);
            if (this.active) {
                this.colors = new float[ParticleEmitter.readInt(reader, "colorsCount")];
                for (int i = 0; i < this.colors.length; i++) {
                    this.colors[i] = ParticleEmitter.readFloat(reader, "colors" + i);
                }
                this.timeline = new float[ParticleEmitter.readInt(reader, "timelineCount")];
                for (int i2 = 0; i2 < this.timeline.length; i2++) {
                    this.timeline[i2] = ParticleEmitter.readFloat(reader, "timeline" + i2);
                }
            }
        }

        public void load(GradientColorValue value) {
            super.load((ParticleValue) value);
            this.colors = new float[value.colors.length];
            System.arraycopy(value.colors, 0, this.colors, 0, this.colors.length);
            this.timeline = new float[value.timeline.length];
            System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
        }
    }

    public static class SpawnShapeValue extends ParticleValue {
        boolean edges;
        SpawnShape shape = SpawnShape.point;
        SpawnEllipseSide side = SpawnEllipseSide.both;

        public SpawnShape getShape() {
            return this.shape;
        }

        public void setShape(SpawnShape shape) {
            this.shape = shape;
        }

        public boolean isEdges() {
            return this.edges;
        }

        public void setEdges(boolean edges) {
            this.edges = edges;
        }

        public SpawnEllipseSide getSide() {
            return this.side;
        }

        public void setSide(SpawnEllipseSide side) {
            this.side = side;
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void save(Writer output) throws IOException {
            super.save(output);
            if (this.active) {
                output.write("shape: " + this.shape + "\n");
                if (this.shape == SpawnShape.ellipse) {
                    output.write("edges: " + this.edges + "\n");
                    output.write("side: " + this.side + "\n");
                }
            }
        }

        @Override // com.badlogic.gdx.graphics.g2d.ParticleEmitter.ParticleValue
        public void load(BufferedReader reader) throws IOException {
            super.load(reader);
            if (this.active) {
                this.shape = SpawnShape.valueOf(ParticleEmitter.readString(reader, "shape"));
                if (this.shape == SpawnShape.ellipse) {
                    this.edges = ParticleEmitter.readBoolean(reader, "edges");
                    this.side = SpawnEllipseSide.valueOf(ParticleEmitter.readString(reader, "side"));
                }
            }
        }

        public void load(SpawnShapeValue value) {
            super.load((ParticleValue) value);
            this.shape = value.shape;
            this.edges = value.edges;
            this.side = value.side;
        }
    }
}
