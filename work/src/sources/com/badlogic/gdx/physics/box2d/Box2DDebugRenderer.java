package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Box2DDebugRenderer {
    private static Vector2 lower;
    private static Vector2 upper;
    private final Color AABB_COLOR;
    private final Color JOINT_COLOR;
    private final Color SHAPE_AWAKE;
    private final Color SHAPE_KINEMATIC;
    private final Color SHAPE_NOT_ACTIVE;
    private final Color SHAPE_NOT_AWAKE;
    private final Color SHAPE_STATIC;
    private final Color VELOCITY_COLOR;
    private boolean drawAABBs;
    private boolean drawBodies;
    private boolean drawInactiveBodies;
    private boolean drawJoints;
    private boolean drawVelocities;
    private final Vector2 f;
    private final Vector2 lv;
    protected ShapeRenderer renderer;
    private final Vector2 v;
    private static Vector2[] vertices = new Vector2[1000];
    private static Vector2 t = new Vector2();
    private static Vector2 axis = new Vector2();

    public Box2DDebugRenderer() {
        this(true, true, false, true, false);
    }

    public Box2DDebugRenderer(boolean drawBodies, boolean drawJoints, boolean drawAABBs, boolean drawInactiveBodies, boolean drawVelocities) {
        this.SHAPE_NOT_ACTIVE = new Color(0.5f, 0.5f, 0.3f, 1.0f);
        this.SHAPE_STATIC = new Color(0.5f, 0.9f, 0.5f, 1.0f);
        this.SHAPE_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1.0f);
        this.SHAPE_NOT_AWAKE = new Color(0.6f, 0.6f, 0.6f, 1.0f);
        this.SHAPE_AWAKE = new Color(0.9f, 0.7f, 0.7f, 1.0f);
        this.JOINT_COLOR = new Color(0.5f, 0.8f, 0.8f, 1.0f);
        this.AABB_COLOR = new Color(1.0f, 0.0f, 1.0f, 1.0f);
        this.VELOCITY_COLOR = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        this.f = new Vector2();
        this.v = new Vector2();
        this.lv = new Vector2();
        this.renderer = new ShapeRenderer();
        lower = new Vector2();
        upper = new Vector2();
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2();
        }
        this.drawBodies = drawBodies;
        this.drawJoints = drawJoints;
        this.drawAABBs = drawAABBs;
        this.drawInactiveBodies = drawInactiveBodies;
        this.drawVelocities = drawVelocities;
    }

    public void render(World world, Matrix4 projMatrix) {
        this.renderer.setProjectionMatrix(projMatrix);
        renderBodies(world);
    }

    private void renderBodies(World world) {
        this.renderer.begin(ShapeRenderer.ShapeType.Line);
        if (this.drawBodies || this.drawAABBs) {
            Iterator<Body> iter = world.getBodies();
            while (iter.hasNext()) {
                Body body = iter.next();
                if (body.isActive() || this.drawInactiveBodies) {
                    renderBody(body);
                }
            }
        }
        if (this.drawJoints) {
            Iterator<Joint> iter2 = world.getJoints();
            while (iter2.hasNext()) {
                Joint joint = iter2.next();
                drawJoint(joint);
            }
        }
        this.renderer.end();
        if (Gdx.gl10 != null) {
            Gdx.gl10.glPointSize(3.0f);
        }
        this.renderer.begin(ShapeRenderer.ShapeType.Point);
        int len = world.getContactList().size();
        for (int i = 0; i < len; i++) {
            drawContact(world.getContactList().get(i));
        }
        this.renderer.end();
        if (Gdx.gl10 != null) {
            Gdx.gl10.glPointSize(1.0f);
        }
    }

    protected void renderBody(Body body) {
        Transform transform = body.getTransform();
        int len = body.getFixtureList().size();
        List<Fixture> fixtures = body.getFixtureList();
        for (int i = 0; i < len; i++) {
            Fixture fixture = fixtures.get(i);
            if (this.drawBodies) {
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
                if (this.drawVelocities) {
                    Vector2 position = body.getPosition();
                    drawSegment(position, body.getLinearVelocity().add(position), this.VELOCITY_COLOR);
                }
            }
            if (this.drawAABBs) {
                drawAABB(fixture, transform);
            }
        }
    }

    private void drawAABB(Fixture fixture, Transform transform) {
        if (fixture.getType() == Shape.Type.Circle) {
            CircleShape shape = (CircleShape) fixture.getShape();
            float radius = shape.getRadius();
            vertices[0].set(shape.getPosition());
            vertices[0].rotate(transform.getRotation()).add(transform.getPosition());
            lower.set(vertices[0].x - radius, vertices[0].y - radius);
            upper.set(vertices[0].x + radius, vertices[0].y + radius);
            vertices[0].set(lower.x, lower.y);
            vertices[1].set(upper.x, lower.y);
            vertices[2].set(upper.x, upper.y);
            vertices[3].set(lower.x, upper.y);
            drawSolidPolygon(vertices, 4, this.AABB_COLOR);
            return;
        }
        if (fixture.getType() == Shape.Type.Polygon) {
            PolygonShape shape2 = (PolygonShape) fixture.getShape();
            int vertexCount = shape2.getVertexCount();
            shape2.getVertex(0, vertices[0]);
            lower.set(transform.mul(vertices[0]));
            upper.set(lower);
            for (int i = 1; i < vertexCount; i++) {
                shape2.getVertex(i, vertices[i]);
                transform.mul(vertices[i]);
                lower.x = Math.min(lower.x, vertices[i].x);
                lower.y = Math.min(lower.y, vertices[i].y);
                upper.x = Math.max(upper.x, vertices[i].x);
                upper.y = Math.max(upper.y, vertices[i].y);
            }
            vertices[0].set(lower.x, lower.y);
            vertices[1].set(upper.x, lower.y);
            vertices[2].set(upper.x, upper.y);
            vertices[3].set(lower.x, upper.y);
            drawSolidPolygon(vertices, 4, this.AABB_COLOR);
        }
    }

    private void drawShape(Fixture fixture, Transform transform, Color color) {
        if (fixture.getType() == Shape.Type.Circle) {
            CircleShape circle = (CircleShape) fixture.getShape();
            t.set(circle.getPosition());
            transform.mul(t);
            drawSolidCircle(t, circle.getRadius(), axis.set(transform.vals[2], transform.vals[3]), color);
        }
        if (fixture.getType() == Shape.Type.Edge) {
            EdgeShape edge = (EdgeShape) fixture.getShape();
            edge.getVertex1(vertices[0]);
            edge.getVertex2(vertices[1]);
            transform.mul(vertices[0]);
            transform.mul(vertices[1]);
            drawSolidPolygon(vertices, 2, color);
        }
        if (fixture.getType() == Shape.Type.Polygon) {
            PolygonShape chain = (PolygonShape) fixture.getShape();
            int vertexCount = chain.getVertexCount();
            for (int i = 0; i < vertexCount; i++) {
                chain.getVertex(i, vertices[i]);
                transform.mul(vertices[i]);
            }
            drawSolidPolygon(vertices, vertexCount, color);
        }
        if (fixture.getType() == Shape.Type.Chain) {
            ChainShape chain2 = (ChainShape) fixture.getShape();
            int vertexCount2 = chain2.getVertexCount();
            for (int i2 = 0; i2 < vertexCount2; i2++) {
                chain2.getVertex(i2, vertices[i2]);
                transform.mul(vertices[i2]);
            }
            drawSolidPolygon(vertices, vertexCount2, color);
        }
    }

    private void drawSolidCircle(Vector2 center, float radius, Vector2 axis2, Color color) {
        float angle = 0.0f;
        this.renderer.setColor(color.r, color.g, color.b, color.a);
        int i = 0;
        while (i < 20) {
            this.v.set((((float) Math.cos(angle)) * radius) + center.x, (((float) Math.sin(angle)) * radius) + center.y);
            if (i == 0) {
                this.lv.set(this.v);
                this.f.set(this.v);
            } else {
                this.renderer.line(this.lv.x, this.lv.y, this.v.x, this.v.y);
                this.lv.set(this.v);
            }
            i++;
            angle += 0.31415927f;
        }
        this.renderer.line(this.f.x, this.f.y, this.lv.x, this.lv.y);
        this.renderer.line(center.x, center.y, 0.0f, center.x + (axis2.x * radius), center.y + (axis2.y * radius), 0.0f);
    }

    private void drawSolidPolygon(Vector2[] vertices2, int vertexCount, Color color) {
        this.renderer.setColor(color.r, color.g, color.b, color.a);
        for (int i = 0; i < vertexCount; i++) {
            Vector2 v = vertices2[i];
            if (i == 0) {
                this.lv.set(v);
                this.f.set(v);
            } else {
                this.renderer.line(this.lv.x, this.lv.y, v.x, v.y);
                this.lv.set(v);
            }
        }
        this.renderer.line(this.f.x, this.f.y, this.lv.x, this.lv.y);
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
        this.renderer.setColor(color);
        this.renderer.line(x1.x, x1.y, x2.x, x2.y);
    }

    private void drawContact(Contact contact) {
        WorldManifold worldManifold = contact.getWorldManifold();
        if (worldManifold.getNumberOfContactPoints() != 0) {
            Vector2 point = worldManifold.getPoints()[0];
            this.renderer.point(point.x, point.y, 0.0f);
        }
    }

    public boolean isDrawBodies() {
        return this.drawBodies;
    }

    public void setDrawBodies(boolean drawBodies) {
        this.drawBodies = drawBodies;
    }

    public boolean isDrawJoints() {
        return this.drawJoints;
    }

    public void setDrawJoints(boolean drawJoints) {
        this.drawJoints = drawJoints;
    }

    public boolean isDrawAABBs() {
        return this.drawAABBs;
    }

    public void setDrawAABBs(boolean drawAABBs) {
        this.drawAABBs = drawAABBs;
    }

    public boolean isDrawInactiveBodies() {
        return this.drawInactiveBodies;
    }

    public void setDrawInactiveBodies(boolean drawInactiveBodies) {
        this.drawInactiveBodies = drawInactiveBodies;
    }

    public boolean isDrawVelocities() {
        return this.drawVelocities;
    }

    public void setDrawVelocities(boolean drawVelocities) {
        this.drawVelocities = drawVelocities;
    }

    public static Vector2 getAxis() {
        return axis;
    }

    public static void setAxis(Vector2 axis2) {
        axis = axis2;
    }

    public void dispose() {
        this.renderer.dispose();
    }
}
