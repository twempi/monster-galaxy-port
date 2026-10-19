package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class PixmapPacker implements Disposable {
    Page currPage;
    boolean disposed;
    final boolean duplicateBorder;
    final int padding;
    final Pixmap.Format pageFormat;
    final int pageHeight;
    final int pageWidth;
    final Array<Page> pages = new Array<>();

    static final class Node {
        public String leaveName;
        public Node leftChild;
        public Rectangle rect;
        public Node rightChild;

        public Node(int x, int y, int width, int height, Node leftChild, Node rightChild, String leaveName) {
            this.rect = new Rectangle(x, y, width, height);
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.leaveName = leaveName;
        }

        public Node() {
            this.rect = new Rectangle();
        }
    }

    public class Page {
        Array<String> addedRects = new Array<>();
        Pixmap image;
        OrderedMap<String, Rectangle> rects;
        Node root;
        Texture texture;

        public Page() {
        }

        public Pixmap getPixmap() {
            return this.image;
        }
    }

    public PixmapPacker(int width, int height, Pixmap.Format format, int padding, boolean duplicateBorder) {
        this.pageWidth = width;
        this.pageHeight = height;
        this.pageFormat = format;
        this.padding = padding;
        this.duplicateBorder = duplicateBorder;
        newPage();
    }

    public synchronized Rectangle pack(String name, Pixmap image) {
        Rectangle rect;
        if (this.disposed) {
            rect = null;
        } else {
            if (getRect(name) != null) {
                throw new RuntimeException("Key with name '" + name + "' is already in map");
            }
            int borderPixels = (this.padding + (this.duplicateBorder ? 1 : 0)) << 1;
            if (image.getWidth() >= this.pageWidth + borderPixels || image.getHeight() >= this.pageHeight + borderPixels) {
                throw new GdxRuntimeException("page size for '" + name + "' to small");
            }
            Node node = insert(this.currPage.root, new Rectangle(0.0f, 0.0f, image.getWidth() + borderPixels, image.getHeight() + borderPixels));
            if (node == null) {
                newPage();
                rect = pack(name, image);
            } else {
                node.leaveName = name;
                rect = new Rectangle(node.rect);
                rect.width -= borderPixels;
                rect.height -= borderPixels;
                int borderPixels2 = borderPixels >> 1;
                rect.x += borderPixels2;
                rect.y += borderPixels2;
                this.currPage.rects.put(name, rect);
                Pixmap.Blending blending = Pixmap.getBlending();
                Pixmap.setBlending(Pixmap.Blending.None);
                this.currPage.image.drawPixmap(image, (int) rect.x, (int) rect.y);
                Pixmap.setBlending(blending);
                if (this.duplicateBorder) {
                    this.currPage.image.drawPixmap(image, (int) rect.x, ((int) rect.y) - 1, ((int) rect.width) + ((int) rect.x), (int) rect.y, 0, 0, image.getWidth(), 1);
                    this.currPage.image.drawPixmap(image, (int) rect.x, ((int) rect.height) + ((int) rect.y), ((int) rect.width) + ((int) rect.x), ((int) rect.y) + ((int) rect.height) + 1, 0, image.getHeight() - 1, image.getWidth(), image.getHeight());
                    this.currPage.image.drawPixmap(image, ((int) rect.x) - 1, (int) rect.y, (int) rect.x, ((int) rect.height) + ((int) rect.y), 0, 0, 1, image.getHeight());
                    this.currPage.image.drawPixmap(image, ((int) rect.width) + ((int) rect.x), (int) rect.y, ((int) rect.x) + ((int) rect.width) + 1, ((int) rect.height) + ((int) rect.y), image.getWidth() - 1, 0, image.getWidth(), image.getHeight());
                    this.currPage.image.drawPixmap(image, ((int) rect.x) - 1, ((int) rect.y) - 1, (int) rect.x, (int) rect.y, 0, 0, 1, 1);
                    this.currPage.image.drawPixmap(image, ((int) rect.width) + ((int) rect.x), ((int) rect.y) - 1, ((int) rect.x) + ((int) rect.width) + 1, (int) rect.y, image.getWidth() - 1, 0, image.getWidth(), 1);
                    this.currPage.image.drawPixmap(image, ((int) rect.x) - 1, ((int) rect.height) + ((int) rect.y), (int) rect.x, ((int) rect.y) + ((int) rect.height) + 1, 0, image.getHeight() - 1, 1, image.getHeight());
                    this.currPage.image.drawPixmap(image, ((int) rect.width) + ((int) rect.x), ((int) rect.height) + ((int) rect.y), ((int) rect.x) + ((int) rect.width) + 1, ((int) rect.y) + ((int) rect.height) + 1, image.getWidth() - 1, image.getHeight() - 1, image.getWidth(), image.getHeight());
                }
                this.currPage.addedRects.add(name);
            }
        }
        return rect;
    }

    private void newPage() {
        Page page = new Page();
        page.image = new Pixmap(this.pageWidth, this.pageHeight, this.pageFormat);
        page.root = new Node(0, 0, this.pageWidth, this.pageHeight, null, null, null);
        page.rects = new OrderedMap<>();
        this.pages.add(page);
        this.currPage = page;
    }

    private Node insert(Node node, Rectangle rect) {
        if (node.leaveName == null && node.leftChild != null && node.rightChild != null) {
            Node newNode = insert(node.leftChild, rect);
            if (newNode == null) {
                return insert(node.rightChild, rect);
            }
            return newNode;
        }
        if (node.leaveName != null) {
            return null;
        }
        if (node.rect.width == rect.width && node.rect.height == rect.height) {
            return node;
        }
        if (node.rect.width < rect.width || node.rect.height < rect.height) {
            return null;
        }
        node.leftChild = new Node();
        node.rightChild = new Node();
        int deltaWidth = ((int) node.rect.width) - ((int) rect.width);
        int deltaHeight = ((int) node.rect.height) - ((int) rect.height);
        if (deltaWidth > deltaHeight) {
            node.leftChild.rect.x = node.rect.x;
            node.leftChild.rect.y = node.rect.y;
            node.leftChild.rect.width = rect.width;
            node.leftChild.rect.height = node.rect.height;
            node.rightChild.rect.x = node.rect.x + rect.width;
            node.rightChild.rect.y = node.rect.y;
            node.rightChild.rect.width = node.rect.width - rect.width;
            node.rightChild.rect.height = node.rect.height;
        } else {
            node.leftChild.rect.x = node.rect.x;
            node.leftChild.rect.y = node.rect.y;
            node.leftChild.rect.width = node.rect.width;
            node.leftChild.rect.height = rect.height;
            node.rightChild.rect.x = node.rect.x;
            node.rightChild.rect.y = node.rect.y + rect.height;
            node.rightChild.rect.width = node.rect.width;
            node.rightChild.rect.height = node.rect.height - rect.height;
        }
        return insert(node.leftChild, rect);
    }

    public Array<Page> getPages() {
        return this.pages;
    }

    public synchronized Rectangle getRect(String name) {
        Rectangle rect;
        for (Page page : this.pages) {
            rect = page.rects.get(name);
            if (rect != null) {
            }
        }
        rect = null;
        return rect;
    }

    public synchronized Page getPage(String name) {
        Page page;
        Iterator<Page> it = this.pages.iterator();
        while (it.hasNext()) {
            page = it.next();
            Rectangle rect = page.rects.get(name);
            if (rect != null) {
            }
        }
        page = null;
        return page;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public synchronized void dispose() {
        for (Page page : this.pages) {
            page.image.dispose();
        }
        this.disposed = true;
    }

    public synchronized TextureAtlas generateTextureAtlas(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
        TextureAtlas atlas;
        atlas = new TextureAtlas();
        for (Page page : this.pages) {
            if (page.rects.size != 0) {
                Texture texture = new Texture(new ManagedPixmapTextureData(page.image, page.image.getFormat(), useMipMaps)) { // from class: com.badlogic.gdx.graphics.g2d.PixmapPacker.1
                    @Override // com.badlogic.gdx.graphics.Texture, com.badlogic.gdx.utils.Disposable
                    public void dispose() {
                        super.dispose();
                        getTextureData().consumePixmap().dispose();
                    }
                };
                texture.setFilter(minFilter, magFilter);
                ObjectMap.Keys<String> names = page.rects.keys();
                for (String name : names) {
                    Rectangle rect = page.rects.get(name);
                    TextureRegion region = new TextureRegion(texture, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
                    atlas.addRegion(name, region);
                }
            }
        }
        return atlas;
    }

    public synchronized void updateTextureAtlas(TextureAtlas atlas, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
        for (Page page : this.pages) {
            if (page.texture == null) {
                if (page.rects.size != 0 && page.addedRects.size > 0) {
                    page.texture = new Texture(new ManagedPixmapTextureData(page.image, page.image.getFormat(), useMipMaps)) { // from class: com.badlogic.gdx.graphics.g2d.PixmapPacker.2
                        @Override // com.badlogic.gdx.graphics.Texture, com.badlogic.gdx.utils.Disposable
                        public void dispose() {
                            super.dispose();
                            getTextureData().consumePixmap().dispose();
                        }
                    };
                    page.texture.setFilter(minFilter, magFilter);
                    for (String name : page.addedRects) {
                        Rectangle rect = page.rects.get(name);
                        TextureRegion region = new TextureRegion(page.texture, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
                        atlas.addRegion(name, region);
                    }
                    page.addedRects.clear();
                }
            } else if (page.addedRects.size > 0) {
                page.texture.load(page.texture.getTextureData());
                for (String name2 : page.addedRects) {
                    Rectangle rect2 = page.rects.get(name2);
                    TextureRegion region2 = new TextureRegion(page.texture, (int) rect2.x, (int) rect2.y, (int) rect2.width, (int) rect2.height);
                    atlas.addRegion(name2, region2);
                }
                page.addedRects.clear();
                break;
            }
        }
    }

    public int getPageWidth() {
        return this.pageWidth;
    }

    public int getPageHeight() {
        return this.pageHeight;
    }

    public int getPadding() {
        return this.padding;
    }

    public boolean duplicateBoarder() {
        return this.duplicateBorder;
    }

    public class ManagedPixmapTextureData extends PixmapTextureData {
        public ManagedPixmapTextureData(Pixmap pixmap, Pixmap.Format format, boolean useMipMaps) {
            super(pixmap, format, useMipMaps, false);
        }

        @Override // com.badlogic.gdx.graphics.glutils.PixmapTextureData, com.badlogic.gdx.graphics.TextureData
        public boolean isManaged() {
            return true;
        }
    }
}
