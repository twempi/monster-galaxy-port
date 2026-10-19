package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Box2DDebugRenderer {
    private static Vector2[] vertices = new Vector2[100];
    private static Vector2 t = new Vector2();
    private static Vector2 axis = new Vector2();
    private final Color SHAPE_NOT_ACTIVE = new Color(0.5f, 0.5f, 0.3f, 1.0f);
    private final Color SHAPE_STATIC = new Color(0.5f, 0.9f, 0.5f, 1.0f);
    private final Color SHAPE_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1.0f);
    private final Color SHAPE_NOT_AWAKE = new Color(0.6f, 0.6f, 0.6f, 1.0f);
    private final Color SHAPE_AWAKE = new Color(0.9f, 0.7f, 0.7f, 1.0f);
    private final Color JOINT_COLOR = new Color(0.5f, 0.8f, 0.8f, 1.0f);
    private final Vector2 v = new Vector2();
    protected ImmediateModeRenderer10 renderer = new ImmediateModeRenderer10();
    public SpriteBatch batch = new SpriteBatch();

    public Box2DDebugRenderer() {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2();
        }
    }

    public void render(World world) {
        renderBodies(world);
    }

    private void renderBodies(World world) {
        Iterator<Body> iter = world.getBodies();
        while (iter.hasNext()) {
            Body body = iter.next();
            Transform transform = body.getTransform();
            int len = body.getFixtureList().size();
            List<Fixture> fixtures = body.getFixtureList();
            for (int i = 0; i < len; i++) {
                Fixture fixture = fixtures.get(i);
                if (!body.isActive()) {
                    drawShape(fixture, transform, this.SHAPE_NOT_ACTIVE);
                } else if (body.getType() == BodyDef.BodyType.StaticBody) {
                    drawShape(fixture, transform, this.SHAPE_STATIC);
                } else if (body.getType() == BodyDef.BodyType.KinematicBody) {
                    drawShape(fixture, transform, this.SHAPE_KINEMATIC);
                } else if (!body.isAwake()) {
                    drawShape(fixture, transform, this.SHAPE_NOT_AWAKE);
                } else {
                    drawShape(fixture, transform, this.SHAPE_AWAKE);
                }
            }
        }
        Iterator<Joint> iter2 = world.getJoints();
        while (iter2.hasNext()) {
            Joint joint = iter2.next();
            drawJoint(joint);
        }
        int len2 = world.getContactList().size();
        for (int i2 = 0; i2 < len2; i2++) {
            drawContact(world.getContactList().get(i2));
        }
    }

    private void drawShape(Fixture fixture, Transform transform, Color color) {
        if (fixture.getType() == Shape.Type.Circle) {
            CircleShape circle = (CircleShape) fixture.getShape();
            t.set(circle.getPosition());
            transform.mul(t);
            drawSolidCircle(t, circle.getRadius(), axis.set(transform.vals[2], transform.vals[3]), color);
            return;
        }
        PolygonShape poly = (PolygonShape) fixture.getShape();
        int vertexCount = poly.getVertexCount();
        for (int i = 0; i < vertexCount; i++) {
            poly.getVertex(i, vertices[i]);
            transform.mul(vertices[i]);
        }
        drawSolidPolygon(vertices, vertexCount, color);
    }

    private void drawSolidCircle(Vector2 center, float radius, Vector2 axis2, Color color) {
        this.renderer.begin(2);
        float angle = 0.0f;
        int i = 0;
        while (i < 20) {
            this.v.set((((float) Math.cos(angle)) * radius) + center.x, (((float) Math.sin(angle)) * radius) + center.y);
            this.renderer.color(color.r, color.g, color.b, color.a);
            this.renderer.vertex(this.v.x, this.v.y, 0.0f);
            i++;
            angle += 0.31415927f;
        }
        this.renderer.end();
        this.renderer.begin(1);
        this.renderer.color(color.r, color.g, color.b, color.a);
        this.renderer.vertex(center.x, center.y, 0.0f);
        this.renderer.color(color.r, color.g, color.b, color.a);
        this.renderer.vertex(center.x + (axis2.x * radius), center.y + (axis2.y * radius), 0.0f);
        this.renderer.end();
    }

    private void drawSolidPolygon(Vector2[] vertices2, int vertexCount, Color color) {
        this.renderer.begin(2);
        for (int i = 0; i < vertexCount; i++) {
            Vector2 v = vertices2[i];
            this.renderer.color(color.r, color.g, color.b, color.a);
            this.renderer.vertex(v.x, v.y, 0.0f);
        }
        this.renderer.end();
    }

    private void drawJoint(Joint joint) {
        Body bodyA = joint.getBodyA();
        Body bodyB = joint.getBodyB();
        Transform xf1 = bodyA.getTransform();
        Transform xf2 = bodyB.getTransform();
        Vector2 x1 = xf1.getPosition();
        Vector2 x2 = xf2.getPosition();
        Vector2 p1 = joint.getAnchorA();
        Vector2 p2 = joint.getAnchorB();
        if (joint.getType() == JointDef.JointType.DistanceJoint) {
            drawSegment(p1, p2, this.JOINT_COLOR);
            return;
        }
        if (joint.getType() == JointDef.JointType.PulleyJoint) {
            PulleyJoint pulley = (PulleyJoint) joint;
            Vector2 s1 = pulley.getGroundAnchorA();
            Vector2 s2 = pulley.getGroundAnchorB();
            drawSegment(s1, p1, this.JOINT_COLOR);
            drawSegment(s2, p2, this.JOINT_COLOR);
            drawSegment(s1, s2, this.JOINT_COLOR);
            return;
        }
        if (joint.getType() == JointDef.JointType.MouseJoint) {
            drawSegment(joint.getAnchorA(), joint.getAnchorB(), this.JOINT_COLOR);
            return;
        }
        drawSegment(x1, p1, this.JOINT_COLOR);
        drawSegment(p1, p2, this.JOINT_COLOR);
        drawSegment(x2, p2, this.JOINT_COLOR);
    }

    private void drawSegment(Vector2 x1, Vector2 x2, Color color) {
        this.renderer.begin(1);
        this.renderer.color(color.r, color.g, color.b, color.a);
        this.renderer.vertex(x1.x, x1.y, 0.0f);
        this.renderer.color(color.r, color.g, color.b, color.a);
        this.renderer.vertex(x2.x, x2.y, 0.0f);
        this.renderer.end();
    }

    private void drawContact(Contact contact) {
    }

    public void dispose() {
        this.batch.dispose();
    }
}
