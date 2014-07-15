package com.example.find;

import java.util.List;

import com.example.connectwebservice.ImageHandeller;
import com.example.entity.McItem;
import com.example.myfirstandroid.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class MyListAdapter extends BaseAdapter{
	private List<McItem> list;
	private LayoutInflater mInflater;
	private boolean needUpdate;
	private McActivity context;
	
	public MyListAdapter(List<McItem> data, McActivity context, boolean need_update) {
		this.list = data;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.needUpdate = need_update;
	}

	class onSubscriptBtnClick implements OnClickListener{
		private int position;
		public onSubscriptBtnClick(int pos){
			position = pos;
		}
		@Override
		public void onClick(View view){
			Button btn = (Button) view;
			context.changeSubscribe(btn, position);
		}
	}
	
	class ViewHolder {
		private ImageView mcLogo;
		private TextView mcName;
		private TextView mcAddress;
		private TextView mcIntro;
		private Button subsBtn;
		private RelativeLayout Layout;
		public TextView getMcAddress() {
			return mcAddress;
		}
		public void setMcAddress(TextView mcAddress) {
			this.mcAddress = mcAddress;
		}
		public TextView getMcIntro() {
			return mcIntro;
		}
		public void setMcIntro(TextView mcIntro) {
			this.mcIntro = mcIntro;
		}
		
		public Button getSubsBtn() {
			return subsBtn;
		}
		public void setSubsBtn(Button subsBtn) {
			this.subsBtn = subsBtn;
		}
		
		         
		public TextView getmcName() {
		    return mcName;
		}
		public void setmcName(TextView mcName) {
		    this.mcName = mcName;
		}
		public ImageView getMcLogo() {
			return mcLogo;
		}
		public void setMcLogo(ImageView mcLogo) {
			this.mcLogo = mcLogo;
		}
		public RelativeLayout getLayout() {
		        return this.Layout;
		}
		public void setLayout(RelativeLayout layout) {
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
			convertView = mInflater
					.inflate(R.layout.merchant_item, null);
			holder.setLayout((RelativeLayout) convertView
					.findViewById(R.id.mc_layout));
			holder.setmcName((TextView) convertView
					.findViewById(R.id.mc_name));
			holder.setMcAddress((TextView) convertView
					.findViewById(R.id.mc_address));
			holder.setMcIntro((TextView) convertView
					.findViewById(R.id.mc_intro));
			holder.setMcLogo((ImageView) convertView
					.findViewById(R.id.mc_logo));
			holder.setSubsBtn((Button) convertView
					.findViewById(R.id.mc_subs_btn));
		 
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		 
	        holder.getmcName().setText(list.get(position).getMcName());
	        holder.getMcAddress().setText(list.get(position).getMcAddress());
	        holder.getMcIntro().setText(list.get(position).getMcIntro());
	        holder.getMcLogo().setImageBitmap(ImageHandeller.getBitmap(list.get(position).getMcLogoUrl()));
	        
	        if(list.get(position).getIsSubscribed())
	        	holder.getSubsBtn().setSelected(true);
	        else
	        	holder.getSubsBtn().setSelected(false);
	        holder.getSubsBtn().setOnClickListener(new onSubscriptBtnClick(position));
	        
		    if (position%2 == 0) {
		        holder.getLayout().setBackgroundResource(R.drawable.mcitem_bg1);
		    } else 
		    	holder.getLayout().setBackgroundResource(R.drawable.mcitem_bg);
		    return convertView;
	}

}
