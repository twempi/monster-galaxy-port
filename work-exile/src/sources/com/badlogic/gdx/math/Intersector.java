package com.badlogic.gdx.math;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Intersector {
    private static final Vector3 v0 = new Vector3();
    private static final Vector3 v1 = new Vector3();
    private static final Vector3 v2 = new Vector3();
    private static final Plane p = new Plane(new Vector3(), 0.0f);
    private static final Vector3 i = new Vector3();
    private static final Vector3 dir = new Vector3();
    private static final Vector3 start = new Vector3();
    static Vector3 tmp = new Vector3();
    static Vector3 best = new Vector3();
    static Vector3 tmp1 = new Vector3();
    static Vector3 tmp2 = new Vector3();
    static Vector3 tmp3 = new Vector3();

    public static float getLowestPositiveRoot(float a, float b, float c) {
        float det = (b * b) - ((4.0f * a) * c);
        if (det < 0.0f) {
            return Float.NaN;
        }
        float sqrtD = (float) Math.sqrt(det);
        float invA = 1.0f / (2.0f * a);
        float r1 = ((-b) - sqrtD) * invA;
        float r2 = ((-b) + sqrtD) * invA;
        if (r1 > r2) {
            r2 = r1;
            r1 = r2;
        }
        if (r1 > 0.0f) {
            return r1;
        }
        if (r2 > 0.0f) {
            return r2;
        }
        return Float.NaN;
    }

    public static boolean isPointInTriangle(Vector3 point, Vector3 t1, Vector3 t2, Vector3 t3) {
        v0.set(t1).sub(point);
        v1.set(t2).sub(point);
        v2.set(t3).sub(point);
        float ab = v0.dot(v1);
        float ac = v0.dot(v2);
        float bc = v1.dot(v2);
        float cc = v2.dot(v2);
        if ((bc * ac) - (cc * ab) < 0.0f) {
            return false;
        }
        float bb = v1.dot(v1);
        return (ab * bc) - (ac * bb) >= 0.0f;
    }

    public static boolean intersectSegmentPlane(Vector3 start2, Vector3 end, Plane plane, Vector3 intersection) {
        Vector3 dir2 = end.tmp().sub(start2);
        float denom = dir2.dot(plane.getNormal());
        float t = (-(start2.dot(plane.getNormal()) + plane.getD())) / denom;
        if (t < 0.0f || t > 1.0f) {
            return false;
        }
        intersection.set(start2).add(dir2.mul(t));
        return true;
    }

    public static boolean isPointInPolygon(List<Vector2> polygon, Vector2 point) {
        int j = polygon.size() - 1;
        boolean oddNodes = false;
        for (int i2 = 0; i2 < polygon.size(); i2++) {
            if ((polygon.get(i2).y < point.y && polygon.get(j).y >= point.y) || (polygon.get(j).y < point.y && polygon.get(i2).y >= point.y)) {
                if (((polygon.get(j).x - polygon.get(i2).x) * ((point.y - polygon.get(i2).y) / (polygon.get(j).y - polygon.get(i2).y))) + polygon.get(i2).x < point.x) {
                    oddNodes = !oddNodes;
                }
            }
            j = i2;
        }
        return oddNodes;
    }

    public static float distanceLinePoint(Vector2 start2, Vector2 end, Vector2 point) {
        tmp.set(end.x, end.y, 0.0f).sub(start2.x, start2.y, 0.0f);
        float l = tmp.len();
        tmp2.set(start2.x, start2.y, 0.0f).sub(point.x, point.y, 0.0f);
        return tmp.crs(tmp2).len() / l;
    }

    public static boolean intersectSegmentCircle(Vector2 start2, Vector2 end, Vector2 center, float squareRadius) {
        tmp.set(end.x - start2.x, end.y - start2.y, 0.0f);
        tmp1.set(center.x - start2.x, center.y - start2.y, 0.0f);
        float l = tmp.len();
        float u = tmp1.dot(tmp.nor());
        if (u <= 0.0f) {
            tmp2.set(start2.x, start2.y, 0.0f);
        } else if (u >= l) {
            tmp2.set(end.x, end.y, 0.0f);
        } else {
            tmp3.set(tmp.mul(u));
            tmp2.set(tmp3.x + start2.x, tmp3.y + start2.y, 0.0f);
        }
        float x = center.x - tmp2.x;
        float y = center.y - tmp2.y;
        return (x * x) + (y * y) <= squareRadius;
    }

    public static float intersectSegmentCircleDisplace(Vector2 start2, Vector2 end, Vector2 point, float radius, Vector2 displacement) {
        float u = ((point.x - start2.x) * (end.x - start2.x)) + ((point.y - start2.y) * (end.y - start2.y));
        float d = start2.dst(end);
        float u2 = u / (d * d);
        if (u2 < 0.0f || u2 > 1.0f) {
            return Float.POSITIVE_INFINITY;
        }
        tmp.set(end.x, end.y, 0.0f).sub(start2.x, start2.y, 0.0f);
        tmp2.set(start2.x, start2.y, 0.0f).add(tmp.mul(u2));
        float d2 = tmp2.dst(point.x, point.y, 0.0f);
        if (d2 >= radius) {
            return Float.POSITIVE_INFINITY;
        }
        displacement.set(point).sub(tmp2.x, tmp2.y).nor();
        return d2;
    }

    public static boolean intersectRayPlane(Ray ray, Plane plane, Vector3 intersection) {
        float denom = ray.direction.dot(plane.getNormal());
        if (denom != 0.0f) {
            float t = (-(ray.origin.dot(plane.getNormal()) + plane.getD())) / denom;
            if (t < 0.0f) {
                return false;
            }
            if (intersection != null) {
                intersection.set(ray.origin).add(ray.direction.tmp().mul(t));
            }
            return true;
        }
        if (plane.testPoint(ray.origin) != Plane.PlaneSide.OnPlane) {
            return false;
        }
        if (intersection != null) {
            intersection.set(ray.origin);
        }
        return true;
    }

    public static boolean intersectRayTriangle(Ray ray, Vector3 t1, Vector3 t2, Vector3 t3, Vector3 intersection) {
        p.set(t1, t2, t3);
        if (!intersectRayPlane(ray, p, i)) {
            return false;
        }
        v0.set(t3).sub(t1);
        v1.set(t2).sub(t1);
        v2.set(i).sub(t1);
        float dot00 = v0.dot(v0);
        float dot01 = v0.dot(v1);
        float dot02 = v0.dot(v2);
        float dot11 = v1.dot(v1);
        float dot12 = v1.dot(v2);
        float denom = (dot00 * dot11) - (dot01 * dot01);
        if (denom == 0.0f) {
            return false;
        }
        float u = ((dot11 * dot02) - (dot01 * dot12)) / denom;
        float v = ((dot00 * dot12) - (dot01 * dot02)) / denom;
        if (u >= 0.0f && v >= 0.0f && u + v <= 1.0f) {
            if (intersection != null) {
                intersection.set(i);
            }
            return true;
        }
        return false;
    }

    public static boolean intersectRaySphere(Ray ray, Vector3 center, float radius, Vector3 intersection) {
        float q;
        dir.set(ray.direction).nor();
        start.set(ray.origin);
        float b = 2.0f * dir.dot(start.tmp().sub(center));
        float c = start.dst2(center) - (radius * radius);
        float disc = (b * b) - (4.0f * c);
        if (disc < 0.0f) {
            return false;
        }
        float distSqrt = (float) Math.sqrt(disc);
        if (b < 0.0f) {
            q = ((-b) - distSqrt) / 2.0f;
        } else {
            q = ((-b) + distSqrt) / 2.0f;
        }
        float t0 = q / 1.0f;
        float t1 = c / q;
        if (t0 > t1) {
            t0 = t1;
            t1 = t0;
        }
        if (t1 < 0.0f) {
            return false;
        }
        if (t0 < 0.0f) {
            if (intersection != null) {
                intersection.set(start).add(dir.tmp().mul(t1));
            }
            return true;
        }
        if (intersection != null) {
            intersection.set(start).add(dir.tmp().mul(t0));
        }
        return true;
    }

    public static boolean intersectRayBoundsFast(Ray ray, BoundingBox box) {
        float min;
        float max;
        float divX = 1.0f / ray.direction.x;
        float divY = 1.0f / ray.direction.y;
        float divZ = 1.0f / ray.direction.z;
        float a = (box.min.x - ray.origin.x) * divX;
        float b = (box.max.x - ray.origin.x) * divX;
        if (a < b) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }
        float a2 = (box.min.y - ray.origin.y) * divY;
        float b2 = (box.max.y - ray.origin.y) * divY;
        if (a2 > b2) {
            a2 = b2;
            b2 = a2;
        }
        if (a2 > min) {
            min = a2;
        }
        if (b2 < max) {
            max = b2;
        }
        float a3 = (box.min.z - ray.origin.z) * divZ;
        float b3 = (box.max.z - ray.origin.z) * divZ;
        if (a3 > b3) {
            a3 = b3;
            b3 = a3;
        }
        if (a3 > min) {
            min = a3;
        }
        if (b3 < max) {
            max = b3;
        }
        return max >= 0.0f && max >= min;
    }

    public static boolean intersectRayTriangles(Ray ray, float[] triangles, Vector3 intersection) {
        float min_dist = Float.MAX_VALUE;
        boolean hit = false;
        if ((triangles.length / 3) % 3 != 0) {
            throw new RuntimeException("triangle list size is not a multiple of 3");
        }
        for (int i2 = 0; i2 < triangles.length - 6; i2 += 9) {
            boolean result = intersectRayTriangle(ray, tmp1.set(triangles[i2], triangles[i2 + 1], triangles[i2 + 2]), tmp2.set(triangles[i2 + 3], triangles[i2 + 4], triangles[i2 + 5]), tmp3.set(triangles[i2 + 6], triangles[i2 + 7], triangles[i2 + 8]), tmp);
            if (result) {
                float dist = ray.origin.tmp().sub(tmp).len();
                if (dist < min_dist) {
                    min_dist = dist;
                    best.set(tmp);
                    hit = true;
                }
            }
        }
        if (!hit) {
            return false;
        }
        if (intersection == null) {
            return true;
        }
        intersection.set(best);
        return true;
    }

    public static boolean intersectRayTriangles(Ray ray, float[] vertices, short[] indices, int vertexSize, Vector3 intersection) {
        float min_dist = Float.MAX_VALUE;
        boolean hit = false;
        if (indices.length % 3 != 0) {
            throw new RuntimeException("triangle list size is not a multiple of 3");
        }
        for (int i2 = 0; i2 < indices.length; i2 += 3) {
            int i1 = indices[i2] * vertexSize;
            int i3 = indices[i2 + 1] * vertexSize;
            int i4 = indices[i2 + 2] * vertexSize;
            boolean result = intersectRayTriangle(ray, tmp1.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]), tmp2.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]), tmp3.set(vertices[i4], vertices[i4 + 1], vertices[i4 + 2]), tmp);
            if (result) {
                float dist = ray.origin.tmp().sub(tmp).len();
                if (dist < min_dist) {
                    min_dist = dist;
                    best.set(tmp);
                    hit = true;
                }
            }
        }
        if (!hit) {
            return false;
        }
        if (intersection != null) {
            intersection.set(best);
        }
        return true;
    }

    public static boolean intersectRayTriangles(Ray ray, List<Vector3> triangles, Vector3 intersection) {
        float min_dist = Float.MAX_VALUE;
        if (triangles.size() % 3 != 0) {
            throw new RuntimeException("triangle list size is not a multiple of 3");
        }
        for (int i2 = 0; i2 < triangles.size() - 2; i2 += 3) {
            boolean result = intersectRayTriangle(ray, triangles.get(i2), triangles.get(i2 + 1), triangles.get(i2 + 2), tmp);
            if (result) {
                float dist = ray.origin.tmp().sub(tmp).len();
                if (dist < min_dist) {
                    min_dist = dist;
                    best.set(tmp);
                }
            }
        }
        if (best == null) {
            return false;
        }
        if (intersection != null) {
            intersection.set(best);
        }
        return true;
    }

    public static boolean intersectRectangles(Rectangle a, Rectangle b) {
        return a.getX() <= b.getX() + b.getWidth() && a.getX() + a.getWidth() >= b.getX() && a.getY() <= b.getY() + b.getHeight() && a.getY() + a.getHeight() >= b.getY();
    }

    public static boolean intersectLines(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection) {
        float x1 = p1.x;
        float y1 = p1.y;
        float x2 = p2.x;
        float y2 = p2.y;
        float x3 = p3.x;
        float y3 = p3.y;
        float x4 = p4.x;
        float y4 = p4.y;
        float det1 = det(x1, y1, x2, y2);
        float det2 = det(x3, y3, x4, y4);
        float det3 = det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
        float x = det(det1, x1 - x2, det2, x3 - x4) / det3;
        float y = det(det1, y1 - y2, det2, y3 - y4) / det3;
        intersection.x = x;
        intersection.y = y;
        return true;
    }

    public static boolean intersectSegments(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection) {
        float x1 = p1.x;
        float y1 = p1.y;
        float x2 = p2.x;
        float y2 = p2.y;
        float x3 = p3.x;
        float y3 = p3.y;
        float x4 = p4.x;
        float y4 = p4.y;
        float d = ((y4 - y3) * (x2 - x1)) - ((x4 - x3) * (y2 - y1));
        if (d == 0.0f) {
            return false;
        }
        float ua = (((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3))) / d;
        float ub = (((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3))) / d;
        if (ua < 0.0f || ua > 1.0f || ub < 0.0f || ub > 1.0f) {
            return false;
        }
        if (intersection != null) {
            intersection.set(((x2 - x1) * ua) + x1, ((y2 - y1) * ua) + y1);
        }
        return true;
    }

    static float det(float a, float b, float c, float d) {
        return (a * d) - (b * c);
    }

    static double detd(double a, double b, double c, double d) {
        return (a * d) - (b * c);
    }

    public static boolean overlapCircles(Circle c1, Circle c2) {
        float x = c1.x - c2.x;
        float y = c1.y - c2.y;
        float distance = (x * x) + (y * y);
        float radiusSum = c1.radius + c2.radius;
        return distance <= radiusSum * radiusSum;
    }

    public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
        return r1.x < r2.x + r2.width && r1.x + r1.width > r2.x && r1.y < r2.y + r2.height && r1.y + r1.height > r2.y;
    }

    public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
        float closestX = c.x;
        float closestY = c.y;
        if (c.x < r.x) {
            closestX = r.x;
        } else if (c.x > r.x + r.width) {
            closestX = r.x + r.width;
        }
        if (c.y < r.y) {
            closestY = r.y;
        } else if (c.y > r.y + r.height) {
            closestY = r.y + r.height;
        }
        float closestX2 = closestX - c.x;
        float closestY2 = closestY - c.y;
        return (closestX2 * closestX2) + (closestY2 * closestY2) < c.radius * c.radius;
    }
}
