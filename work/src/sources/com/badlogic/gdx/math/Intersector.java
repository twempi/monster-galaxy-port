package com.badlogic.gdx.math;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
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
    static Vector3 intersection = new Vector3();

    public static class MinimumTranslationVector {
        public Vector2 normal = new Vector2();
        public float depth = 0.0f;
    }

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

    public static boolean intersectSegmentPlane(Vector3 start2, Vector3 end, Plane plane, Vector3 intersection2) {
        Vector3 dir2 = end.tmp().sub(start2);
        float denom = dir2.dot(plane.getNormal());
        float t = (-(start2.dot(plane.getNormal()) + plane.getD())) / denom;
        if (t < 0.0f || t > 1.0f) {
            return false;
        }
        intersection2.set(start2).add(dir2.mul(t));
        return true;
    }

    public static int pointLineSide(Vector2 linePoint1, Vector2 linePoint2, Vector2 point) {
        return (int) Math.signum(((linePoint2.x - linePoint1.x) * (point.y - linePoint1.y)) - ((linePoint2.y - linePoint1.y) * (point.x - linePoint1.x)));
    }

    public static int pointLineSide(float linePoint1X, float linePoint1Y, float linePoint2X, float linePoint2Y, float pointX, float pointY) {
        return (int) Math.signum(((linePoint2X - linePoint1X) * (pointY - linePoint1Y)) - ((linePoint2Y - linePoint1Y) * (pointX - linePoint1X)));
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
        tmp.set(end.x, end.y, 0.0f);
        float l2 = tmp.sub(start2.x, start2.y, 0.0f).len2();
        if (l2 == 0.0f) {
            return point.dst(start2);
        }
        tmp.set(point.x, point.y, 0.0f);
        tmp.sub(start2.x, start2.y, 0.0f);
        tmp2.set(end.x, end.y, 0.0f);
        tmp2.sub(start2.x, start2.y, 0.0f);
        float t = tmp.dot(tmp2) / l2;
        if (t < 0.0f) {
            return point.dst(start2);
        }
        if (t > 1.0f) {
            return point.dst(end);
        }
        tmp.set(end.x, end.y, 0.0f);
        tmp.sub(start2.x, start2.y, 0.0f).mul(t).add(start2.x, start2.y, 0.0f);
        return tmp2.set(point.x, point.y, 0.0f).dst(tmp);
    }

    public static float distanceLinePoint(float startX, float startY, float endX, float endY, float pointX, float pointY) {
        float normalLength = (float) Math.sqrt(((endX - startX) * (endX - startX)) + ((endY - startY) * (endY - startY)));
        return Math.abs(((pointX - startX) * (endY - startY)) - ((pointY - startY) * (endX - startX))) / normalLength;
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

    public static boolean intersectRayPlane(Ray ray, Plane plane, Vector3 intersection2) {
        float denom = ray.direction.dot(plane.getNormal());
        if (denom != 0.0f) {
            float t = (-(ray.origin.dot(plane.getNormal()) + plane.getD())) / denom;
            if (t < 0.0f) {
                return false;
            }
            if (intersection2 != null) {
                intersection2.set(ray.origin).add(ray.direction.tmp().mul(t));
            }
            return true;
        }
        if (plane.testPoint(ray.origin) != Plane.PlaneSide.OnPlane) {
            return false;
        }
        if (intersection2 != null) {
            intersection2.set(ray.origin);
        }
        return true;
    }

    public static float intersectLinePlane(float x, float y, float z, float x2, float y2, float z2, Plane plane, Vector3 intersection2) {
        Vector3 direction = tmp.set(x2, y2, z2).sub(x, y, z);
        Vector3 origin = tmp2.set(x, y, z);
        float denom = direction.dot(plane.getNormal());
        if (denom != 0.0f) {
            float t = (-(origin.dot(plane.getNormal()) + plane.getD())) / denom;
            if (t < 0.0f || t > 1.0f || intersection2 == null) {
                return t;
            }
            intersection2.set(origin).add(direction.mul(t));
            return t;
        }
        if (plane.testPoint(origin) == Plane.PlaneSide.OnPlane) {
            if (intersection2 != null) {
                intersection2.set(origin);
            }
            return 0.0f;
        }
        return -1.0f;
    }

    public static boolean intersectRayTriangle(Ray ray, Vector3 t1, Vector3 t2, Vector3 t3, Vector3 intersection2) {
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
            if (intersection2 != null) {
                intersection2.set(i);
            }
            return true;
        }
        return false;
    }

    public static boolean intersectRaySphere(Ray ray, Vector3 center, float radius, Vector3 intersection2) {
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
            if (intersection2 != null) {
                intersection2.set(start).add(dir.tmp().mul(t1));
            }
            return true;
        }
        if (intersection2 != null) {
            intersection2.set(start).add(dir.tmp().mul(t0));
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

    public static boolean intersectRayTriangles(Ray ray, float[] triangles, Vector3 intersection2) {
        float min_dist = Float.MAX_VALUE;
        boolean hit = false;
        if ((triangles.length / 3) % 3 != 0) {
            throw new RuntimeException("triangle list size is not a multiple of 3");
        }
        for (int i2 = 0; i2 < triangles.length - 6; i2 += 9) {
            boolean result = intersectRayTriangle(ray, tmp1.set(triangles[i2], triangles[i2 + 1], triangles[i2 + 2]), tmp2.set(triangles[i2 + 3], triangles[i2 + 4], triangles[i2 + 5]), tmp3.set(triangles[i2 + 6], triangles[i2 + 7], triangles[i2 + 8]), tmp);
            if (result) {
                float dist = ray.origin.tmp().sub(tmp).len2();
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
        if (intersection2 == null) {
            return true;
        }
        intersection2.set(best);
        return true;
    }

    public static boolean intersectRayTriangles(Ray ray, float[] vertices, short[] indices, int vertexSize, Vector3 intersection2) {
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
                float dist = ray.origin.tmp().sub(tmp).len2();
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
        if (intersection2 != null) {
            intersection2.set(best);
        }
        return true;
    }

    public static boolean intersectRayTriangles(Ray ray, List<Vector3> triangles, Vector3 intersection2) {
        float min_dist = Float.MAX_VALUE;
        boolean hit = false;
        if (triangles.size() % 3 != 0) {
            throw new RuntimeException("triangle list size is not a multiple of 3");
        }
        for (int i2 = 0; i2 < triangles.size() - 2; i2 += 3) {
            boolean result = intersectRayTriangle(ray, triangles.get(i2), triangles.get(i2 + 1), triangles.get(i2 + 2), tmp);
            if (result) {
                float dist = ray.origin.tmp().sub(tmp).len2();
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
        if (intersection2 != null) {
            intersection2.set(best);
        }
        return true;
    }

    public static boolean intersectRectangles(Rectangle a, Rectangle b) {
        return a.getX() <= b.getX() + b.getWidth() && a.getX() + a.getWidth() >= b.getX() && a.getY() <= b.getY() + b.getHeight() && a.getY() + a.getHeight() >= b.getY();
    }

    public static boolean intersectLines(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection2) {
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
        intersection2.x = x;
        intersection2.y = y;
        return true;
    }

    public static boolean intersectSegments(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection2) {
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
        if (intersection2 != null) {
            intersection2.set(((x2 - x1) * ua) + x1, ((y2 - y1) * ua) + y1);
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

    public static boolean overlapConvexPolygons(Polygon p1, Polygon p2) {
        return overlapConvexPolygons(p1, p2, (MinimumTranslationVector) null);
    }

    public static boolean overlapConvexPolygons(Polygon p1, Polygon p2, MinimumTranslationVector mtv) {
        return overlapConvexPolygons(p1.getTransformedVertices(), p2.getTransformedVertices(), mtv);
    }

    public static boolean overlapConvexPolygons(float[] verts1, float[] verts2, MinimumTranslationVector mtv) {
        float overlap = Float.MAX_VALUE;
        float smallestAxisX = 0.0f;
        float smallestAxisY = 0.0f;
        int numAxes1 = verts1.length;
        for (int i2 = 0; i2 < numAxes1; i2 += 2) {
            float x1 = verts1[i2];
            float y1 = verts1[i2 + 1];
            float x2 = verts1[(i2 + 2) % numAxes1];
            float y2 = verts1[(i2 + 3) % numAxes1];
            float axisX = y1 - y2;
            float axisY = -(x1 - x2);
            float length = (float) Math.sqrt((axisX * axisX) + (axisY * axisY));
            float axisX2 = axisX / length;
            float axisY2 = axisY / length;
            float min1 = (verts1[0] * axisX2) + (verts1[1] * axisY2);
            float max1 = min1;
            for (int j = 2; j < verts1.length; j += 2) {
                float p2 = (verts1[j] * axisX2) + (verts1[j + 1] * axisY2);
                if (p2 < min1) {
                    min1 = p2;
                } else if (p2 > max1) {
                    max1 = p2;
                }
            }
            float min2 = (verts2[0] * axisX2) + (verts2[1] * axisY2);
            float max2 = min2;
            for (int j2 = 2; j2 < verts2.length; j2 += 2) {
                float p3 = (verts2[j2] * axisX2) + (verts2[j2 + 1] * axisY2);
                if (p3 < min2) {
                    min2 = p3;
                } else if (p3 > max2) {
                    max2 = p3;
                }
            }
            if ((min1 > min2 || max1 < min2) && (min2 > min1 || max2 < min1)) {
                return false;
            }
            float o = Math.min(max1, max2) - Math.max(min1, min2);
            if ((min1 < min2 && max1 > max2) || (min2 < min1 && max2 > max1)) {
                float mins = Math.abs(min1 - min2);
                float maxs = Math.abs(max1 - max2);
                if (mins < maxs) {
                    axisX2 = -axisX2;
                    axisY2 = -axisY2;
                    o += mins;
                } else {
                    o += maxs;
                }
            }
            if (o < overlap) {
                overlap = o;
                smallestAxisX = axisX2;
                smallestAxisY = axisY2;
            }
        }
        int numAxes2 = verts2.length;
        for (int i3 = 0; i3 < numAxes2; i3 += 2) {
            float x3 = verts2[i3];
            float y3 = verts2[i3 + 1];
            float x4 = verts2[(i3 + 2) % numAxes2];
            float y4 = verts2[(i3 + 3) % numAxes2];
            float axisX3 = y3 - y4;
            float axisY3 = -(x3 - x4);
            float length2 = (float) Math.sqrt((axisX3 * axisX3) + (axisY3 * axisY3));
            float axisX4 = axisX3 / length2;
            float axisY4 = axisY3 / length2;
            float min3 = (verts1[0] * axisX4) + (verts1[1] * axisY4);
            float max3 = min3;
            for (int j3 = 2; j3 < verts1.length; j3 += 2) {
                float p4 = (verts1[j3] * axisX4) + (verts1[j3 + 1] * axisY4);
                if (p4 < min3) {
                    min3 = p4;
                } else if (p4 > max3) {
                    max3 = p4;
                }
            }
            float min4 = (verts2[0] * axisX4) + (verts2[1] * axisY4);
            float max4 = min4;
            for (int j4 = 2; j4 < verts2.length; j4 += 2) {
                float p5 = (verts2[j4] * axisX4) + (verts2[j4 + 1] * axisY4);
                if (p5 < min4) {
                    min4 = p5;
                } else if (p5 > max4) {
                    max4 = p5;
                }
            }
            if ((min3 > min4 || max3 < min4) && (min4 > min3 || max4 < min3)) {
                return false;
            }
            float o2 = Math.min(max3, max4) - Math.max(min3, min4);
            if ((min3 < min4 && max3 > max4) || (min4 < min3 && max4 > max3)) {
                float mins2 = Math.abs(min3 - min4);
                float maxs2 = Math.abs(max3 - max4);
                if (mins2 < maxs2) {
                    axisX4 = -axisX4;
                    axisY4 = -axisY4;
                    o2 += mins2;
                } else {
                    o2 += maxs2;
                }
            }
            if (o2 < overlap) {
                overlap = o2;
                smallestAxisX = axisX4;
                smallestAxisY = axisY4;
            }
        }
        if (mtv != null) {
            mtv.normal.set(smallestAxisX, smallestAxisY);
            mtv.depth = overlap;
        }
        return true;
    }

    public static void splitTriangle(float[] triangle, Plane plane, SplitTriangle split) {
        int stride = triangle.length / 3;
        boolean r1 = plane.testPoint(triangle[0], triangle[1], triangle[2]) == Plane.PlaneSide.Back;
        boolean r2 = plane.testPoint(triangle[stride + 0], triangle[stride + 1], triangle[stride + 2]) == Plane.PlaneSide.Back;
        boolean r3 = plane.testPoint(triangle[(stride * 2) + 0], triangle[(stride * 2) + 1], triangle[(stride * 2) + 2]) == Plane.PlaneSide.Back;
        split.reset();
        if (r1 == r2 && r2 == r3) {
            split.total = 1;
            if (r1) {
                split.numBack = 1;
                System.arraycopy(triangle, 0, split.back, 0, triangle.length);
                return;
            } else {
                split.numFront = 1;
                System.arraycopy(triangle, 0, split.front, 0, triangle.length);
                return;
            }
        }
        split.total = 3;
        split.numFront = (r3 ? 1 : 0) + (r1 ? 1 : 0) + (r2 ? 1 : 0);
        split.numBack = split.total - split.numFront;
        split.setSide(r1);
        if (r1 != r2) {
            splitEdge(triangle, 0, stride, stride, plane, split.edgeSplit, 0);
            split.add(triangle, 0, stride);
            split.add(split.edgeSplit, 0, stride);
            split.setSide(!split.getSide());
            split.add(split.edgeSplit, 0, stride);
        } else {
            split.add(triangle, 0, stride);
        }
        int second = stride + stride;
        if (r2 != r3) {
            splitEdge(triangle, stride, second, stride, plane, split.edgeSplit, 0);
            split.add(triangle, stride, stride);
            split.add(split.edgeSplit, 0, stride);
            split.setSide(!split.getSide());
            split.add(split.edgeSplit, 0, stride);
        } else {
            split.add(triangle, stride, stride);
        }
        int first = stride + stride;
        if (r3 != r1) {
            splitEdge(triangle, first, 0, stride, plane, split.edgeSplit, 0);
            split.add(triangle, first, stride);
            split.add(split.edgeSplit, 0, stride);
            split.setSide(split.getSide() ? false : true);
            split.add(split.edgeSplit, 0, stride);
        } else {
            split.add(triangle, first, stride);
        }
        if (split.numFront == 2) {
            System.arraycopy(split.front, stride * 2, split.front, stride * 3, stride * 2);
            System.arraycopy(split.front, 0, split.front, stride * 5, stride);
        } else {
            System.arraycopy(split.back, stride * 2, split.back, stride * 3, stride * 2);
            System.arraycopy(split.back, 0, split.back, stride * 5, stride);
        }
    }

    private static void splitEdge(float[] vertices, int s, int e, int stride, Plane plane, float[] split, int offset) {
        float t = intersectLinePlane(vertices[s], vertices[s + 1], vertices[s + 2], vertices[e], vertices[e + 1], vertices[e + 2], plane, intersection);
        split[offset + 0] = intersection.x;
        split[offset + 1] = intersection.y;
        split[offset + 2] = intersection.z;
        for (int i2 = 3; i2 < stride; i2++) {
            float a = vertices[s + i2];
            float b = vertices[e + i2];
            split[offset + i2] = ((b - a) * t) + a;
        }
    }

    public static void main(String[] args) {
        Plane plane = new Plane(new Vector3(1.0f, 0.0f, 0.0f), 0.0f);
        SplitTriangle split = new SplitTriangle(3);
        float[] fTriangle = {-10.0f, 0.0f, 10.0f, -1.0f, 0.0f, 0.0f, -10.0f, 0.0f, 10.0f};
        splitTriangle(fTriangle, plane, split);
        System.out.println(split);
        float[] triangle = {-10.0f, 0.0f, 10.0f, 10.0f, 0.0f, 0.0f, -10.0f, 0.0f, -10.0f};
        splitTriangle(triangle, plane, split);
        System.out.println(split);
    }

    public static class SplitTriangle {
        public float[] back;
        float[] edgeSplit;
        public float[] front;
        public int numBack;
        public int numFront;
        public int total;
        boolean frontCurrent = false;
        int frontOffset = 0;
        int backOffset = 0;

        public SplitTriangle(int numAttributes) {
            this.front = new float[numAttributes * 3 * 2];
            this.back = new float[numAttributes * 3 * 2];
            this.edgeSplit = new float[numAttributes];
        }

        public String toString() {
            return "SplitTriangle [front=" + Arrays.toString(this.front) + ", back=" + Arrays.toString(this.back) + ", numFront=" + this.numFront + ", numBack=" + this.numBack + ", total=" + this.total + "]";
        }

        void setSide(boolean front) {
            this.frontCurrent = front;
        }

        boolean getSide() {
            return this.frontCurrent;
        }

        void add(float[] vertex, int offset, int stride) {
            if (this.frontCurrent) {
                System.arraycopy(vertex, offset, this.front, this.frontOffset, stride);
                this.frontOffset += stride;
            } else {
                System.arraycopy(vertex, offset, this.back, this.backOffset, stride);
                this.backOffset += stride;
            }
        }

        void reset() {
            this.frontCurrent = false;
            this.frontOffset = 0;
            this.backOffset = 0;
            this.numFront = 0;
            this.numBack = 0;
            this.total = 0;
        }
    }
}
