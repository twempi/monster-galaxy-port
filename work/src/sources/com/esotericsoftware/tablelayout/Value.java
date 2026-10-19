package com.esotericsoftware.tablelayout;

/* JADX INFO: loaded from: classes.dex */
public abstract class Value {
    public static final Value zero = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.1
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            return 0.0f;
        }

        @Override // com.esotericsoftware.tablelayout.Value.CellValue, com.esotericsoftware.tablelayout.Value
        public float get(Object table) {
            return 0.0f;
        }
    };
    public static Value minWidth = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.2
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            if (cell == null) {
                throw new RuntimeException("minWidth can only be set on a cell property.");
            }
            Object widget = cell.widget;
            if (widget == null) {
                return 0.0f;
            }
            return Toolkit.instance.getMinWidth(widget);
        }
    };
    public static Value minHeight = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.3
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            if (cell == null) {
                throw new RuntimeException("minHeight can only be set on a cell property.");
            }
            Object widget = cell.widget;
            if (widget == null) {
                return 0.0f;
            }
            return Toolkit.instance.getMinHeight(widget);
        }
    };
    public static Value prefWidth = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.4
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            if (cell == null) {
                throw new RuntimeException("prefWidth can only be set on a cell property.");
            }
            Object widget = cell.widget;
            if (widget == null) {
                return 0.0f;
            }
            return Toolkit.instance.getPrefWidth(widget);
        }
    };
    public static Value prefHeight = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.5
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            if (cell == null) {
                throw new RuntimeException("prefHeight can only be set on a cell property.");
            }
            Object widget = cell.widget;
            if (widget == null) {
                return 0.0f;
            }
            return Toolkit.instance.getPrefHeight(widget);
        }
    };
    public static Value maxWidth = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.6
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            if (cell == null) {
                throw new RuntimeException("maxWidth can only be set on a cell property.");
            }
            Object widget = cell.widget;
            if (widget == null) {
                return 0.0f;
            }
            return Toolkit.instance.getMaxWidth(widget);
        }
    };
    public static Value maxHeight = new CellValue() { // from class: com.esotericsoftware.tablelayout.Value.7
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            if (cell == null) {
                throw new RuntimeException("maxHeight can only be set on a cell property.");
            }
            Object widget = cell.widget;
            if (widget == null) {
                return 0.0f;
            }
            return Toolkit.instance.getMaxHeight(widget);
        }
    };

    public abstract float get(Cell cell);

    public abstract float get(Object obj);

    public float width(Object table) {
        return Toolkit.instance.width(get(table));
    }

    public float height(Object table) {
        return Toolkit.instance.height(get(table));
    }

    public float width(Cell cell) {
        return Toolkit.instance.width(get(cell));
    }

    public float height(Cell cell) {
        return Toolkit.instance.height(get(cell));
    }

    public static abstract class CellValue extends Value {
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Object table) {
            throw new UnsupportedOperationException("This value can only be used for a cell property.");
        }
    }

    public static abstract class TableValue extends Value {
        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            return get(cell.getLayout().getTable());
        }
    }

    public static class FixedValue extends Value {
        private float value;

        public FixedValue(float value) {
            this.value = value;
        }

        public void set(float value) {
            this.value = value;
        }

        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Object table) {
            return this.value;
        }

        @Override // com.esotericsoftware.tablelayout.Value
        public float get(Cell cell) {
            return this.value;
        }
    }

    public static Value percentWidth(final float percent) {
        return new TableValue() { // from class: com.esotericsoftware.tablelayout.Value.8
            @Override // com.esotericsoftware.tablelayout.Value
            public float get(Object table) {
                return Toolkit.instance.getWidth(table) * percent;
            }
        };
    }

    public static Value percentHeight(final float percent) {
        return new TableValue() { // from class: com.esotericsoftware.tablelayout.Value.9
            @Override // com.esotericsoftware.tablelayout.Value
            public float get(Object table) {
                return Toolkit.instance.getHeight(table) * percent;
            }
        };
    }

    public static Value percentWidth(final float percent, final Object widget) {
        return new Value() { // from class: com.esotericsoftware.tablelayout.Value.10
            @Override // com.esotericsoftware.tablelayout.Value
            public float get(Cell cell) {
                return Toolkit.instance.getWidth(widget) * percent;
            }

            @Override // com.esotericsoftware.tablelayout.Value
            public float get(Object table) {
                return Toolkit.instance.getWidth(widget) * percent;
            }
        };
    }

    public static Value percentHeight(final float percent, final Object widget) {
        return new TableValue() { // from class: com.esotericsoftware.tablelayout.Value.11
            @Override // com.esotericsoftware.tablelayout.Value
            public float get(Object table) {
                return Toolkit.instance.getHeight(widget) * percent;
            }
        };
    }
}
