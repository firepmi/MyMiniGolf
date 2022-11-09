package com.wddonline.minigolf.myminigolfscorecard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter
{
	Context activityContext;
	public ArrayList<String> dataArray;
	public ArrayList<String> parArray;
	public MenuAdapter(Context context, ArrayList<String> list){
		activityContext = context;
		dataArray = list;
	}
	public MenuAdapter(Context context, ArrayList<String> list, ArrayList<String> parList){
		activityContext = context;
		dataArray = list;
		parArray = parList;
	}
	@Override
	public int getCount()
	{
		return dataArray.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}
	public void resetList(List<String> list){
		dataArray.clear();
		dataArray = new ArrayList<>(list);
		notifyDataSetChanged();
	}
	public void refreshArray(ArrayList<String> list){
		dataArray = list;
		this.notifyDataSetInvalidated();
	}
	@SuppressLint("InflateParams")
	@Override
	public View getView(int pos, View v, ViewGroup arg2)
	{
		Log.w("Notification", "Notification Adapter Getview " + pos);
		LayoutInflater mInflater = (LayoutInflater)
				activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder holder = null;

		if (v == null) {
			holder = new ViewHolder();
			v = mInflater.inflate(R.layout.menu_item, null);

			holder.textName = (TextView)v.findViewById(R.id.txt_name);
			holder.textPar = v.findViewById(R.id.txt_par);
			v.setTag(holder);
		} else {
			holder = (ViewHolder)v.getTag();
		}
		holder.textName.setText(dataArray.get(pos));
		holder.textName.setTypeface(Config.mainTypeFace);

		if(parArray!=null && pos < parArray.size()) {
			holder.textPar.setText(parArray.get(pos));
			holder.textPar.setTypeface(Config.ziptyTypeFace);
		}
		else {
			holder.textPar.setText("");
		}
		return v;
	}
	public static class ViewHolder {
		TextView textName;
		TextView textPar;
	}
}