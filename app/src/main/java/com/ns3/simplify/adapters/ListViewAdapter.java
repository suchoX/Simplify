package com.ns3.simplify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.ns3.simplify.Attendance;
import com.ns3.simplify.R;
import com.ns3.simplify.others.ObjectItem;

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
        TextView subinfoText = (TextView) convertView.findViewById(R.id.sub_info);
        TextView classinfoText = (TextView) convertView.findViewById(R.id.class_info);
        TextView sectioninfoText = (TextView) convertView.findViewById(R.id.section_info);

        subinfoText.setText(objectItem.Subject+" - "+objectItem.SubjectCode);
        classinfoText.setText(objectItem.Stream+" "+objectItem.Batch+" ("+objectItem.Semester+" Semester)");
        sectioninfoText.setText("Sec :"+objectItem.Section+"  Gr :"+objectItem.Group);

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