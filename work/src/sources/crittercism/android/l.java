package crittercism.android;

import android.content.Context;
import com.crittercism.app.Crittercism;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class l extends j {

    public static class a implements FilenameFilter {
        private String a;

        public a(String str) {
            this.a = new String();
            if (str != null) {
                this.a = str;
            }
        }

        @Override // java.io.FilenameFilter
        public final boolean accept(File file, String str) {
            return str.endsWith(this.a);
        }
    }

    public l() {
        this.a = crittercism.android.a.c.g;
        this.b = new Vector();
    }

    public static l c() {
        String[] list;
        l lVar = new l();
        try {
            Context contextK = Crittercism.a().k();
            String str = contextK.getFilesDir().getAbsolutePath() + "/" + Crittercism.a().r();
            String packageName = contextK.getPackageName();
            if (packageName != null && !packageName.equals("")) {
                File file = new File(str);
                if (file.exists() && (list = file.list(new a(".dmp"))) != null && list.length > 0) {
                    Vector vector = new Vector();
                    for (int i = 0; i < list.length; i++) {
                        String str2 = "filename = " + file.getAbsolutePath() + "/" + list[i];
                        vector.add(file.getAbsolutePath() + "/" + list[i]);
                    }
                    lVar.a(vector);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lVar;
    }

    public final JSONObject a() {
        JSONArray jSONArray;
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        new JSONObject();
        new JSONArray();
        try {
            jSONArray = new JSONArray((Collection) this.b);
        } catch (Exception e) {
            jSONArray = new JSONArray();
        }
        try {
            jSONObject = Crittercism.a().l().c();
            jSONObject.put("app_state", Crittercism.a().l().d());
            jSONObject.put("num_ndk_crashes", this.b.size());
            jSONObject.put("filenames", jSONArray);
            jSONObject.put("filename_prefix", "ndk_crash_");
        } catch (Exception e2) {
            jSONObject = new JSONObject();
        }
        try {
            jSONObject2.put("requestUrl", this.a);
            jSONObject2.put("requestData", jSONObject);
            return jSONObject2;
        } catch (Exception e3) {
            return new JSONObject();
        }
    }

    public final void b() {
        try {
            if (this.b != null) {
                for (String str : this.b) {
                    String str2 = "deleting filename: " + str;
                    new File(str).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
