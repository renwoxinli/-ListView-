package com.lixu.letterlistview;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.lixu.letterlistview.letter.LetterBaseListAdapter;
import com.lixu.letterlistview.letter.LetterListView;
import com.lixu.lianxirenlist.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ArrayList<String> dataArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 通过获取手机通讯录的姓名
		dataArray = new ArrayList<String>();

		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		ContentResolver resolver = this.getContentResolver();
		// 给query传递一个SORT_KEY_PRIMARY，让ContentResolver将获得的数据按照联系人名字首字母排序
		Cursor cursor = resolver.query(uri, null, null, null,
				android.provider.ContactsContract.Contacts.SORT_KEY_PRIMARY);
		while (cursor.moveToNext())

		{
			// 联系人的id
			String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
			// 将联系人按姓名首字母分组
			String sort_key_primary = cursor
					.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.SORT_KEY_PRIMARY));
			// 获取联系人的名字
			String name = cursor
					.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
			dataArray.add(name);
		}

		LetterListView letterListView = (LetterListView) findViewById(R.id.letterListView);
		letterListView.setAdapter(new TestAdapter());

	}

	/**
	 * 这里 使用一个简单的 NameValuePair 对象,做为测试
	 * 
	 * @Title:
	 * @Description:
	 * @Author:Justlcw
	 * @Since:2014-5-13
	 * @Version:
	 */
	class TestAdapter extends LetterBaseListAdapter<NameValuePair> {
		/** 字母对应的key,因为字母是要插入到列表中的,为了区别,所有字母的item都使用同一的key. **/
		private static final String LETTER_KEY = "letter";

		public TestAdapter() {
			super();

			List<NameValuePair> dataList = new ArrayList<NameValuePair>();
			for (int i = 0; i < dataArray.size(); i++) {
				NameValuePair pair = new BasicNameValuePair(String.valueOf(i), dataArray.get(i));
				dataList.add(pair);
			}
			setContainerList(dataList);
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public String getItemString(NameValuePair t) {
			return t.getValue();
		}

		@Override
		public NameValuePair create(char letter) {
			return new BasicNameValuePair(LETTER_KEY, String.valueOf(letter));
		}

		@Override
		public boolean isLetter(NameValuePair t) {
			// 判断是不是字母行,通过key比较,这里是NameValuePair对象,其他对象,就由你自己决定怎么判断了.
			return t.getName().equals(LETTER_KEY);
		}

		@Override
		public View getLetterView(int position, View convertView, ViewGroup parent) {
			// 这里是字母的item界面设置.
			if (convertView == null) {
				convertView = new TextView(MainActivity.this);
				((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
				convertView.setBackgroundColor(getResources().getColor(android.R.color.white));
			}
			((TextView) convertView).setText(list.get(position).getValue());
			((TextView) convertView).setBackgroundColor(Color.GREEN);
			((TextView) convertView).setTextSize(25);
			return convertView;
		}

		@Override
		public View getContainerView(int position, View convertView, ViewGroup parent) {
			// 这里是其他正常数据的item界面设置.
			if (convertView == null) {
				convertView = new TextView(MainActivity.this);
				((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
			}
			((TextView) convertView).setText(list.get(position).getValue());
			((TextView) convertView).setBackgroundColor(Color.YELLOW);
			((TextView) convertView).setTextSize(20);

			return convertView;
		}
	}
}
