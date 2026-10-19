package com.badlogic.gdx.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class EarClippingTriangulator {
    private static final int CONCAVE = 1;
    private static final int CONVEX = -1;
    private int concaveVertexCount;

    public List<Vector2> computeTriangles(List<Vector2> polygon) {
        ArrayList<Vector2> triangles = new ArrayList<>();
        ArrayList<Vector2> vertices = new ArrayList<>(polygon.size());
        vertices.addAll(polygon);
        while (vertices.size() > 3) {
            int[] vertexTypes = classifyVertices(vertices);
            int vertexCount = vertices.size();
            for (int index = 0; index < vertexCount; index++) {
                if (isEarTip(vertices, index, vertexTypes)) {
                    cutEarTip(vertices, index, triangles);
                    break;
                }
            }
        }
        if (vertices.size() == 3) {
            triangles.addAll(vertices);
        }
        return triangles;
    }

    private static boolean areVerticesClockwise(ArrayList<Vector2> pVertices) {
        int vertexCount = pVertices.size();
        float area = 0.0f;
        for (int i = 0; i < vertexCount; i++) {
            Vector2 p1 = pVertices.get(i);
            Vector2 p2 = pVertices.get(computeNextIndex(pVertices, i));
            area += (p1.x * p2.y) - (p2.x * p1.y);
        }
        return area < 0.0f;
    }

    private int[] classifyVertices(ArrayList<Vector2> pVertices) {
        int vertexCount = pVertices.size();
        int[] vertexTypes = new int[vertexCount];
        this.concaveVertexCount = 0;
        if (!areVerticesClockwise(pVertices)) {
            Collections.reverse(pVertices);
        }
        for (int index = 0; index < vertexCount; index++) {
            int previousIndex = computePreviousIndex(pVertices, index);
            int nextIndex = computeNextIndex(pVertices, index);
            Vector2 previousVertex = pVertices.get(previousIndex);
            Vector2 currentVertex = pVertices.get(index);
            Vector2 nextVertex = pVertices.get(nextIndex);
            if (isTriangleConvex(previousVertex.x, previousVertex.y, currentVertex.x, currentVertex.y, nextVertex.x, nextVertex.y)) {
                vertexTypes[index] = -1;
            } else {
                vertexTypes[index] = 1;
                this.concaveVertexCount++;
            }
        }
        return vertexTypes;
    }

    private static boolean isTriangleConvex(float pX1, float pY1, float pX2, float pY2, float pX3, float pY3) {
        return computeSpannedAreaSign(pX1, pY1, pX2, pY2, pX3, pY3) >= 0;
    }

    private static int computeSpannedAreaSign(float pX1, float pY1, float pX2, float pY2, float pX3, float pY3) {
        double area = 0.0d + (((double) pX1) * ((double) (pY3 - pY2)));
        return (int) Math.signum(area + (((double) pX2) * ((double) (pY1 - pY3))) + (((double) pX3) * ((double) (pY2 - pY1))));
    }

    private static boolean isAnyVertexInTriangle(ArrayList<Vector2> pVertices, int[] pVertexTypes, float pX1, float pY1, float pX2, float pY2, float pX3, float pY3) {
        int vertexCount = pVertices.size();
        for (int i = 0; i < vertexCount - 1; i++) {
            if (pVertexTypes[i] == 1) {
                Vector2 currentVertex = pVertices.get(i);
                float currentVertexX = currentVertex.x;
                float currentVertexY = currentVertex.y;
                int areaSign1 = computeSpannedAreaSign(pX1, pY1, pX2, pY2, currentVertexX, currentVertexY);
                int areaSign2 = computeSpannedAreaSign(pX2, pY2, pX3, pY3, currentVertexX, currentVertexY);
                int areaSign3 = computeSpannedAreaSign(pX3, pY3, pX1, pY1, currentVertexX, currentVertexY);
                if (areaSign1 > 0 && areaSign2 > 0 && areaSign3 > 0) {
                    return true;
                }
                if (areaSign1 <= 0 && areaSign2 <= 0 && areaSign3 <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isEarTip(ArrayList<Vector2> pVertices, int pEarTipIndex, int[] pVertexTypes) {
        if (this.concaveVertexCount == 0) {
            return true;
        }
        Vector2 previousVertex = pVertices.get(computePreviousIndex(pVertices, pEarTipIndex));
        Vector2 currentVertex = pVertices.get(pEarTipIndex);
        Vector2 nextVertex = pVertices.get(computeNextIndex(pVertices, pEarTipIndex));
        return !isAnyVertexInTriangle(pVertices, pVertexTypes, previousVertex.x, previousVertex.y, currentVertex.x, currentVertex.y, nextVertex.x, nextVertex.y);
    }

    private void cutEarTip(ArrayList<Vector2> pVertices, int pEarTipIndex, ArrayList<Vector2> pTriangles) {
        int previousIndex = computePreviousIndex(pVertices, pEarTipIndex);
        int nextIndex = computeNextIndex(pVertices, pEarTipIndex);
        if (!isCollinear(pVertices, previousIndex, pEarTipIndex, nextIndex)) {
            pTriangles.add(new Vector2(pVertices.get(previousIndex)));
            pTriangles.add(new Vector2(pVertices.get(pEarTipIndex)));
            pTriangles.add(new Vector2(pVertices.get(nextIndex)));
        }
        pVertices.remove(pEarTipIndex);
        if (pVertices.size() >= 3) {
            removeCollinearNeighborEarsAfterRemovingEarTip(pVertices, pEarTipIndex);
        }
    }

    private static void removeCollinearNeighborEarsAfterRemovingEarTip(ArrayList<Vector2> pVertices, int pEarTipCutIndex) {
        int collinearityCheckNextIndex = pEarTipCutIndex % pVertices.size();
        int collinearCheckPreviousIndex = computePreviousIndex(pVertices, collinearityCheckNextIndex);
        if (isCollinear(pVertices, collinearityCheckNextIndex)) {
            pVertices.remove(collinearityCheckNextIndex);
            if (pVertices.size() > 3) {
                int collinearCheckPreviousIndex2 = computePreviousIndex(pVertices, collinearityCheckNextIndex);
                if (isCollinear(pVertices, collinearCheckPreviousIndex2)) {
                    pVertices.remove(collinearCheckPreviousIndex2);
                    return;
                }
                return;
            }
            return;
        }
        if (isCollinear(pVertices, collinearCheckPreviousIndex)) {
            pVertices.remove(collinearCheckPreviousIndex);
        }
    }

    private static boolean isCollinear(ArrayList<Vector2> pVertices, int pIndex) {
        int previousIndex = computePreviousIndex(pVertices, pIndex);
        int nextIndex = computeNextIndex(pVertices, pIndex);
        return isCollinear(pVertices, previousIndex, pIndex, nextIndex);
    }

    private static boolean isCollinear(ArrayList<Vector2> pVertices, int pPreviousIndex, int pIndex, int pNextIndex) {
        Vector2 previousVertex = pVertices.get(pPreviousIndex);
        Vector2 vertex = pVertices.get(pIndex);
        Vector2 nextVertex = pVertices.get(pNextIndex);
        return computeSpannedAreaSign(previousVertex.x, previousVertex.y, vertex.x, vertex.y, nextVertex.x, nextVertex.y) == 0;
    }

    private static int computePreviousIndex(List<Vector2> pVertices, int pIndex) {
        return pIndex == 0 ? pVertices.size() - 1 : pIndex - 1;
    }

    private static int computeNextIndex(List<Vector2> pVertices, int pIndex) {
        if (pIndex == pVertices.size() - 1) {
            return 0;
        }
        return pIndex + 1;
    }
}
