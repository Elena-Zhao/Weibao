package com.example.find;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.connectwebservice.ImageHandeller;
import com.example.entity.McItem;
import com.example.entity.McPreferentialItem;
import com.example.myfirstandroid.R;

public class MyPostInfoListAdapter extends BaseAdapter {
	private List<McPreferentialItem> list;
	private LayoutInflater mInflater;

	public MyPostInfoListAdapter(List<McPreferentialItem> list, Context context) {
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	class ViewHolder {
		private ImageView pref_image;
		private TextView pref_mc_name;
		private TextView pref_title;

		private LinearLayout Layout;

		public ImageView getPref_image() {
			return pref_image;
		}

		public void setPref_image(ImageView pref_image) {
			this.pref_image = pref_image;
		}

		public TextView getPref_mc_name() {
			return pref_mc_name;
		}

		public void setPref_mc_name(TextView pref_mc_name) {
			this.pref_mc_name = pref_mc_name;
		}

		public TextView getPref_title() {
			return pref_title;
		}

		public void setPref_title(TextView pref_title) {
			this.pref_title = pref_title;
		}

		public LinearLayout getLayout() {
			return this.Layout;
		}

		public void setLayout(LinearLayout layout) {
			this.Layout = layout;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.preferential_item, null);
			holder.setLayout((LinearLayout) convertView
					.findViewById(R.id.pref_layout));
			holder.setPref_mc_name((TextView) convertView.findViewById(R.id.pref_mc_name));
			holder.setPref_title((TextView) convertView
					.findViewById(R.id.pref_title));
			
			holder.setPref_image((ImageView) convertView.findViewById(R.id.pref_image));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.getPref_mc_name().setText(list.get(position).getMcName());
		holder.getPref_title().setText(list.get(position).getPrefTitle());
		holder.getPref_image().setImageBitmap(ImageHandeller.getBitmap(list.get(position).getPrefImageUrl()));

		if (position % 2 == 0) {
			holder.getLayout().setBackgroundResource(R.drawable.mcitem_bg1);
		} else
			holder.getLayout().setBackgroundResource(R.drawable.mcitem_bg);
		return convertView;
	}

}
