package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Annotations implements Serializable {
    public static final int lengthLimit = 512;
    private static final long serialVersionUID = 7928827620306593741L;
    private List<Annotation> annotations = null;

    public Annotations() {
        setAnnotations(null);
    }

    public Annotations(List<Annotation> annotations) {
        setAnnotations(annotations);
    }

    Annotations(JSONArray jsonArray) {
        setAnnotations(null);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                addAnnotation(new Annotation(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                this.annotations.clear();
                return;
            }
        }
    }

    public List<Annotation> getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        if (annotations == null) {
            annotations = new ArrayList<>();
        }
        this.annotations = annotations;
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    public Annotations annotation(Annotation annotation) {
        addAnnotation(annotation);
        return this;
    }

    public boolean isEmpty() {
        return this.annotations.isEmpty();
    }

    public Integer size() {
        return new Integer(this.annotations.size());
    }

    public static boolean isExceedingLengthLimit(Annotations annotations) {
        return annotations.asParameterValue().length() > 512;
    }

    public boolean isExceedingLengthLimit() {
        return isExceedingLengthLimit(this);
    }

    String asParameterValue() {
        JSONArray jsonArray = new JSONArray();
        for (Annotation annotation : this.annotations) {
            jsonArray.put(annotation.asJSONObject());
        }
        return jsonArray.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this != obj) {
            return (obj instanceof Annotations) && ((Annotations) obj).getSortedAnnotations().equals(getSortedAnnotations());
        }
        return true;
    }

    public int hashCode() {
        return getSortedAnnotations().hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Annotations{");
        for (int i = 0; i < size().intValue(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.annotations.get(i).toString());
        }
        sb.append('}');
        return sb.toString();
    }

    private List<Annotation> getSortedAnnotations() {
        List<Annotation> sortedAnnotations = new ArrayList<>(size().intValue());
        sortedAnnotations.addAll(this.annotations);
        Collections.sort(sortedAnnotations);
        return sortedAnnotations;
    }
}
