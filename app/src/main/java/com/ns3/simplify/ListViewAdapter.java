package com.ns3.simplify;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class ListViewAdapter extends ArrayAdapter<ObjectItem>{

    Context mContext;
    int layoutResourceId;
    ObjectItem data[] = null;
    String BatchID;

    public ListViewAdapter(Context mContext, int layoutResourceId, ObjectItem[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        ObjectItem objectItem = data[position];
        TextView textViewItem = (TextView) convertView.findViewById(R.id.text_class);

        ViewGroup.LayoutParams params = textViewItem.getLayoutParams();
        params.height = (int)textViewItem.getTextSize()*4;
        textViewItem.setLayoutParams(params);
        textViewItem.setText(objectItem.batch + "  " + objectItem.subject);

        BatchID = objectItem.batchID;
        convertView.setOnClickListener(new OnItemClickListener(BatchID));

        return convertView;

    }

    private class OnItemClickListener  implements View.OnClickListener {
        private String BatchID;

        OnItemClickListener(String BatchID)
        {
            this.BatchID = BatchID;
        }

        @Override
        public void onClick(View arg0)
        {
            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            ((Attendance)mContext).selected_class(BatchID);
        }
    }

}