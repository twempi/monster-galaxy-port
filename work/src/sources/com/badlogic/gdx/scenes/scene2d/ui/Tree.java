package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

/* JADX INFO: loaded from: classes.dex */
public class Tree extends WidgetGroup {
    private ClickListener clickListener;
    private Node foundNode;
    float iconSpacing;
    float indentSpacing;
    private float leftColumnWidth;
    boolean multiSelect;
    Node overNode;
    float padding;
    private float prefHeight;
    private float prefWidth;
    final Array<Node> rootNodes;
    final Array<Node> selectedNodes;
    private boolean sizeInvalid;
    TreeStyle style;
    float ySpacing;

    public Tree(Skin skin) {
        this((TreeStyle) skin.get(TreeStyle.class));
    }

    public Tree(Skin skin, String styleName) {
        this((TreeStyle) skin.get(styleName, TreeStyle.class));
    }

    public Tree(TreeStyle style) {
        this.rootNodes = new Array<>();
        this.selectedNodes = new Array<>();
        this.ySpacing = 4.0f;
        this.iconSpacing = 2.0f;
        this.padding = 0.0f;
        this.sizeInvalid = true;
        this.multiSelect = true;
        setStyle(style);
        initialize();
    }

    private void initialize() {
        ClickListener clickListener = new ClickListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Tree.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener
            public void clicked(InputEvent event, float x, float y) {
                Node node = Tree.this.getNodeAt(y);
                if (node != null && node == Tree.this.getNodeAt(getTouchDownY())) {
                    if (Tree.this.multiSelect && Tree.this.selectedNodes.size > 0 && (Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60))) {
                        float low = Tree.this.selectedNodes.first().actor.getY();
                        float high = node.actor.getY();
                        if (!Gdx.input.isKeyPressed(129) && !Gdx.input.isKeyPressed(130)) {
                            Tree.this.selectedNodes.clear();
                        }
                        if (low > high) {
                            Tree.this.selectNodes(Tree.this.rootNodes, high, low);
                        } else {
                            Tree.this.selectNodes(Tree.this.rootNodes, low, high);
                        }
                        ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
                        Tree.this.fire(changeEvent);
                        Pools.free(changeEvent);
                        return;
                    }
                    if (!Tree.this.multiSelect || (!Gdx.input.isKeyPressed(129) && !Gdx.input.isKeyPressed(130))) {
                        if (node.children.size > 0) {
                            float rowX = node.actor.getX();
                            if (node.icon != null) {
                                rowX -= Tree.this.iconSpacing + node.icon.getMinWidth();
                            }
                            if (x < rowX) {
                                node.setExpanded(node.expanded ? false : true);
                                return;
                            }
                        }
                        if (node.isSelectable()) {
                            Tree.this.selectedNodes.clear();
                        } else {
                            return;
                        }
                    } else if (!node.isSelectable()) {
                        return;
                    }
                    if (!Tree.this.selectedNodes.removeValue(node, true)) {
                        Tree.this.selectedNodes.add(node);
                    }
                    ChangeListener.ChangeEvent changeEvent2 = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
                    Tree.this.fire(changeEvent2);
                    Pools.free(changeEvent2);
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean mouseMoved(InputEvent event, float x, float y) {
                Tree.this.setOverNode(Tree.this.getNodeAt(y));
                return false;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener, com.badlogic.gdx.scenes.scene2d.InputListener
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (toActor == null || !toActor.isDescendantOf(Tree.this)) {
                    Tree.this.setOverNode(null);
                }
            }
        };
        this.clickListener = clickListener;
        addListener(clickListener);
    }

    public void setStyle(TreeStyle style) {
        this.style = style;
        this.indentSpacing = Math.max(style.plus.getMinWidth(), style.minus.getMinWidth()) + this.iconSpacing;
    }

    public void add(Node node) {
        insert(this.rootNodes.size, node);
    }

    public void insert(int index, Node node) {
        remove(node);
        node.parent = null;
        this.rootNodes.insert(index, node);
        node.addToTree(this);
        invalidateHierarchy();
    }

