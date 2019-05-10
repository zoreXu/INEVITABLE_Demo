package com.example.jian2020.nuc.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.lang.ref.PhantomReference;

public class DatabaseProvider extends ContentProvider {

    private static final int BOOK_DIR = 0;

    private static final int BOOK_ITEM = 1;

    private static final int CATEGORY_DIR = 2;

    private static final int CATEGORY_ITEM = 3;

    private static final String AUTHRITY = "com.example.jian2020.nuc.databasetest.provider";

    private static UriMatcher uriMatcher = null;

    private MyDatabaseHelper dbHelpr = null;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHRITY, "book", BOOK_DIR);
        //访问Book表中所有的数据
        uriMatcher.addURI(AUTHRITY, "book/#", BOOK_ITEM);
        //访问Book表中的单条数据
        uriMatcher.addURI(AUTHRITY, "category", CATEGORY_DIR);
        //访问Category中的所有数据
        uriMatcher.addURI(AUTHRITY, "category/#", CATEGORY_ITEM);
        //访问Category中的单条数据
    }

    public DatabaseProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelpr = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //删除数据
        // Implement this to handle requests to delete one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db = dbHelpr.getWritableDatabase();

        int deleteRows = 0;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Book", "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Category", "id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        //throw new UnsupportedOperationException("Not yet implemented");
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/"
                        + "vnd.com.example.jian2020.nuc.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/"
                        + "vnd.com.example.jian2020.nuc.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/"
                        + "vnd.com.example.jian2020.nuc.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/"
                        + "vnd.com.example.jian2020.nuc.databasetest.provider.category";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //添加数据
        // TODO: Implement this to handle requests to insert a new row.
        // throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db = dbHelpr.getWritableDatabase();

        Uri uriReturn = null;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBooKId = db.insert("Book", null, values);
                uriReturn = Uri.parse("content://" + AUTHRITY + "/book/" + newBooKId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                uriReturn = Uri.parse("content://" + AUTHRITY + "/category/" + newCategoryId);
                break;
        }
        return uriReturn;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //查询数据
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db = dbHelpr.getReadableDatabase();
        //获取SQLiteDatabase实例
        Cursor cursor = null;

        switch (uriMatcher.match(uri)){//利用uri中判断用户访问那张表
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[]{bookId},
                        null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection,
                        selectionArgs,null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", projection, "id = ?", new String[]{categoryId},
                        null, null, sortOrder);
                break;
           default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //更新数据
        // TODO: Implement this to handle requests to update one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db = dbHelpr.getWritableDatabase();

        int updateRows = 0;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book", values,"id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category", values, "id = ?", new String[]{categoryId});
                break;
            default:
               break;
        }
        return updateRows;
    }
}
