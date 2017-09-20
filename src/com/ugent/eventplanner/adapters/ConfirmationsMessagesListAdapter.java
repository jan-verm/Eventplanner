package com.ugent.eventplanner.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ugent.eventplanner.R;
import com.ugent.eventplanner.models.ConfirmationMessageContainer;

public class ConfirmationsMessagesListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader;
    private Map<String, List<ConfirmationMessageContainer>> _listDataChild;
 
    public ConfirmationsMessagesListAdapter(Context context, List<String> listDataHeader,
            Map<String, List<ConfirmationMessageContainer>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
 
    @Override
    public ConfirmationMessageContainer getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitleText = getChild(groupPosition, childPosition).getTitle();
        String childSubtitleText = getChild(groupPosition, childPosition).getSubtitle();
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView childTitleView = (TextView) convertView
                .findViewById(R.id.lblListTitle);
        TextView childSubtitleView = (TextView) convertView
                .findViewById(R.id.lblListSubTitle);
 
        childTitleView.setText(childTitleText);
        childSubtitleView.setText(childSubtitleText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public String getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