    public void remove(Node node) {
        if (node.parent != null) {
            node.parent.remove(node);
            return;
        }
        this.rootNodes.removeValue(node, true);
        node.removeFromTree(this);
        invalidateHierarchy();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void clear() {
        super.clear();
        this.rootNodes.clear();
        this.selectedNodes.clear();
        setOverNode(null);
    }

    public Array<Node> getNodes() {
        return this.rootNodes;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void invalidate() {
        super.invalidate();
        this.sizeInvalid = true;
    }

    private void computeSize() {
        this.sizeInvalid = false;
        this.prefWidth = this.style.plus.getMinWidth();
        this.prefWidth = Math.max(this.prefWidth, this.style.minus.getMinWidth());
        this.prefHeight = getHeight();
        this.leftColumnWidth = 0.0f;
        computeSize(this.rootNodes, this.indentSpacing);
        this.leftColumnWidth += this.iconSpacing + this.padding;
        this.prefWidth += this.leftColumnWidth + this.padding;
        this.prefHeight = getHeight() - this.prefHeight;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void computeSize(Array<Node> nodes, float indent) {
        float rowWidth;
        float ySpacing = this.ySpacing;
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            float rowWidth2 = indent + this.iconSpacing;
            Actor actor = node.actor;
            if (actor instanceof Layout) {
                Layout layout = (Layout) actor;
                rowWidth = rowWidth2 + layout.getPrefWidth();
                node.height = layout.getPrefHeight();
                layout.pack();
            } else {
                rowWidth = rowWidth2 + actor.getWidth();
                node.height = actor.getHeight();
            }
            if (node.icon != null) {
                rowWidth += (this.iconSpacing * 2.0f) + node.icon.getMinWidth();
                node.height = Math.max(node.height, node.icon.getMinHeight());
            }
            this.prefWidth = Math.max(this.prefWidth, rowWidth);
            this.prefHeight -= node.height + ySpacing;
            if (node.expanded) {
                computeSize(node.children, this.indentSpacing + indent);
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        if (this.sizeInvalid) {
            computeSize();
        }
        layout(this.rootNodes, this.leftColumnWidth + this.indentSpacing, getHeight() - (this.ySpacing / 2.0f));
    }

    private float layout(Array<Node> nodes, float indent, float y) {
        float ySpacing = this.ySpacing;
        Drawable drawable = this.style.plus;
        Drawable drawable2 = this.style.minus;
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            Actor actor = node.actor;
            float x = indent;
            if (node.icon != null) {
                x += node.icon.getMinWidth();
            }
            float y2 = y - node.height;
            node.actor.setPosition(x, y2);
            y = y2 - ySpacing;
            if (node.expanded) {
                y = layout(node.children, this.indentSpacing + indent, y);
            }
        }
        return y;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Color color = getColor();
        if (this.style.background != null) {
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            this.style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
            batch.setColor(Color.WHITE);
        }
        draw(batch, this.rootNodes, this.leftColumnWidth);
        super.draw(batch, parentAlpha);
    }

    private void draw(SpriteBatch batch, Array<Node> nodes, float indent) {
        Drawable plus = this.style.plus;
        Drawable minus = this.style.minus;
        float x = getX();
        float y = getY();
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            Actor actor = node.actor;
            if (this.selectedNodes.contains(node, true) && this.style.selection != null) {
                this.style.selection.draw(batch, x, (actor.getY() + y) - (this.ySpacing / 2.0f), getWidth(), this.ySpacing + node.height);
            } else if (node == this.overNode && this.style.over != null) {
                this.style.over.draw(batch, x, (actor.getY() + y) - (this.ySpacing / 2.0f), getWidth(), this.ySpacing + node.height);
            }
            if (node.icon != null) {
                float iconY = actor.getY() + Math.round((node.height - node.icon.getMinHeight()) / 2.0f);
                batch.setColor(actor.getColor());
                node.icon.draw(batch, ((node.actor.getX() + x) - this.iconSpacing) - node.icon.getMinWidth(), y + iconY, node.icon.getMinWidth(), node.icon.getMinHeight());
                batch.setColor(Color.WHITE);
            }
            if (node.children.size != 0) {
                Drawable expandIcon = node.expanded ? minus : plus;
                float iconY2 = actor.getY() + Math.round((node.height - expandIcon.getMinHeight()) / 2.0f);
                expandIcon.draw(batch, (x + indent) - this.iconSpacing, y + iconY2, expandIcon.getMinWidth(), expandIcon.getMinHeight());
                if (node.expanded) {
                    draw(batch, node.children, this.indentSpacing + indent);
                }
            }
        }
    }

    public Node getNodeAt(float y) {
        this.foundNode = null;
        getNodeAt(this.rootNodes, y, getHeight());
        return this.foundNode;
    }

    private float getNodeAt(Array<Node> nodes, float y, float rowY) {
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            if (y >= (rowY - node.height) - this.ySpacing && y < rowY) {
                this.foundNode = node;
                return -1.0f;
            }
            rowY -= node.height + this.ySpacing;
            if (node.expanded) {
                rowY = getNodeAt(node.children, y, rowY);
                if (rowY == -1.0f) {
                    return -1.0f;
                }
            }
        }
        return rowY;
    }

    void selectNodes(Array<Node> nodes, float low, float high) {
        float f = this.ySpacing;
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            if (node.actor.getY() >= low) {
                if (node.isSelectable()) {
                    if (node.actor.getY() <= high) {
                        this.selectedNodes.add(node);
                    }
                    if (node.expanded) {
                        selectNodes(node.children, low, high);
                    }
                }
            } else {
                return;
            }
        }
    }

