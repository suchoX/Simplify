package com.ns3.simplify;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Sucho on 04-Oct-15.
 */
public class OnItemClickListenerListViewItem implements OnItemClickListener
{
    Context context;
    char batch[],subject[];
    public OnItemClickListenerListViewItem(Context context)
    {
        this.context=context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        int i,length,j;
        batch=new char[100];
        subject=new char [100];
        TextView textViewItem = ((TextView) view.findViewById(R.id.text_class));
        String listItemText = textViewItem.getText().toString();

        length=listItemText.length();
        i=0;
        while(listItemText.charAt(i)!=' ')
            batch[i]=listItemText.charAt(i++);
        i+=2;
        j=0;
        while(i<length)
            subject[j++]=listItemText.charAt(i++);

        String s1=new String(batch);
        String s2=new String(subject);
        s1=s1.trim();
        s2=s2.trim();
        ((Attendance) context).selected_class(s1, s2);

    }

}
