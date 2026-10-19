package com.kontagent.queue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.kontagent.KontagentLog;
import com.kontagent.Stateful;
import com.sessionm.ui.SessionMActivity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class OfflineQueue extends Stateful implements KontagentQueue {
    private static final String TAG = OfflineQueue.class.getSimpleName();
    private final Context context;
    private DataHelper data;

    public OfflineQueue(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override // com.kontagent.Stateful
    protected void onStart() {
        KontagentLog.d(TAG, "onStart()");
        this.data = new DataHelper(this.context);
    }

    @Override // com.kontagent.Stateful
    protected void onStop() {
        KontagentLog.d(TAG, "onStop()");
        this.data.close();
        this.data = null;
    }

    private void changed() {
        setChanged();
        notifyObservers();
    }

    @Override // com.kontagent.queue.KontagentQueue
    public boolean enqueue(String url) {
        KontagentLog.d(TAG, "enqueue(): " + url);
        assertStarted();
        this.data.insert(url);
        changed();
        return true;
    }

    @Override // com.kontagent.queue.KontagentQueue
    public boolean enqueue(Collection<String> urls) {
        KontagentLog.d(TAG, "enqueue(Collection)");
        assertStarted();
        this.data.bulkInsert(urls);
        changed();
        return true;
    }

    public void clear() {
        KontagentLog.d(TAG, "clear()");
        assertStarted();
        this.data.deleteAll();
        changed();
    }

    public int size() {
        assertStarted();
        return this.data.size();
    }

    public List<String> all() {
        assertStarted();
        return this.data.selectAll();
    }

    public void convertToOnline(OnlineQueue onlineQueue) {
        KontagentLog.d(TAG, "convertToOnline()");
        assertStarted();
        onlineQueue.enqueue(this.data.selectAll());
        this.data.deleteAll();
        changed();
    }

    private static class DataHelper {
        private static final String DATABASE_NAME = "Kontagent.db";
        private static final int DATABASE_VERSION = 1;
        private static final String INSERT = "insert into api_calls(url) values (?)";
        private static final String TABLE_NAME = "api_calls";
        private final SQLiteDatabase db;
        private final SQLiteStatement insertStmt;

        public DataHelper(Context context) {
            OpenHelper openHelper = new OpenHelper(context);
            this.db = openHelper.getWritableDatabase();
            this.insertStmt = this.db.compileStatement(INSERT);
        }

        public void close() {
            this.db.close();
            this.insertStmt.close();
        }

        public long insert(String url) {
            this.insertStmt.bindString(1, url);
            return this.insertStmt.executeInsert();
        }

        public void bulkInsert(Collection<String> urls) {
            this.db.beginTransaction();
            try {
                for (String url : urls) {
                    insert(url);
                }
                this.db.setTransactionSuccessful();
            } finally {
                this.db.endTransaction();
            }
        }

        public void deleteAll() {
            try {
                this.db.delete(TABLE_NAME, null, null);
            } catch (SQLiteDiskIOException ex) {
                Log.e(OfflineQueue.TAG, "Failed to delete offline records.", ex);
            }
        }

        public int size() {
            Cursor cursor = this.db.query(TABLE_NAME, new String[]{SessionMActivity.INTENT_PARAM_URL}, null, null, null, null, "id asc");
            int size = cursor.getCount();
            cursor.close();
            return size;
        }

        public List<String> selectAll() {
            List<String> list = new ArrayList<>();
            Cursor cursor = this.db.query(TABLE_NAME, new String[]{SessionMActivity.INTENT_PARAM_URL}, null, null, null, null, "id asc");
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return list;
        }

        private static class OpenHelper extends SQLiteOpenHelper {
            OpenHelper(Context context) {
                super(context, DataHelper.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
            }

            @Override // android.database.sqlite.SQLiteOpenHelper
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE api_calls (id INTEGER PRIMARY KEY, url TEXT)");
            }

            @Override // android.database.sqlite.SQLiteOpenHelper
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        }
    }
}
