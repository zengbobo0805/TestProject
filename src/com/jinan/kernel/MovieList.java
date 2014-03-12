package com.jinan.kernel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MovieList extends Activity {
	private Button btnplay = null;
	private final static int UPDATE = Menu.FIRST;
	private final static int QUIT = Menu.FIRST + 1;
	private ListView lv;
	private ArrayList<Map<String, String>> name;
	private String path = Environment.getExternalStorageDirectory().getPath();// +"/movie.mp4";
	private String file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		btnplay = (Button) findViewById(R.id.btnplay);
		setTitle(path);
		lv = (ListView) findViewById(R.id.lv);
		name = new ArrayList<Map<String, String>>();
		if (FileUtils.isExternalStorageState()) {
			File path = FileUtils.externalStorageDirectory();// 获得SD卡路径
			// File path = new File("/mnt/sdcard/");
			File[] files = path.listFiles();// 读取
			getFileName(files);

		}
		SimpleAdapter adapter = new SimpleAdapter(this, name, R.layout.sd_list,
				new String[] { "视频名称" }, new int[] { R.id.mp4 });
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// setTitle(lv.getItemAtPosition(position).toString()+":"+String.valueOf(lv.getItemIdAtPosition(position)));
				String str = lv.getItemAtPosition(position).toString();
				System.out.println("MovieList onItemClick str:"+str);
				file = str.substring("{视频名称=".length(), str.lastIndexOf("}"));
				String moviename = path + "/" + file;
				// setTitle(moviename+":"+lv.getItemAtPosition(position).toString());
				Intent intent = new Intent(MovieList.this, Myvideoplayer.class);
				intent.putExtra("moviename", moviename);
				startActivity(intent);

			}
		});

		btnplay.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MovieList.this, Myvideoplayer.class);
				intent.putExtra("moviename", path);
				startActivity(intent);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int i = Menu.FIRST;
		menu.add(i, UPDATE, i, "更新列表");
		menu.add(i, QUIT, i, "退出");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case UPDATE:
			File path = Environment.getExternalStorageDirectory();// 获得SD卡路径
			File[] files = path.listFiles();// 读取
			getFileName(files); // 更新列表
			break;

		case QUIT: // 退出
			this.finish();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void getFileName(File[] files) { // 仅搜索当前目录下的文件
		if (files != null) {// 先判断目录是否为空，否则会报空指针
			for (File file : files) {
				String fileName = file.getName();
				if (fileName.endsWith(".mp4") || fileName.endsWith(".3gp")) {
					HashMap<String, String> map = new HashMap<String, String>();
					String s = fileName.substring(0, fileName.lastIndexOf("."))
							.toString();
					Log.i("zeng", "文件名mp4或3gp: " + s);
					map.put("视频名称", fileName);
					name.add(map);
				}
			}
		}
		// }
	}

}