package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.GearJoint;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.physics.box2d.joints.LineJoint;
import com.badlogic.gdx.physics.box2d.joints.LineJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.Pool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class World implements Disposable {
    private final long addr;
    protected final LongMap<Body> bodies;
    private final Contact contact;
    private long[] contactAddrs;
    protected ContactFilter contactFilter;
    protected ContactListener contactListener;
    private final ArrayList<Contact> contacts;
    protected final LongMap<Fixture> fixtures;
    protected final Pool<Body> freeBodies;
    private final ArrayList<Contact> freeContacts;
    protected final Pool<Fixture> freeFixtures;
    final Vector2 gravity;
    private final ContactImpulse impulse;
    protected final LongMap<Joint> joints;
    private final Manifold manifold;
    private QueryCallback queryCallback;
    private RayCastCallback rayCastCallback;
    private Vector2 rayNormal;
    private Vector2 rayPoint;
    final float[] tmpGravity;

    public static native float getVelocityThreshold();

    private native void jniClearForces(long j);

    private native long jniCreateBody(long j, int i, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, float f9);

    private native long jniCreateDistanceJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5, float f6, float f7);

    private native long jniCreateFrictionJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5, float f6);

    private native long jniCreateGearJoint(long j, long j2, long j3, boolean z, long j4, long j5, float f);

    private native long jniCreateLineJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5, float f6, boolean z2, float f7, float f8, boolean z3, float f9, float f10);

    private native long jniCreateMouseJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5);

    private native long jniCreatePrismaticJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean z2, float f8, float f9, boolean z3, float f10, float f11);

    private native long jniCreatePulleyJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13);

    private native long jniCreateRevoluteJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5, boolean z2, float f6, float f7, boolean z3, float f8, float f9);

    private native long jniCreateWeldJoint(long j, long j2, long j3, boolean z, float f, float f2, float f3, float f4, float f5);

    private native void jniDestroyBody(long j, long j2);

    private native void jniDestroyJoint(long j, long j2);

    private native void jniDispose(long j);

    private native boolean jniGetAutoClearForces(long j);

    private native int jniGetBodyCount(long j);

    private native int jniGetContactCount(long j);

    private native void jniGetContactList(long j, long[] jArr);

    private native void jniGetGravity(long j, float[] fArr);

    private native int jniGetJointcount(long j);

    private native int jniGetProxyCount(long j);

    private native boolean jniIsLocked(long j);

    private native void jniQueryAABB(long j, float f, float f2, float f3, float f4);

    private native void jniRayCast(long j, float f, float f2, float f3, float f4);

    private native void jniSetAutoClearForces(long j, boolean z);

    private native void jniSetContiousPhysics(long j, boolean z);

    private native void jniSetGravity(long j, float f, float f2);

    private native void jniSetWarmStarting(long j, boolean z);

    private native void jniStep(long j, float f, int i, int i2);

    private native long newWorld(float f, float f2, boolean z);

    public static native void setVelocityThreshold(float f);

    public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2) {
        this.rayCastCallback = callback;
        jniRayCast(this.addr, point1.x, point1.y, point2.x, point2.y);
    }

    private float reportRayFixture(long addr, float pX, float pY, float nX, float nY, float fraction) {
        if (this.rayCastCallback != null) {
            return this.rayCastCallback.reportRayFixture(this.fixtures.get(addr), this.rayPoint.set(pX, pY), this.rayNormal.set(nX, nY), fraction);
        }
        return 0.0f;
    }

    public World(Vector2 gravity, boolean doSleep) {
        int i = HttpResponseCode.OK;
        int i2 = 100;
        this.freeBodies = new Pool<Body>(i2, i) { // from class: com.badlogic.gdx.physics.box2d.World.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.badlogic.gdx.utils.Pool
            public Body newObject() {
                return new Body(World.this, 0L);
            }
        };
        this.freeFixtures = new Pool<Fixture>(i2, i) { // from class: com.badlogic.gdx.physics.box2d.World.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.badlogic.gdx.utils.Pool
            public Fixture newObject() {
                return new Fixture(null, 0L);
            }
        };
        this.bodies = new LongMap<>(100);
        this.fixtures = new LongMap<>(100);
        this.joints = new LongMap<>(100);
        this.contactFilter = null;
        this.contactListener = null;
        this.rayCastCallback = null;
        this.rayPoint = new Vector2();
        this.rayNormal = new Vector2();
        this.tmpGravity = new float[2];
        this.gravity = new Vector2();
        this.queryCallback = null;
        this.contactAddrs = new long[HttpResponseCode.OK];
        this.contacts = new ArrayList<>();
        this.freeContacts = new ArrayList<>();
        this.contact = new Contact(this, 0L);
        this.manifold = new Manifold(this, 0L);
        this.impulse = new ContactImpulse(this, 0L);
        this.addr = newWorld(gravity.x, gravity.y, doSleep);
        this.contacts.ensureCapacity(this.contactAddrs.length);
        this.freeContacts.ensureCapacity(this.contactAddrs.length);
        for (int i3 = 0; i3 < this.contactAddrs.length; i3++) {
            this.freeContacts.add(new Contact(this, 0L));
        }
    }

    public void setDestructionListener(DestructionListener listener) {
    }

    public void setContactFilter(ContactFilter filter) {
        this.contactFilter = filter;
    }

    public void setContactListener(ContactListener listener) {
        this.contactListener = listener;
    }

    public Body createBody(BodyDef def) {
        long bodyAddr = jniCreateBody(this.addr, def.type.getValue(), def.position.x, def.position.y, def.angle, def.linearVelocity.x, def.linearVelocity.y, def.angularVelocity, def.linearDamping, def.angularDamping, def.allowSleep, def.awake, def.fixedRotation, def.bullet, def.active, def.inertiaScale);
        Body body = this.freeBodies.obtain();
        body.reset(bodyAddr);
        this.bodies.put(body.addr, body);
        return body;
    }

    public void destroyBody(Body body) {
        this.bodies.remove(body.addr);
        for (int i = 0; i < body.getFixtureList().size(); i++) {
            this.fixtures.remove(body.getFixtureList().get(i).addr);
        }
        for (int i2 = 0; i2 < body.getJointList().size(); i2++) {
            this.joints.remove(body.getJointList().get(i2).joint.addr);
        }
        jniDestroyBody(this.addr, body.addr);
        this.freeBodies.free(body);
    }

    public Joint createJoint(JointDef def) {
        long jointAddr = createProperJoint(def);
        Joint joint = def.type == JointDef.JointType.DistanceJoint ? new DistanceJoint(this, jointAddr) : null;
        if (def.type == JointDef.JointType.FrictionJoint) {
            joint = new FrictionJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.GearJoint) {
            joint = new GearJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.LineJoint) {
            joint = new LineJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.MouseJoint) {
            joint = new MouseJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.PrismaticJoint) {
            joint = new PrismaticJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.PulleyJoint) {
            joint = new PulleyJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.RevoluteJoint) {
            joint = new RevoluteJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.WeldJoint) {
            joint = new WeldJoint(this, jointAddr);
        }
        if (joint != null) {
            this.joints.put(joint.addr, joint);
        }
        JointEdge jointEdgeA = new JointEdge(def.bodyB, joint);
        JointEdge jointEdgeB = new JointEdge(def.bodyA, joint);
        joint.jointEdgeA = jointEdgeA;
        joint.jointEdgeB = jointEdgeB;
        def.bodyA.joints.add(jointEdgeA);
        def.bodyB.joints.add(jointEdgeB);
        return joint;
    }

    private long createProperJoint(JointDef def) {
        if (def.type == JointDef.JointType.DistanceJoint) {
            DistanceJointDef d = (DistanceJointDef) def;
            return jniCreateDistanceJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.length, d.frequencyHz, d.dampingRatio);
        }
        if (def.type == JointDef.JointType.FrictionJoint) {
            FrictionJointDef d2 = (FrictionJointDef) def;
            return jniCreateFrictionJoint(this.addr, d2.bodyA.addr, d2.bodyB.addr, d2.collideConnected, d2.localAnchorA.x, d2.localAnchorA.y, d2.localAnchorB.x, d2.localAnchorB.y, d2.maxForce, d2.maxTorque);
        }
        if (def.type == JointDef.JointType.GearJoint) {
            GearJointDef d3 = (GearJointDef) def;
            return jniCreateGearJoint(this.addr, d3.bodyA.addr, d3.bodyB.addr, d3.collideConnected, d3.joint1.addr, d3.joint2.addr, d3.ratio);
        }
        if (def.type == JointDef.JointType.LineJoint) {
            LineJointDef d4 = (LineJointDef) def;
            return jniCreateLineJoint(this.addr, d4.bodyA.addr, d4.bodyB.addr, d4.collideConnected, d4.localAnchorA.x, d4.localAnchorA.y, d4.localAnchorB.x, d4.localAnchorB.y, d4.localAxisA.x, d4.localAxisA.y, d4.enableLimit, d4.lowerTranslation, d4.upperTranslation, d4.enableMotor, d4.maxMotorForce, d4.motorSpeed);
        }
        if (def.type == JointDef.JointType.MouseJoint) {
            MouseJointDef d5 = (MouseJointDef) def;
            return jniCreateMouseJoint(this.addr, d5.bodyA.addr, d5.bodyB.addr, d5.collideConnected, d5.target.x, d5.target.y, d5.maxForce, d5.frequencyHz, d5.dampingRatio);
        }
        if (def.type == JointDef.JointType.PrismaticJoint) {
            PrismaticJointDef d6 = (PrismaticJointDef) def;
            return jniCreatePrismaticJoint(this.addr, d6.bodyA.addr, d6.bodyB.addr, d6.collideConnected, d6.localAnchorA.x, d6.localAnchorA.y, d6.localAnchorB.x, d6.localAnchorB.y, d6.localAxis1.x, d6.localAxis1.y, d6.referenceAngle, d6.enableLimit, d6.lowerTranslation, d6.upperTranslation, d6.enableMotor, d6.maxMotorForce, d6.motorSpeed);
        }
        if (def.type == JointDef.JointType.PulleyJoint) {
            PulleyJointDef d7 = (PulleyJointDef) def;
            return jniCreatePulleyJoint(this.addr, d7.bodyA.addr, d7.bodyB.addr, d7.collideConnected, d7.groundAnchorA.x, d7.groundAnchorA.y, d7.groundAnchorB.x, d7.groundAnchorB.y, d7.localAnchorA.x, d7.localAnchorA.y, d7.localAnchorB.x, d7.localAnchorB.y, d7.lengthA, d7.maxLengthA, d7.lengthB, d7.maxLengthB, d7.ratio);
        }
        if (def.type == JointDef.JointType.RevoluteJoint) {
            RevoluteJointDef d8 = (RevoluteJointDef) def;
            return jniCreateRevoluteJoint(this.addr, d8.bodyA.addr, d8.bodyB.addr, d8.collideConnected, d8.localAnchorA.x, d8.localAnchorA.y, d8.localAnchorB.x, d8.localAnchorB.y, d8.referenceAngle, d8.enableLimit, d8.lowerAngle, d8.upperAngle, d8.enableMotor, d8.motorSpeed, d8.maxMotorTorque);
        }
        if (def.type == JointDef.JointType.WeldJoint) {
            WeldJointDef d9 = (WeldJointDef) def;
            return jniCreateWeldJoint(this.addr, d9.bodyA.addr, d9.bodyB.addr, d9.collideConnected, d9.localAnchorA.x, d9.localAnchorA.y, d9.localAnchorB.x, d9.localAnchorB.y, d9.referenceAngle);
        }
        return 0L;
    }

    public void destroyJoint(Joint joint) {
        this.joints.remove(joint.addr);
        joint.jointEdgeA.other.joints.remove(joint.jointEdgeB);
        joint.jointEdgeB.other.joints.remove(joint.jointEdgeA);
        jniDestroyJoint(this.addr, joint.addr);
    }

    public void step(float timeStep, int velocityIterations, int positionIterations) {
        jniStep(this.addr, timeStep, velocityIterations, positionIterations);
    }

    public void clearForces() {
        jniClearForces(this.addr);
    }

    public void setWarmStarting(boolean flag) {
        jniSetWarmStarting(this.addr, flag);
    }

    public void setContinuousPhysics(boolean flag) {
        jniSetContiousPhysics(this.addr, flag);
    }

    public int getProxyCount() {
        return jniGetProxyCount(this.addr);
    }

    public int getBodyCount() {
        return jniGetBodyCount(this.addr);
    }

    public int getJointCount() {
        return jniGetJointcount(this.addr);
    }

    public int getContactCount() {
        return jniGetContactCount(this.addr);
    }

    public void setGravity(Vector2 gravity) {
        jniSetGravity(this.addr, gravity.x, gravity.y);
    }

    public Vector2 getGravity() {
        jniGetGravity(this.addr, this.tmpGravity);
        this.gravity.x = this.tmpGravity[0];
        this.gravity.y = this.tmpGravity[1];
        return this.gravity;
    }

    public boolean isLocked() {
        return jniIsLocked(this.addr);
    }

    public void setAutoClearForces(boolean flag) {
        jniSetAutoClearForces(this.addr, flag);
    }

    public boolean getAutoClearForces() {
        return jniGetAutoClearForces(this.addr);
    }

    public void QueryAABB(QueryCallback callback, float lowerX, float lowerY, float upperX, float upperY) {
        this.queryCallback = callback;
        jniQueryAABB(this.addr, lowerX, lowerY, upperX, upperY);
    }

    public List<Contact> getContactList() {
        int numContacts = getContactCount();
        if (numContacts > this.contactAddrs.length) {
            int newSize = numContacts * 2;
            this.contactAddrs = new long[newSize];
            this.contacts.ensureCapacity(newSize);
            this.freeContacts.ensureCapacity(newSize);
        }
        if (numContacts > this.freeContacts.size()) {
            int freeConts = this.freeContacts.size();
            for (int i = 0; i < numContacts - freeConts; i++) {
                this.freeContacts.add(new Contact(this, 0L));
            }
        }
        jniGetContactList(this.addr, this.contactAddrs);
        this.contacts.clear();
        for (int i2 = 0; i2 < numContacts; i2++) {
            Contact contact = this.freeContacts.get(i2);
            contact.addr = this.contactAddrs[i2];
            this.contacts.add(contact);
        }
        return this.contacts;
    }

    public Iterator<Body> getBodies() {
        return this.bodies.values();
    }

    public Iterator<Joint> getJoints() {
        return this.joints.values();
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        jniDispose(this.addr);
    }

    private boolean contactFilter(long fixtureA, long fixtureB) {
        if (this.contactFilter != null) {
            return this.contactFilter.shouldCollide(this.fixtures.get(fixtureA), this.fixtures.get(fixtureB));
        }
        Filter filterA = this.fixtures.get(fixtureA).getFilterData();
        Filter filterB = this.fixtures.get(fixtureB).getFilterData();
        if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
            return filterA.groupIndex > 0;
        }
        boolean collide = ((filterA.maskBits & filterB.categoryBits) == 0 || (filterA.categoryBits & filterB.maskBits) == 0) ? false : true;
        return collide;
    }

    private void beginContact(long contactAddr) {
        this.contact.addr = contactAddr;
        if (this.contactListener != null) {
            this.contactListener.beginContact(this.contact);
        }
    }

    private void endContact(long contactAddr) {
        this.contact.addr = contactAddr;
        if (this.contactListener != null) {
            this.contactListener.endContact(this.contact);
        }
    }

    private void preSolve(long contactAddr, long manifoldAddr) {
        this.contact.addr = contactAddr;
        this.manifold.addr = manifoldAddr;
        if (this.contactListener != null) {
            this.contactListener.preSolve(this.contact, this.manifold);
        }
    }

    private void postSolve(long contactAddr, long impulseAddr) {
        this.contact.addr = contactAddr;
        this.impulse.addr = impulseAddr;
        if (this.contactListener != null) {
            this.contactListener.postSolve(this.contact, this.impulse);
        }
    }

    private boolean reportFixture(long addr) {
        if (this.queryCallback != null) {
            return this.queryCallback.reportFixture(this.fixtures.get(addr));
        }
        return false;
    }
}