    public Array<Node> getSelection() {
        return this.selectedNodes;
    }

    public void setSelection(Node node) {
        this.selectedNodes.clear();
        this.selectedNodes.add(node);
    }

    public void setSelection(Array<Node> nodes) {
        this.selectedNodes.clear();
        this.selectedNodes.addAll(nodes);
    }

    public void addSelection(Node node) {
        this.selectedNodes.add(node);
    }

    public void clearSelection() {
        this.selectedNodes.clear();
    }

    public TreeStyle getStyle() {
        return this.style;
    }

    public Array<Node> getRootNodes() {
        return this.rootNodes;
    }

    public Node getOverNode() {
        return this.overNode;
    }

    public void setOverNode(Node overNode) {
        this.overNode = overNode;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public void setYSpacing(float ySpacing) {
        this.ySpacing = ySpacing;
    }

    public void setIconSpacing(float iconSpacing) {
        this.iconSpacing = iconSpacing;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.prefWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.prefHeight;
    }

    public Node findNode(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        return findNode(this.rootNodes, object);
    }

    static Node findNode(Array<Node> nodes, Object object) {
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            if (object.equals(node.object)) {
                return node;
            }
        }
        int n2 = nodes.size;
        for (int i2 = 0; i2 < n2; i2++) {
            Node found = findNode(nodes.get(i2).children, object);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public void collapseAll() {
        collapseAll(this.rootNodes);
    }

    static void collapseAll(Array<Node> nodes) {
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            node.setExpanded(false);
            collapseAll(node.children);
        }
    }

    public void expandAll() {
        expandAll(this.rootNodes);
    }

    static void expandAll(Array<Node> nodes) {
        int n = nodes.size;
        for (int i = 0; i < n; i++) {
            nodes.get(i).expandAll();
        }
    }

    public ClickListener getClickListener() {
        return this.clickListener;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public static class Node {
        Actor actor;
        boolean expanded;
        float height;
        Drawable icon;
        Object object;
        Node parent;
        final Array<Node> children = new Array<>(0);
        boolean selectable = true;

        public Node(Actor actor) {
            if (actor == null) {
                throw new IllegalArgumentException("actor cannot be null.");
            }
            this.actor = actor;
        }

        public void setExpanded(boolean expanded) {
            Tree tree;
            if (expanded != this.expanded) {
                this.expanded = expanded;
                if (this.children.size != 0 && (tree = getTree()) != null) {
                    if (expanded) {
                        int n = this.children.size;
                        for (int i = 0; i < n; i++) {
                            this.children.get(i).addToTree(tree);
                        }
                    } else {
                        int n2 = this.children.size;
                        for (int i2 = 0; i2 < n2; i2++) {
                            this.children.get(i2).removeFromTree(tree);
                        }
                    }
                    tree.invalidateHierarchy();
                }
            }
        }

        protected void addToTree(Tree tree) {
            tree.addActor(this.actor);
            if (this.expanded) {
                int n = this.children.size;
                for (int i = 0; i < n; i++) {
                    this.children.get(i).addToTree(tree);
                }
            }
        }

        protected void removeFromTree(Tree tree) {
            tree.removeActor(this.actor);
            if (this.expanded) {
                int n = this.children.size;
                for (int i = 0; i < n; i++) {
                    this.children.get(i).removeFromTree(tree);
                }
            }
        }

        public void add(Node node) {
            insert(this.children.size, node);
        }

        public void addAll(Array<Node> nodes) {
            int n = nodes.size;
            for (int i = 0; i < n; i++) {
                insert(this.children.size, nodes.get(i));
            }
        }

        public void insert(int index, Node node) {
            Tree tree;
            node.parent = this;
            this.children.insert(index, node);
            if (this.expanded && (tree = getTree()) != null) {
                int n = this.children.size;
                for (int i = 0; i < n; i++) {
                    this.children.get(i).addToTree(tree);
                }
            }
        }

        public void remove() {
            Tree tree = getTree();
            if (tree != null) {
                tree.remove(this);
            }
        }

        public void remove(Node node) {
            Tree tree;
            this.children.removeValue(node, true);
            if (this.expanded && (tree = getTree()) != null) {
                node.removeFromTree(tree);
                if (this.children.size == 0) {
                    this.expanded = false;
                }
            }
        }

        public void removeAll() {
            Tree tree = getTree();
            if (tree != null) {
                int n = this.children.size;
                for (int i = 0; i < n; i++) {
                    this.children.get(i).removeFromTree(tree);
                }
            }
            this.children.clear();
        }

        public Tree getTree() {
            Group parent = this.actor.getParent();
            if (parent instanceof Tree) {
                return (Tree) parent;
            }
            return null;
        }

        public Actor getActor() {
            return this.actor;
        }

        public boolean isExpanded() {
            return this.expanded;
        }

        public Array<Node> getChildren() {
            return this.children;
        }

        public Node getParent() {
            return this.parent;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public Object getObject() {
            return this.object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Drawable getIcon() {
            return this.icon;
        }

        public Node findNode(Object object) {
            if (object == null) {
                throw new IllegalArgumentException("object cannot be null.");
            }
            return object.equals(this.object) ? this : Tree.findNode(this.children, object);
        }

        public void collapseAll() {
            setExpanded(false);
            Tree.collapseAll(this.children);
        }

        public void expandAll() {
            setExpanded(true);
            if (this.children.size > 0) {
                Tree.expandAll(this.children);
            }
        }

        public void expandTo() {
            for (Node node = this.parent; node != null; node = node.parent) {
                node.setExpanded(true);
            }
        }

        public boolean isSelectable() {
            return this.selectable;
        }

        public void setSelectable(boolean selectable) {
            this.selectable = selectable;
        }
    }

    public static class TreeStyle {
        public Drawable background;
        public Drawable minus;
        public Drawable over;
        public Drawable plus;
        public Drawable selection;

        public TreeStyle() {
        }

        public TreeStyle(Drawable plus, Drawable minus, Drawable selection) {
            this.plus = plus;
            this.minus = minus;
            this.selection = selection;
        }

        public TreeStyle(TreeStyle style) {
            this.plus = style.plus;
            this.minus = style.minus;
            this.selection = style.selection;
        }
    }
}
