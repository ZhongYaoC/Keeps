package com.example.zhong.keeps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhong on 17-12-22.
 */

public class ChildKPsAdapter extends ArrayAdapter<KnowledgePoint> {

    private int resourceId;

    public ChildKPsAdapter(Context context, int textViewResourceId,
                           List<KnowledgePoint> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        KnowledgePoint kp = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,
                parent,false);
        TextView titleName = view.findViewById(R.id.name);
        titleName.setText(kp.getName());
        return view;
    }

    /*@Override
    public int getCount() {
        return  5;
    }*/
}
