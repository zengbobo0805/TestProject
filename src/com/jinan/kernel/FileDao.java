package com.jinan.kernel;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

public class FileDao {

	public static boolean save(Context mContext, String fileName, String content) {
		byte[] buf;
		FileOutputStream fos = null;
		try {
			buf = fileName.getBytes("iso8859-1");
			fileName = new String(buf, "utf-8");
			// Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
			// Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
			// Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
			// MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
			// 如果希望文件被其他应用读和写，可以传入：
			// openFileOutput(fileName, Context.MODE_WORLD_READABLE +
			// Context.MODE_WORLD_WRITEABLE);
			fos = mContext.openFileOutput(fileName, mContext.MODE_PRIVATE);
			fos.write(content.getBytes());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static String read(Context mContext, String fileName) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			fis = mContext.openFileInput(fileName);
			baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = fis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			return baos.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
				baos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
