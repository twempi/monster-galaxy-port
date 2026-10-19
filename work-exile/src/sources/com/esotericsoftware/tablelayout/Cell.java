package com.esotericsoftware.tablelayout;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Cell<C> {
    Integer align;
    int cellAboveIndex = -1;
    Integer colspan;
    int column;
    boolean endRow;
    Integer expandX;
    Integer expandY;
    Float fillX;
    Float fillY;
    Boolean ignore;
    private final BaseTableLayout layout;
    String maxHeight;
    String maxWidth;
    String minHeight;
    String minWidth;
    String name;
    String padBottom;
    int padBottomTemp;
    String padLeft;
    int padLeftTemp;
    String padRight;
    int padRightTemp;
    String padTop;
    int padTopTemp;
    String prefHeight;
    String prefWidth;
    int row;
    String spaceBottom;
    String spaceLeft;
    String spaceRight;
    String spaceTop;
    Boolean uniformX;
    Boolean uniformY;
    C widget;
    int widgetHeight;
    int widgetWidth;
    int widgetX;
    int widgetY;

    Cell(BaseTableLayout layout) {
        this.layout = layout;
    }

    void set(Cell defaults) {
        this.minWidth = defaults.minWidth;
        this.minHeight = defaults.minHeight;
        this.prefWidth = defaults.prefWidth;
        this.prefHeight = defaults.prefHeight;
        this.maxWidth = defaults.maxWidth;
        this.maxHeight = defaults.maxHeight;
        this.spaceTop = defaults.spaceTop;
        this.spaceLeft = defaults.spaceLeft;
        this.spaceBottom = defaults.spaceBottom;
        this.spaceRight = defaults.spaceRight;
        this.padTop = defaults.padTop;
        this.padLeft = defaults.padLeft;
        this.padBottom = defaults.padBottom;
        this.padRight = defaults.padRight;
        this.fillX = defaults.fillX;
        this.fillY = defaults.fillY;
        this.align = defaults.align;
        this.expandX = defaults.expandX;
        this.expandY = defaults.expandY;
        this.ignore = defaults.ignore;
        this.colspan = defaults.colspan;
        this.uniformX = defaults.uniformX;
        this.uniformY = defaults.uniformY;
    }

    void merge(Cell cell) {
        if (cell != null) {
            if (cell.minWidth != null) {
                this.minWidth = cell.minWidth;
            }
            if (cell.minHeight != null) {
                this.minHeight = cell.minHeight;
            }
            if (cell.prefWidth != null) {
                this.prefWidth = cell.prefWidth;
            }
            if (cell.prefHeight != null) {
                this.prefHeight = cell.prefHeight;
            }
            if (cell.maxWidth != null) {
                this.maxWidth = cell.maxWidth;
            }
            if (cell.maxHeight != null) {
                this.maxHeight = cell.maxHeight;
            }
            if (cell.spaceTop != null) {
                this.spaceTop = cell.spaceTop;
            }
            if (cell.spaceLeft != null) {
                this.spaceLeft = cell.spaceLeft;
            }
            if (cell.spaceBottom != null) {
                this.spaceBottom = cell.spaceBottom;
            }
            if (cell.spaceRight != null) {
                this.spaceRight = cell.spaceRight;
            }
            if (cell.padTop != null) {
                this.padTop = cell.padTop;
            }
            if (cell.padLeft != null) {
                this.padLeft = cell.padLeft;
            }
            if (cell.padBottom != null) {
                this.padBottom = cell.padBottom;
            }
            if (cell.padRight != null) {
                this.padRight = cell.padRight;
            }
            if (cell.fillX != null) {
                this.fillX = cell.fillX;
            }
            if (cell.fillY != null) {
                this.fillY = cell.fillY;
            }
            if (cell.align != null) {
                this.align = cell.align;
            }
            if (cell.expandX != null) {
                this.expandX = cell.expandX;
            }
            if (cell.expandY != null) {
                this.expandY = cell.expandY;
            }
            if (cell.ignore != null) {
                this.ignore = cell.ignore;
            }
            if (cell.colspan != null) {
                this.colspan = cell.colspan;
            }
            if (cell.uniformX != null) {
                this.uniformX = cell.uniformX;
            }
            if (cell.uniformY != null) {
                this.uniformY = cell.uniformY;
            }
        }
    }

    public Cell setWidget(C widget) {
        this.layout.toolkit.setWidget(this.layout, this, widget);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public C getWidget() {
        return this.widget;
    }

    public Cell minSize(String size) {
        this.minWidth = size;
        this.minHeight = size;
        return this;
    }

    public Cell minWidth(String minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    public Cell minHeight(String minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    public Cell minSize(int size) {
        this.minWidth = String.valueOf(size);
        this.minHeight = String.valueOf(size);
        return this;
    }

    public Cell minWidth(int minWidth) {
        this.minWidth = String.valueOf(minWidth);
        return this;
    }

    public Cell minHeight(int minHeight) {
        this.minHeight = String.valueOf(minHeight);
        return this;
    }

    public Cell prefSize(String size) {
        this.prefWidth = size;
        this.prefHeight = size;
        return this;
    }

    public Cell prefWidth(String prefWidth) {
        this.prefWidth = prefWidth;
        return this;
    }

    public Cell prefHeight(String prefHeight) {
        this.prefHeight = prefHeight;
        return this;
    }

    public Cell prefSize(int size) {
        this.prefWidth = String.valueOf(size);
        this.prefHeight = String.valueOf(size);
        return this;
    }

    public Cell prefWidth(int prefWidth) {
        this.prefWidth = String.valueOf(prefWidth);
        return this;
    }

    public Cell prefHeight(int prefHeight) {
        this.prefHeight = String.valueOf(prefHeight);
        return this;
    }

    public Cell maxSize(String size) {
        this.maxWidth = size;
        this.maxHeight = size;
        return this;
    }

    public Cell maxWidth(String maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public Cell maxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public Cell maxSize(int size) {
        this.maxWidth = String.valueOf(size);
        this.maxHeight = String.valueOf(size);
        return this;
    }

    public Cell maxWidth(int maxWidth) {
        this.maxWidth = String.valueOf(maxWidth);
        return this;
    }

    public Cell maxHeight(int maxHeight) {
        this.maxHeight = String.valueOf(maxHeight);
        return this;
    }

    public Cell space(String space) {
        this.spaceTop = space;
        this.spaceLeft = space;
        this.spaceBottom = space;
        this.spaceRight = space;
        return this;
    }

    public Cell space(String top, String left, String bottom, String right) {
        this.spaceTop = top;
        this.spaceLeft = left;
        this.spaceBottom = bottom;
        this.spaceRight = right;
        return this;
    }

    public Cell spaceTop(String spaceTop) {
        this.spaceTop = spaceTop;
        return this;
    }

    public Cell spaceLeft(String spaceLeft) {
        this.spaceLeft = spaceLeft;
        return this;
    }

    public Cell spaceBottom(String spaceBottom) {
        this.spaceBottom = spaceBottom;
        return this;
    }

    public Cell spaceRight(String spaceRight) {
        this.spaceRight = spaceRight;
        return this;
    }

    public Cell space(int space) {
        String value = String.valueOf(space);
        this.spaceTop = value;
        this.spaceLeft = value;
        this.spaceBottom = value;
        this.spaceRight = value;
        return this;
    }

    public Cell space(int top, int left, int bottom, int right) {
        this.spaceTop = String.valueOf(top);
        this.spaceLeft = String.valueOf(left);
        this.spaceBottom = String.valueOf(bottom);
        this.spaceRight = String.valueOf(right);
        return this;
    }

    public Cell spaceTop(int spaceTop) {
        this.spaceTop = String.valueOf(spaceTop);
        return this;
    }

    public Cell spaceLeft(int spaceLeft) {
        this.spaceLeft = String.valueOf(spaceLeft);
        return this;
    }

    public Cell spaceBottom(int spaceBottom) {
        this.spaceBottom = String.valueOf(spaceBottom);
        return this;
    }

    public Cell spaceRight(int spaceRight) {
        this.spaceRight = String.valueOf(spaceRight);
        return this;
    }

    public Cell pad(String pad) {
        this.padTop = pad;
        this.padLeft = pad;
        this.padBottom = pad;
        this.padRight = pad;
        return this;
    }

    public Cell pad(String top, String left, String bottom, String right) {
        this.padTop = top;
        this.padLeft = left;
        this.padBottom = bottom;
        this.padRight = right;
        return this;
    }

    public Cell padTop(String padTop) {
        this.padTop = padTop;
        return this;
    }

    public Cell padLeft(String padLeft) {
        this.padLeft = padLeft;
        return this;
    }

    public Cell padBottom(String padBottom) {
        this.padBottom = padBottom;
        return this;
    }

    public Cell padRight(String padRight) {
        this.padRight = padRight;
        return this;
    }

    public Cell pad(int pad) {
        String value = String.valueOf(pad);
        this.padTop = value;
        this.padLeft = value;
        this.padBottom = value;
        this.padRight = value;
        return this;
    }

    public Cell pad(int top, int left, int bottom, int right) {
        this.padTop = String.valueOf(top);
        this.padLeft = String.valueOf(left);
        this.padBottom = String.valueOf(bottom);
        this.padRight = String.valueOf(right);
        return this;
    }

    public Cell padTop(int padTop) {
        this.padTop = String.valueOf(padTop);
        return this;
    }

    public Cell padLeft(int padLeft) {
        this.padLeft = String.valueOf(padLeft);
        return this;
    }

    public Cell padBottom(int padBottom) {
        this.padBottom = String.valueOf(padBottom);
        return this;
    }

    public Cell padRight(int padRight) {
        this.padRight = String.valueOf(padRight);
        return this;
    }

    public Cell fill() {
        this.fillX = Float.valueOf(1.0f);
        this.fillY = Float.valueOf(1.0f);
        return this;
    }

    public Cell fillX() {
        this.fillX = Float.valueOf(1.0f);
        return this;
    }

    public Cell fillY() {
        this.fillY = Float.valueOf(1.0f);
        return this;
    }

    public Cell fill(Float x, Float y) {
        this.fillX = x;
        this.fillY = y;
        return this;
    }

    public Cell fill(boolean x, boolean y) {
        this.fillX = Float.valueOf(x ? 1.0f : 0.0f);
        this.fillY = Float.valueOf(y ? 1.0f : 0.0f);
        return this;
    }

    public Cell align(Integer align) {
        this.align = align;
        return this;
    }

    public Cell align(String value) {
        this.align = 0;
        if (value.contains("center")) {
            this.align = Integer.valueOf(this.align.intValue() | 1);
        }
        if (value.contains("left")) {
            this.align = Integer.valueOf(this.align.intValue() | 8);
        }
        if (value.contains("right")) {
            this.align = Integer.valueOf(this.align.intValue() | 16);
        }
        if (value.contains("top")) {
            this.align = Integer.valueOf(this.align.intValue() | 2);
        }
        if (value.contains("bottom")) {
            this.align = Integer.valueOf(this.align.intValue() | 4);
        }
        return this;
    }

    public Cell center() {
        if (this.align == null) {
            this.align = 1;
        } else {
            this.align = Integer.valueOf(this.align.intValue() | 1);
        }
        return this;
    }

    public Cell top() {
        if (this.align == null) {
            this.align = 2;
        } else {
            this.align = Integer.valueOf(this.align.intValue() | 2);
            this.align = Integer.valueOf(this.align.intValue() & (-5));
        }
        return this;
    }

    public Cell left() {
        if (this.align == null) {
            this.align = 8;
        } else {
            this.align = Integer.valueOf(this.align.intValue() | 8);
            this.align = Integer.valueOf(this.align.intValue() & (-17));
        }
        return this;
    }

    public Cell bottom() {
        if (this.align == null) {
            this.align = 4;
        } else {
            this.align = Integer.valueOf(this.align.intValue() | 4);
            this.align = Integer.valueOf(this.align.intValue() & (-3));
        }
        return this;
    }

    public Cell right() {
        if (this.align == null) {
            this.align = 16;
        } else {
            this.align = Integer.valueOf(this.align.intValue() | 16);
            this.align = Integer.valueOf(this.align.intValue() & (-9));
        }
        return this;
    }

    public Cell expand() {
        this.expandX = 1;
        this.expandY = 1;
        return this;
    }

    public Cell expandX() {
        this.expandX = 1;
        return this;
    }

    public Cell expandY() {
        this.expandY = 1;
        return this;
    }

    public Cell expand(Integer x, Integer y) {
        this.expandX = x;
        this.expandY = y;
        return this;
    }

    public Cell expand(boolean x, boolean y) {
        this.expandX = Integer.valueOf(x ? 1 : 0);
        this.expandY = Integer.valueOf(y ? 1 : 0);
        return this;
    }

    public Cell ignore(Boolean ignore) {
        this.ignore = ignore;
        return this;
    }

    public Cell ignore() {
        this.ignore = true;
        return this;
    }

    public boolean getIgnore() {
        return this.ignore != null && this.ignore.booleanValue();
    }

    public Cell colspan(Integer colspan) {
        this.colspan = colspan;
        return this;
    }

    public Cell uniform() {
        this.uniformX = true;
        this.uniformY = true;
        return this;
    }

    public Cell uniformX() {
        this.uniformX = true;
        return this;
    }

    public Cell uniformY() {
        this.uniformY = true;
        return this;
    }

    public Cell uniform(Boolean x, Boolean y) {
        this.uniformX = x;
        this.uniformY = y;
        return this;
    }

    public int getWidgetX() {
        return this.widgetX;
    }

    public int getWidgetY() {
        return this.widgetY;
    }

    public int getWidgetWidth() {
        return this.widgetWidth;
    }

    public int getWidgetHeight() {
        return this.widgetHeight;
    }

    public int getColumn() {
        return this.column;
    }

    public int getRow() {
        return this.row;
    }

    public String getMinWidth() {
        return this.minWidth;
    }

    public String getMinHeight() {
        return this.minHeight;
    }

    public String getPrefWidth() {
        return this.prefWidth;
    }

    public String getPrefHeight() {
        return this.prefHeight;
    }

    public String getMaxWidth() {
        return this.maxWidth;
    }

    public String getMaxHeight() {
        return this.maxHeight;
    }

    public String getSpaceTop() {
        return this.spaceTop;
    }

    public String getSpaceLeft() {
        return this.spaceLeft;
    }

    public String getSpaceBottom() {
        return this.spaceBottom;
    }

    public String getSpaceRight() {
        return this.spaceRight;
    }

    public String getPadTop() {
        return this.padTop;
    }

    public String getPadLeft() {
        return this.padLeft;
    }

    public String getPadBottom() {
        return this.padBottom;
    }

    public String getPadRight() {
        return this.padRight;
    }

    public Float getFillX() {
        return this.fillX;
    }

    public Float getFillY() {
        return this.fillY;
    }

    public Integer getAlign() {
        return this.align;
    }

    public Integer getExpandX() {
        return this.expandX;
    }

    public Integer getExpandY() {
        return this.expandY;
    }

    public Integer getColspan() {
        return this.colspan;
    }

    public Boolean getUniformX() {
        return this.uniformX;
    }

    public Boolean getUniformY() {
        return this.uniformY;
    }

    public boolean isEndRow() {
        return this.endRow;
    }

    static Cell defaults(BaseTableLayout layout) {
        Cell defaults = new Cell(layout);
        defaults.minWidth = BaseTableLayout.MIN;
        defaults.minHeight = BaseTableLayout.MIN;
        defaults.prefWidth = BaseTableLayout.PREF;
        defaults.prefHeight = BaseTableLayout.PREF;
        defaults.maxWidth = BaseTableLayout.MAX;
        defaults.maxHeight = BaseTableLayout.MAX;
        defaults.spaceTop = null;
        defaults.spaceLeft = null;
        defaults.spaceBottom = null;
        defaults.spaceRight = null;
        defaults.padTop = null;
        defaults.padLeft = null;
        defaults.padBottom = null;
        defaults.padRight = null;
        defaults.fillX = Float.valueOf(0.0f);
        defaults.fillY = Float.valueOf(0.0f);
        defaults.align = 1;
        defaults.expandX = 0;
        defaults.expandY = 0;
        defaults.ignore = false;
        defaults.colspan = 1;
        return defaults;
    }
}
