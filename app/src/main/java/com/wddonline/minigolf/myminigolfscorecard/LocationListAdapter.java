package com.wddonline.minigolf.myminigolfscorecard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LocationListAdapter extends BaseAdapter
{
	Context activityContext;
	public ArrayList<LocationData> dataArray;
	public LocationListAdapter(Context context, ArrayList<LocationData> list){
		activityContext = context;
		dataArray = list;
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
	public void resetList(List<LocationData> list){
		dataArray.clear();
		dataArray = new ArrayList<>(list);
		notifyDataSetChanged();
	}
	public void refreshArray(ArrayList<LocationData> list){
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
			v = mInflater.inflate(R.layout.location_list_item, null);

			holder.textName = (TextView)v.findViewById(R.id.txt_name);
			holder.textAddress = (TextView)v.findViewById(R.id.txt_address);
			v.setTag(holder);
		} else {
			holder = (ViewHolder)v.getTag();
		}
		holder.textName.setText(dataArray.get(pos).NAME);
		holder.textAddress.setText(
				dataArray.get(pos).CITY +
						", " + dataArray.get(pos).STATE +
						String.format(" (%.2f)",Float.parseFloat(dataArray.get(pos).DISTANCE)));
		holder.textName.setTypeface(Config.mainTypeFace);
		holder.textAddress.setTypeface(Config.mainTypeFace);

		return v;
	}
	public static class ViewHolder {
		TextView textName;
		TextView textAddress;
	}
}