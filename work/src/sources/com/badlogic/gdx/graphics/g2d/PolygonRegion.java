package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PolygonRegion {
    private float[] localVertices;
    private final TextureRegion region;
    private float[] texCoords;

    public PolygonRegion(TextureRegion region, FileHandle file) {
        this.region = region;
        if (file == null) {
            throw new IllegalArgumentException("region cannot be null.");
        }
        loadPolygonDefinition(file);
    }

    public PolygonRegion(TextureRegion region, float[] vertices) {
        this.region = region;
        EarClippingTriangulator ect = new EarClippingTriangulator();
        List<Vector2> polygonVectors = new ArrayList<>();
        for (int i = 0; i < vertices.length; i += 2) {
            polygonVectors.add(new Vector2(vertices[i], vertices[i + 1]));
        }
        List<Vector2> triangulatedVectors = ect.computeTriangles(polygonVectors);
        this.localVertices = new float[triangulatedVectors.size() * 2];
        this.texCoords = new float[triangulatedVectors.size() * 2];
        float f = region.u2 - region.u;
        float f2 = region.v2 - region.v;
        for (int i2 = 0; i2 < triangulatedVectors.size(); i2++) {
            this.localVertices[i2 * 2] = triangulatedVectors.get(i2).x;
            this.localVertices[(i2 * 2) + 1] = triangulatedVectors.get(i2).y;
            this.texCoords[i2 * 2] = region.u + ((this.localVertices[i2 * 2] - region.getRegionX()) / region.getRegionWidth());
            this.texCoords[(i2 * 2) + 1] = region.v + (1.0f - ((this.localVertices[(i2 * 2) + 1] - region.getRegionY()) / region.getRegionHeight()));
        }
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x00ab */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void loadPolygonDefinition(FileHandle file) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()), 64);
        while (true) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    if (line.startsWith("v")) {
                        String[] vertices = line.substring(1).trim().split(",");
                        this.localVertices = new float[vertices.length];
                        for (int i = 0; i < vertices.length; i += 2) {
                            this.localVertices[i] = Float.parseFloat(vertices[i]);
                            this.localVertices[i + 1] = Float.parseFloat(vertices[i + 1]);
                        }
                    } else if (line.startsWith("u")) {
                        String[] texCoords = line.substring(1).trim().split(",");
                        float[] localTexCoords = new float[texCoords.length];
                        for (int i2 = 0; i2 < texCoords.length; i2 += 2) {
                            localTexCoords[i2] = Float.parseFloat(texCoords[i2]);
                            localTexCoords[i2 + 1] = Float.parseFloat(texCoords[i2 + 1]);
                        }
                        this.texCoords = calculateAtlasTexCoords(localTexCoords);
                    }
                } else {
                    try {
                        reader.close();
                        return;
                    } catch (IOException e) {
                        return;
                    }
                }
            } catch (IOException e2) {
                throw new GdxRuntimeException("Error reading polygon shape file: " + file);
            }
        }
    }

    private float[] calculateAtlasTexCoords(float[] localTexCoords) {
        float uvWidth = this.region.u2 - this.region.u;
        float uvHeight = this.region.v2 - this.region.v;
        for (int i = 0; i < localTexCoords.length; i += 2) {
            localTexCoords[i] = this.region.u + (localTexCoords[i] * uvWidth);
            localTexCoords[i + 1] = this.region.v + (localTexCoords[i + 1] * uvHeight);
        }
        return localTexCoords;
    }

    public float[] getLocalVertices() {
        return this.localVertices;
    }

    public float[] getTextureCoords() {
        return this.texCoords;
    }

    public TextureRegion getRegion() {
        return this.region;
    }
}
