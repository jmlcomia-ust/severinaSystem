package com.example.testois.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.testois.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomExListAdapterOwner extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitle;
    private Map<String, List<String>> listItem;

    public CustomExListAdapterOwner(Context context, List<String> listTitle, Map<String, List<String>> listItem) {
        this.context = context;
        this.listTitle = listTitle;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return listTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItem.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return listTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(listTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title =(String)getGroup(groupPosition);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group,null);
        }
        TextView txtTitle = (TextView)convertView.findViewById(R.id.listTitle);
        txtTitle.setTypeface(null, Typeface.BOLD);
        txtTitle.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title =(String)getChild(groupPosition, childPosition);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        }
        TextView txtChild = (TextView)convertView.findViewById(R.id.expandableListItem);
        txtChild.setText(title);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
