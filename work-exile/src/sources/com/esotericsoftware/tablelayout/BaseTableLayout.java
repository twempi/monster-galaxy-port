package com.esotericsoftware.tablelayout;

import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class BaseTableLayout<C, T extends C, L extends BaseTableLayout, K extends Toolkit> {
    public static final int BOTTOM = 4;
    public static final int CENTER = 1;
    public static final int DEBUG_ALL = 1;
    public static final int DEBUG_CELL = 4;
    public static final int DEBUG_NONE = 0;
    public static final int DEBUG_TABLE = 2;
    public static final int DEBUG_WIDGET = 8;
    public static final int LEFT = 8;
    public static final String MAX = "max";
    public static final String MIN = "min";
    public static final String PREF = "pref";
    public static final int RIGHT = 16;
    public static final int TOP = 2;
    private int columns;
    String height;
    private int layoutHeight;
    private int layoutWidth;
    private int layoutX;
    private int layoutY;
    String padBottom;
    String padLeft;
    String padRight;
    String padTop;
    private Cell rowDefaults;
    private int rows;
    T table;
    private int tableMinHeight;
    private int tableMinWidth;
    private int tablePrefHeight;
    private int tablePrefWidth;
    K toolkit;
    String width;
    HashMap<String, C> nameToWidget = new HashMap<>();
    HashMap<C, Cell> widgetToCell = new HashMap<>();
    private final ArrayList<Cell> cells = new ArrayList<>();
    private final Cell cellDefaults = Cell.defaults(this);
    private final ArrayList<Cell> columnDefaults = new ArrayList<>(4);
    int align = 1;
    int debug = 0;

    public abstract void invalidate();

    public abstract void invalidateHierarchy();

    public BaseTableLayout(K toolkit) {
        this.toolkit = toolkit;
    }

    public void setLayoutSize(int tableLayoutX, int tableLayoutY, int tableLayoutWidth, int tableLayoutHeight) {
        this.layoutX = tableLayoutX;
        this.layoutY = tableLayoutY;
        this.layoutWidth = tableLayoutWidth;
        this.layoutHeight = tableLayoutHeight;
    }

    public C register(String name, C widget) {
        String name2 = name.toLowerCase();
        if (this.nameToWidget.containsKey(name2)) {
            throw new IllegalArgumentException("Name is already used: " + name2);
        }
        this.nameToWidget.put(name2, widget);
        return widget;
    }

    public void parse(String tableDescription) {
        TableLayoutParser.parse(this, tableDescription);
    }

    public Cell<C> add(C c) {
        Cell<C> cell = new Cell<>(this);
        cell.widget = c;
        this.widgetToCell.put(c, cell);
        for (Map.Entry<String, C> entry : this.nameToWidget.entrySet()) {
            if (c == entry.getValue()) {
                cell.name = entry.getKey();
                break;
            }
        }
        if (this.cells.size() > 0) {
            Cell cell2 = this.cells.get(this.cells.size() - 1);
            if (!cell2.endRow) {
                cell.column = cell2.column + cell2.colspan.intValue();
                cell.row = cell2.row;
            } else {
                cell.row = cell2.row + 1;
            }
            if (cell.row > 0) {
                loop1: for (int size = this.cells.size() - 1; size >= 0; size--) {
                    Cell cell3 = this.cells.get(size);
                    int i = cell3.column;
                    int iIntValue = i + cell3.colspan.intValue();
                    while (i < iIntValue) {
                        if (cell3.column != cell.column) {
                            i++;
                        } else {
                            cell.cellAboveIndex = size;
                            break loop1;
                        }
                    }
                }
            }
        }
        this.cells.add(cell);
        if (cell.column < this.columnDefaults.size()) {
            Cell cell4 = this.columnDefaults.get(cell.column);
            if (cell4 == null) {
                cell4 = this.cellDefaults;
            }
            cell.set(cell4);
        } else {
            cell.set(this.cellDefaults);
        }
        cell.merge(this.rowDefaults);
        this.toolkit.addChild(this.table, c, null);
        return cell;
    }

    public Cell row() {
        if (this.cells.size() > 0) {
            endRow();
        }
        this.rowDefaults = new Cell(this);
        return this.rowDefaults;
    }

    private void endRow() {
        int rowColumns = 0;
        for (int i = this.cells.size() - 1; i >= 0; i--) {
            Cell cell = this.cells.get(i);
            if (cell.endRow) {
                break;
            }
            rowColumns += cell.colspan.intValue();
        }
        this.columns = Math.max(this.columns, rowColumns);
        this.rows++;
        this.cells.get(this.cells.size() - 1).endRow = true;
    }

    public Cell columnDefaults(int column) {
        Cell cell = this.columnDefaults.size() > column ? this.columnDefaults.get(column) : null;
        if (cell == null) {
            cell = new Cell(this);
            cell.set(this.cellDefaults);
            if (column <= this.columnDefaults.size()) {
                for (int i = this.columnDefaults.size(); i < column; i++) {
                    this.columnDefaults.add(null);
                }
                this.columnDefaults.add(cell);
            } else {
                this.columnDefaults.set(column, cell);
            }
        }
        return cell;
    }

    public void reset() {
        clear();
        this.padTop = null;
        this.padLeft = null;
        this.padBottom = null;
        this.padRight = null;
        this.align = 1;
        if (this.debug != 0) {
            this.toolkit.clearDebugRectangles(this);
        }
        this.debug = 0;
        this.cellDefaults.set(Cell.defaults(this));
        this.columnDefaults.clear();
        this.rowDefaults = null;
    }

    public void clear() {
        for (int size = this.cells.size() - 1; size >= 0; size--) {
            this.toolkit.removeChild(this.table, this.cells.get(size).widget);
        }
        this.cells.clear();
        this.nameToWidget.clear();
        this.widgetToCell.clear();
        this.rows = 0;
        this.columns = 0;
    }

    public C getWidget(String name) {
        return this.nameToWidget.get(name.toLowerCase());
    }

    public List<C> getWidgets() {
        return new ArrayList(this.nameToWidget.values());
    }

    public List<C> getWidgets(String namePrefix) {
        ArrayList<C> widgets = new ArrayList<>();
        for (Map.Entry<String, C> entry : this.nameToWidget.entrySet()) {
            if (entry.getKey().startsWith(namePrefix)) {
                widgets.add(entry.getValue());
            }
        }
        return widgets;
    }

    public Cell getCell(C widget) {
        return this.widgetToCell.get(widget);
    }

    public Cell getCell(String name) {
        return getCell(getWidget(name));
    }

    public List<Cell> getAllCells() {
        return new ArrayList(this.widgetToCell.values());
    }

    public List<Cell> getAllCells(String namePrefix) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (Cell cell : this.widgetToCell.values()) {
            if (cell.name.startsWith(namePrefix)) {
                cells.add(cell);
            }
        }
        return cells;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

    public void setWidget(String name, C widget) {
        getCell(name).setWidget(widget);
    }

    public void setParent(BaseTableLayout parent) {
        this.nameToWidget = parent.nameToWidget;
        this.widgetToCell = parent.widgetToCell;
    }

    public void setToolkit(K toolkit) {
        this.toolkit = toolkit;
    }

    public T getTable() {
        return this.table;
    }

    public void setTable(T table) {
        this.table = table;
    }

    public int getLayoutX() {
        return this.layoutX;
    }

    public int getLayoutY() {
        return this.layoutY;
    }

    public int getLayoutWidth() {
        return this.layoutWidth;
    }

    public int getLayoutHeight() {
        return this.layoutHeight;
    }

    public int getMinWidth() {
        return this.tableMinWidth;
    }

    public int getMinHeight() {
        return this.tableMinHeight;
    }

    public int getPrefWidth() {
        return this.tablePrefWidth;
    }

    public int getPrefHeight() {
        return this.tablePrefHeight;
    }

    public Cell defaults() {
        return this.cellDefaults;
    }

    public K getToolkit() {
        return this.toolkit;
    }

    public L size(String width, String height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public L width(String width) {
        this.width = width;
        return this;
    }

    public L height(String height) {
        this.height = height;
        return this;
    }

    public L size(int width, int height) {
        this.width = String.valueOf(width);
        this.height = String.valueOf(height);
        return this;
    }

    public L width(int width) {
        this.width = String.valueOf(width);
        return this;
    }

    public L height(int height) {
        this.height = String.valueOf(height);
        return this;
    }

    public L pad(String pad) {
        this.padTop = pad;
        this.padLeft = pad;
        this.padBottom = pad;
        this.padRight = pad;
        return this;
    }

    public L pad(String top, String left, String bottom, String right) {
        this.padTop = top;
        this.padLeft = left;
        this.padBottom = bottom;
        this.padRight = right;
        return this;
    }

    public L padTop(String padTop) {
        this.padTop = padTop;
        return this;
    }

    public L padLeft(String padLeft) {
        this.padLeft = padLeft;
        return this;
    }

    public L padBottom(String padBottom) {
        this.padBottom = padBottom;
        return this;
    }

    public L padRight(String padRight) {
        this.padRight = padRight;
        return this;
    }

    public L pad(int pad) {
        this.padTop = String.valueOf(pad);
        this.padLeft = String.valueOf(pad);
        this.padBottom = String.valueOf(pad);
        this.padRight = String.valueOf(pad);
        return this;
    }

    public L pad(int top, int left, int bottom, int right) {
        this.padTop = String.valueOf(top);
        this.padLeft = String.valueOf(left);
        this.padBottom = String.valueOf(bottom);
        this.padRight = String.valueOf(right);
        return this;
    }

    public L padTop(int padTop) {
        this.padTop = String.valueOf(padTop);
        return this;
    }

    public L padLeft(int padLeft) {
        this.padLeft = String.valueOf(padLeft);
        return this;
    }

    public L padBottom(int padBottom) {
        this.padBottom = String.valueOf(padBottom);
        return this;
    }

    public L padRight(int padRight) {
        this.padRight = String.valueOf(padRight);
        return this;
    }

    public L align(int align) {
        this.align = align;
        return this;
    }

    public L align(String value) {
        this.align = 0;
        if (value.contains("center")) {
            this.align |= 1;
        }
        if (value.contains("left")) {
            this.align |= 8;
        }
        if (value.contains("right")) {
            this.align |= 16;
        }
        if (value.contains("top")) {
            this.align |= 2;
        }
        if (value.contains("bottom")) {
            this.align |= 4;
        }
        return this;
    }

    public L center() {
        this.align |= 1;
        return this;
    }

    public L top() {
        this.align |= 2;
        this.align &= -5;
        return this;
    }

    public L left() {
        this.align |= 8;
        this.align &= -17;
        return this;
    }

    public L bottom() {
        this.align |= 4;
        this.align &= -3;
        return this;
    }

    public L right() {
        this.align |= 16;
        this.align &= -9;
        return this;
    }

    public L debug(int debug) {
        this.debug = debug;
        if (debug == 0) {
            this.toolkit.clearDebugRectangles(this);
        }
        return this;
    }

    public L debug(String value) {
        this.debug = 0;
        if (value != null) {
            if (value.equalsIgnoreCase("true")) {
                this.debug |= 1;
            }
            if (value.contains("all")) {
                this.debug |= 1;
            }
            if (value.contains("cell")) {
                this.debug |= 4;
            }
            if (value.contains("table")) {
                this.debug |= 2;
            }
            if (value.contains("widget")) {
                this.debug |= 8;
            }
            if (this.debug == 0) {
                this.toolkit.clearDebugRectangles(this);
            }
        }
        return this;
    }

    public int getDebug() {
        return this.debug;
    }

    public String getWidth() {
        return this.width;
    }

    public String getHeight() {
        return this.height;
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

    public int getAlign() {
        return this.align;
    }

    public void layout() {
        int[] columnMaxWidth;
        int[] rowMaxHeight;
        int diff;
        int diff2;
        Toolkit toolkit = this.toolkit;
        ArrayList<Cell> cells = this.cells;
        if (cells.size() > 0 && !cells.get(cells.size() - 1).endRow) {
            endRow();
        }
        int[] columnMinWidth = new int[this.columns];
        int[] rowMinHeight = new int[this.rows];
        int[] columnPrefWidth = new int[this.columns];
        int[] rowPrefHeight = new int[this.rows];
        int spaceRightLast = 0;
        int n = cells.size();
        for (int i = 0; i < n; i++) {
            Cell c = cells.get(i);
            if (!c.ignore.booleanValue()) {
                c.padLeftTemp = c.column == 0 ? toolkit.width(this, c.padLeft) : toolkit.width(this, c.padLeft) + Math.max(0, toolkit.width(this, c.spaceLeft) - spaceRightLast);
                c.padTopTemp = c.cellAboveIndex == -1 ? toolkit.height(this, c.padTop) : Math.max(0, toolkit.height(this, c.spaceTop) - toolkit.height(this, cells.get(c.cellAboveIndex).spaceBottom)) + toolkit.height(this, c.padTop);
                int spaceRight = toolkit.width(this, c.spaceRight);
                c.padRightTemp = c.column + c.colspan.intValue() == this.columns ? toolkit.width(this, c.padRight) : toolkit.width(this, c.padRight) + spaceRight;
                c.padBottomTemp = c.row == this.rows + (-1) ? toolkit.height(this, c.padBottom) : toolkit.height(this, c.padBottom) + toolkit.height(this, c.spaceBottom);
                spaceRightLast = spaceRight;
                int prefWidth = toolkit.getWidgetWidth(this, c.widget, c.prefWidth);
                int prefHeight = toolkit.getWidgetHeight(this, c.widget, c.prefHeight);
                int minWidth = toolkit.getWidgetWidth(this, c.widget, c.minWidth);
                int minHeight = toolkit.getWidgetHeight(this, c.widget, c.minHeight);
                if (prefWidth < minWidth) {
                    prefWidth = minWidth;
                }
                if (prefHeight < minHeight) {
                    prefHeight = minHeight;
                }
                if (c.colspan.intValue() == 1) {
                    int hpadding = c.padLeftTemp + c.padRightTemp;
                    columnPrefWidth[c.column] = Math.max(columnPrefWidth[c.column], prefWidth + hpadding);
                    columnMinWidth[c.column] = Math.max(columnMinWidth[c.column], minWidth + hpadding);
                }
                int vpadding = c.padTopTemp + c.padBottomTemp;
                rowPrefHeight[c.row] = Math.max(rowPrefHeight[c.row], prefHeight + vpadding);
                rowMinHeight[c.row] = Math.max(rowMinHeight[c.row], minHeight + vpadding);
            }
        }
        this.tableMinWidth = 0;
        this.tableMinHeight = 0;
        this.tablePrefWidth = 0;
        this.tablePrefHeight = 0;
        for (int i2 = 0; i2 < this.columns; i2++) {
            this.tableMinWidth += columnMinWidth[i2];
            this.tablePrefWidth += columnPrefWidth[i2];
        }
        for (int i3 = 0; i3 < this.rows; i3++) {
            this.tableMinHeight += rowMinHeight[i3];
            this.tablePrefHeight += Math.max(rowMinHeight[i3], rowPrefHeight[i3]);
        }
        int hpadding2 = toolkit.width(this, this.padLeft) + toolkit.width(this, this.padRight);
        int vpadding2 = toolkit.height(this, this.padTop) + toolkit.height(this, this.padBottom);
        int width = toolkit.width(this, this.width) - hpadding2;
        int height = toolkit.height(this, this.height) - vpadding2;
        this.tableMinWidth = Math.max(this.tableMinWidth + hpadding2, width);
        this.tableMinHeight = Math.max(this.tableMinHeight + vpadding2, height);
        this.tablePrefWidth = Math.max(this.tablePrefWidth + hpadding2, this.tableMinWidth);
        this.tablePrefHeight = Math.max(this.tablePrefHeight + vpadding2, this.tableMinHeight);
        int tableLayoutWidth = this.layoutWidth;
        int totalGrowWidth = this.tablePrefWidth - this.tableMinWidth;
        if (totalGrowWidth == 0) {
            columnMaxWidth = columnMinWidth;
        } else {
            int extraWidth = Math.max(0, tableLayoutWidth - this.tableMinWidth);
            columnMaxWidth = new int[this.columns];
            for (int i4 = 0; i4 < this.columns; i4++) {
                int growWidth = columnPrefWidth[i4] - columnMinWidth[i4];
                float growRatio = growWidth / totalGrowWidth;
                columnMaxWidth[i4] = columnMinWidth[i4] + ((int) (extraWidth * growRatio));
            }
        }
        int tableLayoutHeight = this.layoutHeight;
        int totalGrowHeight = this.tablePrefHeight - this.tableMinHeight;
        if (totalGrowHeight == 0) {
            rowMaxHeight = rowMinHeight;
        } else {
            int extraHeight = Math.max(0, tableLayoutHeight - this.tableMinHeight);
            rowMaxHeight = new int[this.rows];
            for (int i5 = 0; i5 < this.rows; i5++) {
                int growHeight = rowPrefHeight[i5] - rowMinHeight[i5];
                float growRatio2 = growHeight / totalGrowHeight;
                rowMaxHeight[i5] = rowMinHeight[i5] + ((int) (extraHeight * growRatio2));
            }
        }
        int[] columnWidth = new int[this.columns];
        int[] rowHeight = new int[this.rows];
        float[] expandWidth = new float[this.columns];
        float[] expandHeight = new float[this.rows];
        float totalExpandWidth = 0.0f;
        float totalExpandHeight = 0.0f;
        int n2 = cells.size();
        for (int i6 = 0; i6 < n2; i6++) {
            Cell c2 = cells.get(i6);
            if (!c2.ignore.booleanValue()) {
                int spannedCellMaxWidth = 0;
                int column = c2.column;
                int nn = column + c2.colspan.intValue();
                while (column < nn) {
                    spannedCellMaxWidth += columnMaxWidth[column];
                    if (c2.colspan.intValue() == 1 && c2.expandX.intValue() != 0 && expandWidth[column] == 0.0f) {
                        expandWidth[column] = c2.expandX.intValue() / c2.colspan.intValue();
                        totalExpandWidth += c2.expandX.intValue() / c2.colspan.intValue();
                    }
                    column++;
                }
                int spannedCellMaxWidth2 = spannedCellMaxWidth - (c2.padLeftTemp + c2.padRightTemp);
                if (c2.expandY.intValue() != 0 && expandHeight[c2.row] == 0.0f) {
                    expandHeight[c2.row] = c2.expandY.intValue();
                    totalExpandHeight += c2.expandY.intValue();
                }
                int prefWidth2 = toolkit.getWidgetWidth(this, c2.widget, c2.prefWidth);
                int prefHeight2 = toolkit.getWidgetHeight(this, c2.widget, c2.prefHeight);
                int minWidth2 = toolkit.getWidgetWidth(this, c2.widget, c2.minWidth);
                int minHeight2 = toolkit.getWidgetHeight(this, c2.widget, c2.minHeight);
                if (prefWidth2 < minWidth2) {
                    prefWidth2 = minWidth2;
                }
                if (prefHeight2 < minHeight2) {
                    prefHeight2 = minHeight2;
                }
                c2.widgetWidth = Math.min(spannedCellMaxWidth2, prefWidth2);
                c2.widgetHeight = Math.min((rowMaxHeight[c2.row] - c2.padTopTemp) - c2.padBottomTemp, prefHeight2);
                if (c2.colspan.intValue() == 1) {
                    columnWidth[c2.column] = Math.max(columnWidth[c2.column], c2.widgetWidth + c2.padLeftTemp + c2.padRightTemp);
                }
                rowHeight[c2.row] = Math.max(rowHeight[c2.row], c2.widgetHeight + c2.padTopTemp + c2.padBottomTemp);
            }
        }
        int uniformMaxWidth = 0;
        int uniformMaxHeight = 0;
        int n3 = cells.size();
        for (int i7 = 0; i7 < n3; i7++) {
            Cell c3 = cells.get(i7);
            if (!c3.ignore.booleanValue()) {
                if (c3.uniformX != null) {
                    uniformMaxWidth = Math.max(uniformMaxWidth, columnWidth[c3.column]);
                }
                if (c3.uniformY != null) {
                    uniformMaxHeight = Math.max(uniformMaxHeight, rowHeight[c3.row]);
                }
            }
        }
        if (uniformMaxWidth > 0 || uniformMaxHeight > 0) {
            int n4 = cells.size();
            for (int i8 = 0; i8 < n4; i8++) {
                Cell c4 = cells.get(i8);
                if (!c4.ignore.booleanValue()) {
                    if (uniformMaxWidth > 0 && c4.uniformX != null && (diff2 = uniformMaxWidth - columnWidth[c4.column]) > 0) {
                        columnWidth[c4.column] = uniformMaxWidth;
                        this.tableMinWidth += diff2;
                        this.tablePrefWidth += diff2;
                    }
                    if (uniformMaxHeight > 0 && c4.uniformY != null && (diff = uniformMaxHeight - rowHeight[c4.row]) > 0) {
                        rowHeight[c4.row] = uniformMaxHeight;
                        this.tableMinHeight += diff;
                        this.tablePrefHeight += diff;
                    }
                }
            }
        }
        if (totalExpandWidth > 0.0f) {
            int extra = Math.max(0, tableLayoutWidth - this.tablePrefWidth);
            int used = 0;
            int lastIndex = 0;
            for (int i9 = 0; i9 < this.columns; i9++) {
                if (expandWidth[i9] != 0.0f) {
                    int amount = (int) ((extra * expandWidth[i9]) / totalExpandWidth);
                    columnWidth[i9] = columnWidth[i9] + amount;
                    used += amount;
                    lastIndex = i9;
                }
            }
            columnWidth[lastIndex] = columnWidth[lastIndex] + (extra - used);
        }
        if (totalExpandHeight > 0.0f) {
            int extra2 = Math.max(0, tableLayoutHeight - this.tablePrefHeight);
            int used2 = 0;
            int lastIndex2 = 0;
            for (int i10 = 0; i10 < this.rows; i10++) {
                if (expandHeight[i10] != 0.0f) {
                    int amount2 = (int) ((extra2 * expandHeight[i10]) / totalExpandHeight);
                    rowHeight[i10] = rowHeight[i10] + amount2;
                    used2 += amount2;
                    lastIndex2 = i10;
                }
            }
            rowHeight[lastIndex2] = rowHeight[lastIndex2] + (extra2 - used2);
        }
        int n5 = cells.size();
        for (int i11 = 0; i11 < n5; i11++) {
            Cell c5 = cells.get(i11);
            if (!c5.ignore.booleanValue() && c5.colspan.intValue() != 1) {
                int minWidth3 = toolkit.getWidgetWidth(this, c5.widget, c5.minWidth);
                int spannedCellWidth = 0;
                int column2 = c5.column;
                int nn2 = column2 + c5.colspan.intValue();
                while (column2 < nn2) {
                    spannedCellWidth += columnWidth[column2];
                    column2++;
                }
                int extraWidth2 = Math.max(0, minWidth3 - spannedCellWidth) / c5.colspan.intValue();
                int column3 = c5.column;
                int nn3 = column3 + c5.colspan.intValue();
                while (column3 < nn3) {
                    columnWidth[column3] = columnWidth[column3] + extraWidth2;
                    column3++;
                }
                c5.widgetWidth = Math.max(c5.widgetWidth, minWidth3 - (c5.padLeftTemp + c5.padRightTemp));
            }
        }
        int tableWidth = 0;
        int tableHeight = 0;
        for (int i12 = 0; i12 < this.columns; i12++) {
            tableWidth += columnWidth[i12];
        }
        int tableWidth2 = Math.max(tableWidth + hpadding2, width);
        for (int i13 = 0; i13 < this.rows; i13++) {
            tableHeight += rowHeight[i13];
        }
        int tableHeight2 = Math.max(tableHeight + vpadding2, height);
        int x = this.layoutX + toolkit.width(this, this.padLeft);
        if ((this.align & 16) != 0) {
            x += tableLayoutWidth - tableWidth2;
        } else if ((this.align & 8) == 0) {
            x += (tableLayoutWidth - tableWidth2) / 2;
        }
        int y = this.layoutY + toolkit.height(this, this.padTop);
        if ((this.align & 4) != 0) {
            y += tableLayoutHeight - tableHeight2;
        } else if ((this.align & 2) == 0) {
            y += (tableLayoutHeight - tableHeight2) / 2;
        }
        int currentX = x;
        int currentY = y;
        int n6 = cells.size();
        for (int i14 = 0; i14 < n6; i14++) {
            Cell c6 = cells.get(i14);
            if (!c6.ignore.booleanValue()) {
                int spannedCellWidth2 = 0;
                int column4 = c6.column;
                int nn4 = column4 + c6.colspan.intValue();
                while (column4 < nn4) {
                    spannedCellWidth2 += columnWidth[column4];
                    column4++;
                }
                int spannedCellWidth3 = spannedCellWidth2 - (c6.padLeftTemp + c6.padRightTemp);
                int currentX2 = currentX + c6.padLeftTemp;
                if (c6.fillX.floatValue() > 0.0f) {
                    c6.widgetWidth = (int) (spannedCellWidth3 * c6.fillX.floatValue());
                    int maxWidth = toolkit.getWidgetWidth(this, c6.widget, c6.maxWidth);
                    if (maxWidth > 0) {
                        c6.widgetWidth = Math.min(c6.widgetWidth, maxWidth);
                    }
                }
                if (c6.fillY.floatValue() > 0.0f) {
                    c6.widgetHeight = (((int) (rowHeight[c6.row] * c6.fillY.floatValue())) - c6.padTopTemp) - c6.padBottomTemp;
                    int maxHeight = toolkit.getWidgetHeight(this, c6.widget, c6.maxHeight);
                    if (maxHeight > 0) {
                        c6.widgetHeight = Math.min(c6.widgetHeight, maxHeight);
                    }
                }
                if ((c6.align.intValue() & 8) != 0) {
                    c6.widgetX = currentX2;
                } else if ((c6.align.intValue() & 16) != 0) {
                    c6.widgetX = (currentX2 + spannedCellWidth3) - c6.widgetWidth;
                } else {
                    c6.widgetX = ((spannedCellWidth3 - c6.widgetWidth) / 2) + currentX2;
                }
                if ((c6.align.intValue() & 2) != 0) {
                    c6.widgetY = c6.padTopTemp + currentY;
                } else if ((c6.align.intValue() & 4) != 0) {
                    c6.widgetY = ((rowHeight[c6.row] + currentY) - c6.widgetHeight) - c6.padBottomTemp;
                } else {
                    c6.widgetY = ((((rowHeight[c6.row] - c6.widgetHeight) + c6.padTopTemp) - c6.padBottomTemp) / 2) + currentY;
                }
                if (c6.endRow) {
                    currentX = x;
                    currentY += rowHeight[c6.row];
                } else {
                    currentX = currentX2 + c6.padRightTemp + spannedCellWidth3;
                }
            }
        }
        if (this.debug != 0) {
            toolkit.clearDebugRectangles(this);
            int currentX3 = x;
            int currentY2 = y;
            if ((this.debug & 2) != 0 || (this.debug & 1) != 0) {
                toolkit.addDebugRectangle(this, 2, this.layoutX, this.layoutY, tableLayoutWidth, tableLayoutHeight);
                toolkit.addDebugRectangle(this, 2, x, y, tableWidth2 - hpadding2, tableHeight2 - vpadding2);
            }
            int i15 = 0;
            int n7 = cells.size();
            while (true) {
                int currentX4 = currentX3;
                if (i15 < n7) {
                    Cell c7 = cells.get(i15);
                    if (c7.ignore.booleanValue()) {
                        currentX3 = currentX4;
                    } else {
                        if ((this.debug & 8) != 0 || (this.debug & 1) != 0) {
                            toolkit.addDebugRectangle(this, 8, c7.widgetX, c7.widgetY, c7.widgetWidth, c7.widgetHeight);
                        }
                        int spannedCellWidth4 = 0;
                        int column5 = c7.column;
                        int nn5 = column5 + c7.colspan.intValue();
                        while (column5 < nn5) {
                            spannedCellWidth4 += columnWidth[column5];
                            column5++;
                        }
                        int spannedCellWidth5 = spannedCellWidth4 - (c7.padLeftTemp + c7.padRightTemp);
                        int currentX5 = currentX4 + c7.padLeftTemp;
                        if ((this.debug & 4) != 0 || (this.debug & 1) != 0) {
                            toolkit.addDebugRectangle(this, 4, currentX5, currentY2 + c7.padTopTemp, spannedCellWidth5, (rowHeight[c7.row] - c7.padTopTemp) - c7.padBottomTemp);
                        }
                        if (c7.endRow) {
                            currentX3 = x;
                            currentY2 += rowHeight[c7.row];
                        } else {
                            currentX3 = currentX5 + c7.padRightTemp + spannedCellWidth5;
                        }
                    }
                    i15++;
                } else {
                    return;
                }
            }
        }
    }
}
