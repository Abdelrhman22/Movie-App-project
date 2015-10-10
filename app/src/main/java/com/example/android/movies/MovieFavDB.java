package com.example.android.movies;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieFavDB {

	public static final String MYDATABASE_NAME = "Movies";
	public static final String MYDATABASE_TABLE = "moviefav";
	public static final int MYDATABASE_VERSION = 1;
	public static final String id = "_id", image = "image",
			title = "title", overview = "overview",
			vote_average = "vote_average", release_date = "release_date" ;

	// create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE + "(" + id + " text , "
			+ image + " text, " + title + " text, " + overview
			+ " text, " + vote_average + " text, " + release_date + " text);";

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;

	public MovieFavDB(Context c) {

		context = c;
	}

	public MovieFavDB openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public MovieFavDB openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}

	@SuppressWarnings("finally")
	public long insert(GridItem content) {
		if (!isMovieFound(content.getId())) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(id, content.getId());
			contentValues.put(image, content.getImage());
			contentValues.put(title, content.getTitle());
			contentValues.put(overview, content.getOverview()); // 0 or 1

			contentValues.put(release_date, content.getRelease_date());


			contentValues.put(vote_average, content.getVote_average());

			try {
				// sqLiteDatabase.beginTransaction();
				sqLiteDatabase.insertOrThrow(MYDATABASE_TABLE, null,
						contentValues);
				// sqLiteDatabase.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			} finally {
				// sqLiteDatabase.endTransaction();
				return 1;
			}

		} else
			return -2; // found it
	}

	public int deleteAll() {
		return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
	}

	public int delete(String id) {
		if (isMovieFound(id)) {
			return sqLiteDatabase.delete(MYDATABASE_TABLE, id + "='" + id
					+ "'", null);
		}
		return -1;
	}




	public ArrayList<GridItem> getMovieFavList() {
		ArrayList<GridItem> ProdFav = new ArrayList<GridItem>();

		String[] columns = new String[] { id, image, title,
				overview, vote_average, release_date  };

		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null,
				null, null, null, null);
		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				GridItem favItem = new GridItem();
				favItem.setId(cursor.getString(0));
				favItem.setImage(cursor.getString(1));
				favItem.setTitle(cursor.getString(2));
				favItem.setOverview(cursor.getString(3));
				favItem.setVote_average(cursor.getString(4));
				favItem.setRelease_date(cursor.getString(5));

				cursor.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			cursor.close();
		}
		return ProdFav;
	}

	public boolean isMovieFound(String favId) {
		String[] columns = new String[] { id};
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, id
				+ "='" + favId + "' ",
				null, null, null, null);
		int index = 0;
		try {
			index = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return index > 0 ? true : false;
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			// db.deleteDatabase(new File(MYDATABASE_NAME));

			db.execSQL(SCRIPT_CREATE_DATABASE);
			// db.deleteDatabase(file)
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

}
