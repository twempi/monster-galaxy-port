package com.badlogic.gdx.graphics.g2d.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.millennialmedia.android.MMAdView;
import com.sessionm.ui.SessionMActivity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

/* JADX INFO: loaded from: classes.dex */
public class TiledLoader {
    public static TiledMap createMap(String tmxData) {
        return createMap(null, tmxData);
    }

    public static TiledMap createMap(FileHandle tmxFile) {
        return createMap(tmxFile, null);
    }

    private static TiledMap createMap(FileHandle tmxFile, String tmxData) {
        final TiledMap map = new TiledMap();
        map.tmxFile = tmxFile;
        try {
            XmlReader xmlReader = new XmlReader() { // from class: com.badlogic.gdx.graphics.g2d.tiled.TiledLoader.1
                int col;
                String compression;
                TiledLayer currLayer;
                TiledObject currObject;
                TiledObjectGroup currObjectGroup;
                Property currProperty;
                int currTile;
                TileSet currTileSet;
                byte[] data;
                String dataString;
                String encoding;
                Polyline polygon;
                Polyline polyline;
                int row;
                Stack<String> currBranch = new Stack<>();
                boolean awaitingData = false;
                int currLayerWidth = 0;
                int currLayerHeight = 0;
                int dataCounter = 0;

                /* JADX INFO: renamed from: com.badlogic.gdx.graphics.g2d.tiled.TiledLoader$1$Polyline */
                class Polyline {
                    String name;
                    String points;

                    public Polyline(String name) {
                        this.name = name;
                    }

                    public Polyline() {
                    }
                }

                /* JADX INFO: renamed from: com.badlogic.gdx.graphics.g2d.tiled.TiledLoader$1$Property */
                class Property {
                    String name;
                    String parentType;
                    String value;

                    Property() {
                    }
                }

                @Override // com.badlogic.gdx.utils.XmlReader
                protected void open(String name) {
                    this.currBranch.push(name);
                    if ("layer".equals(name)) {
                        this.currLayer = new TiledLayer();
                        return;
                    }
                    if ("tileset".equals(name)) {
                        this.currTileSet = new TileSet();
                        return;
                    }
                    if ("data".equals(name)) {
                        this.dataString = "";
                        this.awaitingData = true;
                        return;
                    }
                    if ("objectgroup".equals(name)) {
                        this.currObjectGroup = new TiledObjectGroup();
                        return;
                    }
                    if ("object".equals(name)) {
                        this.currObject = new TiledObject();
                        return;
                    }
                    if ("property".equals(name)) {
                        this.currProperty = new Property();
                        this.currProperty.parentType = this.currBranch.get(this.currBranch.size() - 3);
                    } else if ("polyline".equals(name)) {
                        this.polyline = new Polyline("polyline");
                    } else if ("polygon".equals(name)) {
                        this.polygon = new Polyline("polygon");
                    }
                }

                @Override // com.badlogic.gdx.utils.XmlReader
                protected void attribute(String name, String value) {
                    String element = this.currBranch.peek();
                    if ("layer".equals(element)) {
                        if ("width".equals(name)) {
                            this.currLayerWidth = Integer.parseInt(value);
                        } else if ("height".equals(name)) {
                            this.currLayerHeight = Integer.parseInt(value);
                        }
                        if (this.currLayerWidth != 0 && this.currLayerHeight != 0) {
                            this.currLayer.tiles = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, this.currLayerHeight, this.currLayerWidth);
                        }
                        if ("name".equals(name)) {
                            this.currLayer.name = value;
                            return;
                        }
                        return;
                    }
                    if ("tileset".equals(element)) {
                        if ("firstgid".equals(name)) {
                            this.currTileSet.firstgid = Integer.parseInt(value);
                            return;
                        }
                        if ("tilewidth".equals(name)) {
                            this.currTileSet.tileWidth = Integer.parseInt(value);
                            return;
                        }
                        if ("tileheight".equals(name)) {
                            this.currTileSet.tileHeight = Integer.parseInt(value);
                            return;
                        }
                        if ("name".equals(name)) {
                            this.currTileSet.name = value;
                            return;
                        }
                        if ("spacing".equals(name)) {
                            this.currTileSet.spacing = Integer.parseInt(value);
                            return;
                        } else {
                            if ("margin".equals(name)) {
                                this.currTileSet.margin = Integer.parseInt(value);
                                return;
                            }
                            return;
                        }
                    }
                    if ("image".equals(element)) {
                        if ("source".equals(name)) {
                            this.currTileSet.imageName = value;
                            return;
                        }
                        return;
                    }
                    if ("data".equals(element)) {
                        if ("encoding".equals(name)) {
                            this.encoding = value;
                            return;
                        } else {
                            if ("compression".equals(name)) {
                                this.compression = value;
                                return;
                            }
                            return;
                        }
                    }
                    if ("objectgroup".equals(element)) {
                        if ("name".equals(name)) {
                            this.currObjectGroup.name = value;
                            return;
                        }
                        if ("height".equals(name)) {
                            this.currObjectGroup.height = Integer.parseInt(value);
                            return;
                        } else {
                            if ("width".equals(name)) {
                                this.currObjectGroup.width = Integer.parseInt(value);
                                return;
                            }
                            return;
                        }
                    }
                    if ("object".equals(element)) {
                        if ("name".equals(name)) {
                            this.currObject.name = value;
                            return;
                        }
                        if (SessionMActivity.INTENT_PARAM_TYPE.equals(name)) {
                            this.currObject.type = value;
                            return;
                        }
                        if ("x".equals(name)) {
                            this.currObject.x = Integer.parseInt(value);
                            return;
                        }
                        if ("y".equals(name)) {
                            this.currObject.y = Integer.parseInt(value);
                            return;
                        }
                        if ("width".equals(name)) {
                            this.currObject.width = Integer.parseInt(value);
                            return;
                        } else if ("height".equals(name)) {
                            this.currObject.height = Integer.parseInt(value);
                            return;
                        } else {
                            if ("gid".equals(name)) {
                                this.currObject.gid = Integer.parseInt(value);
                                return;
                            }
                            return;
                        }
                    }
                    if ("map".equals(element)) {
                        if (MMAdView.KEY_ORIENTATION.equals(name)) {
                            map.orientation = value;
                            return;
                        }
                        if ("width".equals(name)) {
                            map.width = Integer.parseInt(value);
                            return;
                        }
                        if ("height".equals(name)) {
                            map.height = Integer.parseInt(value);
                            return;
                        } else if ("tilewidth".equals(name)) {
                            map.tileWidth = Integer.parseInt(value);
                            return;
                        } else {
                            if ("tileheight".equals(name)) {
                                map.tileHeight = Integer.parseInt(value);
                                return;
                            }
                            return;
                        }
                    }
                    if ("tile".equals(element)) {
                        if (this.awaitingData) {
                            if ("gid".equals(name)) {
                                this.col = this.dataCounter % this.currLayerWidth;
                                this.row = this.dataCounter / this.currLayerWidth;
                                if (this.row < this.currLayerHeight) {
                                    this.currLayer.tiles[this.row][this.col] = Integer.parseInt(value);
                                } else {
                                    Gdx.app.log("TiledLoader", "Warning: extra XML gid values ignored! Your map is likely corrupt!");
                                }
                                this.dataCounter++;
                                return;
                            }
                            return;
                        }
                        if ("id".equals(name)) {
                            this.currTile = Integer.parseInt(value);
                            return;
                        }
                        return;
                    }
                    if ("property".equals(element)) {
                        if ("name".equals(name)) {
                            this.currProperty.name = value;
                            return;
                        } else {
                            if ("value".equals(name)) {
                                this.currProperty.value = value;
                                return;
                            }
                            return;
                        }
                    }
                    if ("polyline".equals(element)) {
                        if ("points".equals(name)) {
                            this.polyline.points = value;
                        }
                    } else if ("polygon".equals(element) && "points".equals(name)) {
                        this.polygon.points = value;
                    }
                }

                @Override // com.badlogic.gdx.utils.XmlReader
                protected void text(String text) {
                    if (this.awaitingData) {
                        this.dataString = this.dataString.concat(text);
                    }
                }

                @Override // com.badlogic.gdx.utils.XmlReader
                protected void close() {
                    String element = this.currBranch.pop();
                    if ("layer".equals(element)) {
                        map.layers.add(this.currLayer);
                        this.currLayer = null;
                        return;
                    }
                    if ("tileset".equals(element)) {
                        map.tileSets.add(this.currTileSet);
                        this.currTileSet = null;
                        return;
                    }
                    if ("object".equals(element)) {
                        this.currObjectGroup.objects.add(this.currObject);
                        this.currObject = null;
                        return;
                    }
                    if ("objectgroup".equals(element)) {
                        map.objectGroups.add(this.currObjectGroup);
                        this.currObjectGroup = null;
                        return;
                    }
                    if ("property".equals(element)) {
                        putProperty(this.currProperty);
                        this.currProperty = null;
                        return;
                    }
                    if ("polyline".equals(element)) {
                        putPolyLine(this.polyline);
                        this.polyline = null;
                        return;
                    }
                    if ("polygon".equals(element)) {
                        putPolyLine(this.polygon);
                        this.polygon = null;
                        return;
                    }
                    if ("data".equals(element)) {
                        if ("base64".equals(this.encoding)) {
                            if (!((this.dataString == null) | "".equals(this.dataString.trim()))) {
                                this.data = Base64Coder.decode(this.dataString.trim());
                                if ("gzip".equals(this.compression)) {
                                    unGZip();
                                } else if ("zlib".equals(this.compression)) {
                                    unZlib();
                                } else if (this.compression == null) {
                                    arrangeData();
                                }
                            } else {
                                return;
                            }
                        } else if ("csv".equals(this.encoding) && this.compression == null) {
                            fromCSV();
                        } else if (this.encoding == null && this.compression == null) {
                            this.dataCounter = 0;
                        } else {
                            throw new GdxRuntimeException("Unsupported encoding and/or compression format");
                        }
                        this.awaitingData = false;
                        return;
                    }
                    if ("property".equals(element)) {
                        putProperty(this.currProperty);
                        this.currProperty = null;
                    }
                }

                private void putPolyLine(Polyline polyLine) {
                    if (polyLine != null) {
                        if ("polyline".equals(polyLine.name)) {
                            this.currObject.polyline = polyLine.points;
                        } else if ("polygon".equals(polyLine.name)) {
                            this.currObject.polygon = polyLine.points;
                        }
                    }
                }

                private void putProperty(Property property) {
                    if ("tile".equals(property.parentType)) {
                        map.setTileProperty(this.currTile + this.currTileSet.firstgid, property.name, property.value);
                        return;
                    }
                    if ("map".equals(property.parentType)) {
                        map.properties.put(property.name, property.value);
                        return;
                    }
                    if ("layer".equals(property.parentType)) {
                        this.currLayer.properties.put(property.name, property.value);
                    } else if ("objectgroup".equals(property.parentType)) {
                        this.currObjectGroup.properties.put(property.name, property.value);
                    } else if ("object".equals(property.parentType)) {
                        this.currObject.properties.put(property.name, property.value);
                    }
                }

                private void fromCSV() {
                    StringTokenizer st = new StringTokenizer(this.dataString.trim(), ",");
                    for (int row = 0; row < this.currLayerHeight; row++) {
                        for (int col = 0; col < this.currLayerWidth; col++) {
                            this.currLayer.tiles[row][col] = (int) Long.parseLong(st.nextToken().trim());
                        }
                    }
                }

                private void arrangeData() {
                    int byteCounter = 0;
                    for (int row = 0; row < this.currLayerHeight; row++) {
                        for (int col = 0; col < this.currLayerWidth; col++) {
                            int[] iArr = this.currLayer.tiles[row];
                            int byteCounter2 = byteCounter + 1;
                            int iUnsignedByteToInt = TiledLoader.unsignedByteToInt(this.data[byteCounter]);
                            int byteCounter3 = byteCounter2 + 1;
                            int iUnsignedByteToInt2 = iUnsignedByteToInt | (TiledLoader.unsignedByteToInt(this.data[byteCounter2]) << 8);
                            int byteCounter4 = byteCounter3 + 1;
                            int iUnsignedByteToInt3 = iUnsignedByteToInt2 | (TiledLoader.unsignedByteToInt(this.data[byteCounter3]) << 16);
                            byteCounter = byteCounter4 + 1;
                            iArr[col] = iUnsignedByteToInt3 | (TiledLoader.unsignedByteToInt(this.data[byteCounter4]) << 24);
                        }
                    }
                }

                private void unZlib() {
                    Inflater zlib = new Inflater();
                    byte[] readTemp = new byte[4];
                    zlib.setInput(this.data, 0, this.data.length);
                    for (int row = 0; row < this.currLayerHeight; row++) {
                        for (int col = 0; col < this.currLayerWidth; col++) {
                            try {
                                zlib.inflate(readTemp, 0, 4);
                                this.currLayer.tiles[row][col] = TiledLoader.unsignedByteToInt(readTemp[0]) | (TiledLoader.unsignedByteToInt(readTemp[1]) << 8) | (TiledLoader.unsignedByteToInt(readTemp[2]) << 16) | (TiledLoader.unsignedByteToInt(readTemp[3]) << 24);
                            } catch (DataFormatException e) {
                                throw new GdxRuntimeException("Error Reading TMX Layer Data.", e);
                            }
                        }
                    }
                }

                private void unGZip() {
                    try {
                        GZIPInputStream GZIS = new GZIPInputStream(new ByteArrayInputStream(this.data), this.data.length);
                        byte[] readTemp = new byte[4];
                        for (int row = 0; row < this.currLayerHeight; row++) {
                            for (int col = 0; col < this.currLayerWidth; col++) {
                                try {
                                    GZIS.read(readTemp, 0, 4);
                                    this.currLayer.tiles[row][col] = TiledLoader.unsignedByteToInt(readTemp[0]) | (TiledLoader.unsignedByteToInt(readTemp[1]) << 8) | (TiledLoader.unsignedByteToInt(readTemp[2]) << 16) | (TiledLoader.unsignedByteToInt(readTemp[3]) << 24);
                                } catch (IOException e) {
                                    throw new GdxRuntimeException("Error Reading TMX Layer Data.", e);
                                }
                            }
                        }
                    } catch (IOException e2) {
                        throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e2.getMessage());
                    }
                }
            };
            if (tmxFile != null) {
                xmlReader.parse(tmxFile);
            } else {
                xmlReader.parse(tmxData);
            }
            return map;
        } catch (IOException e) {
            throw new GdxRuntimeException("Error Parsing TMX file", e);
        }
    }

    static int unsignedByteToInt(byte b) {
        return b & 255;
    }
}
