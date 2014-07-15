package com.example.me;

import java.util.List;

import com.example.connectwebservice.ImageHandeller;
import com.example.entity.McItem;
import com.example.entity.WeiJinEntity;
import com.example.find.McActivity;
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
public class MyBillListAdapter extends BaseAdapter{
	private List<WeiJinEntity> list;
	private LayoutInflater mInflater;
	private boolean needUpdate;
	private bill4thTabActivity context;
	
	public MyBillListAdapter(List<WeiJinEntity> data, bill4thTabActivity context, boolean need_update) {
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
		//private ImageView mcLogo;
		private TextView storeName;
		private TextView billAmount;
		private TextView billIntro;
		private Button exchangeBtn;
		public TextView getStoreName() {
			return storeName;
		}
		public void setStoreName(TextView storeName) {
			this.storeName = storeName;
		}
		public TextView getBillAmount() {
			return billAmount;
		}
		public void setBillAmount(TextView billAmount) {
			this.billAmount = billAmount;
		}
		public TextView getBillIntro() {
			return billIntro;
		}
		public void setBillIntro(TextView billIntro) {
			this.billIntro = billIntro;
		}
		public Button getExchangeBtn() {
			return exchangeBtn;
		}
		public void setExchangeBtn(Button exchangeBtn) {
			this.exchangeBtn = exchangeBtn;
		}
		public RelativeLayout getLayout() {
			return Layout;
		}
		public void setLayout(RelativeLayout layout) {
			Layout = layout;
		}
		private RelativeLayout Layout;
		
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
					.inflate(R.layout.bill_item, null);
			holder.setLayout((RelativeLayout) convertView
					.findViewById(R.id.bill_layout));
			holder.setStoreName((TextView) convertView
					.findViewById(R.id.store_name));
			holder.setBillAmount((TextView) convertView
					.findViewById(R.id.bill_amount));
			holder.setBillIntro((TextView) convertView
					.findViewById(R.id.bill_intro));
			holder.setExchangeBtn((Button) convertView
					.findViewById(R.id.exchange_bill_btn));
		 
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		 
	        holder.getStoreName().setText(list.get(position).getStoreName());
	        holder.getBillAmount().setText(Float.toString(list.get(position).getAmount()));
	        holder.getBillIntro().setText(list.get(position).getBriefInfoString());
	        //holder.getMcLogo().setImageBitmap(ImageHandeller.getBitmap(list.get(position).getMcLogoUrl()));
	        
	        if(list.get(position).getIsExchanged())
	        	holder.getExchangeBtn().setSelected(true);
	        else
	        	holder.getExchangeBtn().setSelected(false);
	        holder.getExchangeBtn().setOnClickListener(new onSubscriptBtnClick(position));
	        
		    if (position%2 == 0) {
		        holder.getLayout().setBackgroundResource(R.drawable.mcitem_bg1);
		    } else 
		    	holder.getLayout().setBackgroundResource(R.drawable.mcitem_bg);
		    return convertView;
	}

}
