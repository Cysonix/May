package cn.edu.cuc.logindemo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据操作管理类封装
 *
 * @author SongQing
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public Context context;
	public final static int version = 1;
	private static final String DB_NAME = "demo.db"; // 数据库文件名
	//	private static final String DB_NAME="/mnt/sdcard/com.miti.demo/databases/demo.db";
	private final static String TAG= "SQLiteHelper";
	private static SQLiteDatabase sda;
//	private static SQLiteHelper instance;

//	public static synchronized SQLiteHelper getHelper(Context context) {
//		if (instance == null) {
//			instance = new SQLiteHelper(context);
//		}
//		return instance;
//	}

	/**
	 *
	 * @param context
	 *            上下文环境
	 * @param dbname
	 *            要创建的数据库名称
	 * @param version
	 *            要创建的数据库版本
	 */
	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, version);
		this.context = context;
	}

	/**
	 * 在数据库第一次生成的时候会调用这个方法，一般用于生成数据库表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	/**
	 * 当数据库需要升级的时候，Android系统会主动的调用这个方法。
	 * 在这个方法里边删除数据表，并建立新的数据表，当然是否还需要做其他的操作，完全取决于应用的需求
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 打开数据库时创建数据库帮助类 获取写的失败时就获取读的
	 */
	public void open() {
		try {
			if (sda == null || !sda.isOpen()) {
				// helper = new SQLiteHelper(this.context, this.version);
				sda = this.getWritableDatabase();
			}
		} catch (Exception e) {
			sda = this.getReadableDatabase();
		}
	}

	/**
	 * 关闭写数据库对象
	 */
	public void close() {
//		 if(sda != null){
//		 sda.close();
//		 sda = null ;
//		 }
	}

	/**
	 *
	 * @param sql
	 *            执行插入操作的sql语句
	 * @param params
	 *            将插入的字段封装到一个Object[]中
	 * @return 成功返回1
	 */
	public int insert(String sql, Object[] params) {
		open();
		try {
			sda.execSQL(sql, params);

		} catch (SQLException e) {
			e.printStackTrace();
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 * 要求返回行id的数据添加
	 *
	 * @param table
	 *            表名
	 * @param nullColumnHack
	 *            当values参数为空或者里面没有内容的时候，会将改行自动设置为null再插入
	 * @param values
	 *            ContentValues对象，类似一个map.通过键值对的形式存储值。
	 * @return 插入行的id
	 */
	public int insert(String table, String nullColumnHack, ContentValues values) {
		open();
		try {
			return (int) sda.insert(table, nullColumnHack, values);

		} catch (SQLException e) {
			e.printStackTrace();
			return e.hashCode();
		} finally {
			close();
		}
	}

	/**
	 *
	 * @param sql
	 *            执行更新操作的sql语句
	 * @param params
	 *            将插入的字段封装到一个Object[]中
	 * @return 成功返回1
	 */
	public int update(String sql, Object[] params) {
		open();
		try {
			sda.execSQL(sql, params);
		} catch (SQLException e) {
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 *
	 * @param sql
	 *            执行更新操作的sql语句
	 * @param params
	 *            将插入的字段封装到一个Object[]中
	 * @return 成功返回1
	 */
	public int updateBySqlStatement(String sql) {
		open();
		try {
			sda.execSQL(sql);
		} catch (SQLException e) {
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 *
	 * @param sql
	 *            执行按条件查询操作的sql语句
	 * @param selectionArgs将插入的字段封装到一个String
	 *            []中
	 * @return 返回一个游标对象
	 */
	public Cursor findQuery(String sql, String[] selectionArgs) {
		open();
		Cursor cursor = sda.rawQuery(sql, selectionArgs);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		close();
		return cursor;
	}

	/**
	 *
	 * @param sql
	 *            执行查询操作的sql语句
	 * @return 返回一个游标对象
	 */
	public Cursor findQuery(String sql) {
		open();
		Cursor cursor = sda.rawQuery(sql, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		close();
		return cursor;
	}

	/**
	 * 按条件获取数据
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	public Cursor findQuery(String table, String[] columns, String selection,
							String[] selectionArgs, String sortOrder) {
		open();
		Cursor cursor = sda.query(table, columns, selection, selectionArgs, null, null, sortOrder);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		close();
		return cursor;
	}

	/**
	 *
	 * @param sql
	 *            执行删除操作的sql语句
	 */
	public int deleteAll(String sql) {
		open();

		try {
			sda.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.hashCode();
		} finally {
			close();
		}

		return 1;
	}

	/**
	 *
	 * @param sql
	 *            执行按条件删除操作的sql语句
	 * @param id
	 *            传入要删除的id
	 * @return 成功返回1
	 */
	public int deleteOne(String sql, int id) {
		open();
		Object[] params = { id };
		try {
			sda.execSQL(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}

	/**
	 *
	 * @param sql
	 *            执行按条件删除操作的sql语句
	 * @param params
	 *            传入要删除的条件
	 * @return 成功返回1
	 */
	public int delete(String sql, Object[] params) {
		open();
		try {
			sda.execSQL(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.hashCode();
		} finally {
			close();
		}
		return 1;
	}


	/**
	 *
	 * @param sql
	 *            查询语句sql
	 * @return 返回查询记录条数
	 */
	public int rowCount(String sql) {
		open();
		try {
			Cursor cursor = findQuery(sql);
			return cursor.getCount();
		} catch (SQLException e) {
			Log.e(TAG,e.toString());
			return -1;
		} finally {
			close();
		}
	}

	public SQLiteDatabase GetSQLiteDatabase() {
		open();
		return sda;
	}

}
