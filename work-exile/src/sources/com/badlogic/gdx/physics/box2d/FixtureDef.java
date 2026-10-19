package com.badlogic.gdx.physics.box2d;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FixtureDef {
    public Shape shape;
    public float friction = 0.2f;
    public float restitution = 0.0f;
    public float density = 0.0f;
    public boolean isSensor = false;
    public final Filter filter = new Filter();
}
