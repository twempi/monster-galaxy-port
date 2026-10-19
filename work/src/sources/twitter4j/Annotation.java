package twitter4j;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Annotation implements Comparable<Annotation>, Serializable {
    private static final long serialVersionUID = -6515375141284988754L;
    private Map<String, String> attributes;
    private String type;

    @Override // java.lang.Comparable
    public int compareTo(Annotation annotation) {
        return compareTo2(annotation);
    }

    public Annotation(String type) {
        this.type = null;
        this.attributes = null;
        setType(type);
        setAttributes(null);
    }

    public Annotation(String type, Map<String, String> attributes) {
        this.type = null;
        this.attributes = null;
        setType(type);
        setAttributes(attributes);
    }

    Annotation(JSONObject jsonObject) {
        this.type = null;
        this.attributes = null;
        String typ = null;
        Map<String, String> attrs = null;
        Iterator<String> itKeys = jsonObject.keys();
        if (itKeys.hasNext()) {
            String typ2 = itKeys.next();
            typ = typ2;
            if (itKeys.hasNext()) {
                this.type = null;
            } else {
                try {
                    JSONObject jo = jsonObject.getJSONObject(typ);
                    Map<String, String> attrs2 = new LinkedHashMap<>();
                    try {
                        Iterator<String> itKeys2 = jo.keys();
                        while (itKeys2.hasNext()) {
                            String key = itKeys2.next();
                            String value = jo.getString(key);
                            attrs2.put(key, value);
                        }
                        attrs = attrs2;
                    } catch (JSONException e) {
                        typ = null;
                        attrs = null;
                    }
                } catch (JSONException e2) {
                }
            }
        }
        setType(typ);
        setAttributes(attrs);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        if (type == null) {
            type = "";
        }
        this.type = type;
    }

    public Annotation type(String type) {
        setType(type);
        return this;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        this.attributes = attributes;
    }

    public Annotation attributes(Map<String, String> attributes) {
        setAttributes(attributes);
        return this;
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }

    public Annotation attribute(String name, String value) {
        addAttribute(name, value);
        return this;
    }

    public boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    public Integer size() {
        return new Integer(this.attributes.size());
    }

    String asParameterValue() {
        return asJSONObject().toString();
    }

    JSONObject asJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(this.type, (Map) this.attributes);
        } catch (JSONException e) {
        }
        return jsonObject;
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(Annotation other) {
        if (other == null) {
            return 1;
        }
        if (this == other) {
            return 0;
        }
        int result = getType().compareTo(other.getType());
        if (result == 0) {
            int result2 = size().compareTo(other.size());
            if (result2 == 0) {
                Iterator<String> otherNamesIt = other.sortedNames().iterator();
                for (String thisName : sortedNames()) {
                    String otherName = otherNamesIt.next();
                    int result3 = thisName.compareTo(otherName);
                    if (result3 == 0) {
                        String thisValue = getAttributes().get(thisName);
                        String otherValue = other.getAttributes().get(otherName);
                        int result4 = thisValue.compareTo(otherValue);
                        if (result4 != 0) {
                            return result4;
                        }
                    } else {
                        return result3;
                    }
                }
                return 0;
            }
            return result2;
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Annotation)) {
            return false;
        }
        Annotation other = (Annotation) obj;
        return getType().equals(other.getType()) && getAttributes().equals(other.getAttributes());
    }

    public int hashCode() {
        return (this.type.hashCode() * 31) + this.attributes.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Annotation{type='");
        sb.append(this.type).append("', attributes={");
        Iterator<String> nameIt = this.attributes.keySet().iterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            String value = this.attributes.get(name);
            sb.append('\'').append(name).append("'='").append(value).append('\'');
            if (nameIt.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}}");
        return sb.toString();
    }

    private SortedSet<String> sortedNames() {
        SortedSet<String> names = new TreeSet<>();
        names.addAll(getAttributes().keySet());
        return names;
    }
}
