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
		// ͨ����ȡ�ֻ�ͨѶ¼������
		dataArray = new ArrayList<String>();

		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		ContentResolver resolver = this.getContentResolver();
		// ��query����һ��SORT_KEY_PRIMARY����ContentResolver����õ����ݰ�����ϵ����������ĸ����
		Cursor cursor = resolver.query(uri, null, null, null,
				android.provider.ContactsContract.Contacts.SORT_KEY_PRIMARY);
		while (cursor.moveToNext())

		{
			// ��ϵ�˵�id
			String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
			// ����ϵ�˰���������ĸ����
			String sort_key_primary = cursor
					.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.SORT_KEY_PRIMARY));
			// ��ȡ��ϵ�˵�����
			String name = cursor
					.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
			dataArray.add(name);
		}

		LetterListView letterListView = (LetterListView) findViewById(R.id.letterListView);
		letterListView.setAdapter(new TestAdapter());

	}

	/**
	 * ���� ʹ��һ���򵥵� NameValuePair ����,��Ϊ����
	 * 
	 * @Title:
	 * @Description:
	 * @Author:Justlcw
	 * @Since:2014-5-13
	 * @Version:
	 */
	class TestAdapter extends LetterBaseListAdapter<NameValuePair> {
		/** ��ĸ��Ӧ��key,��Ϊ��ĸ��Ҫ���뵽�б��е�,Ϊ������,������ĸ��item��ʹ��ͬһ��key. **/
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
			// �ж��ǲ�����ĸ��,ͨ��key�Ƚ�,������NameValuePair����,��������,�������Լ�������ô�ж���.
			return t.getName().equals(LETTER_KEY);
		}

		@Override
		public View getLetterView(int position, View convertView, ViewGroup parent) {
			// ��������ĸ��item��������.
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
			// �����������������ݵ�item��������.
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
