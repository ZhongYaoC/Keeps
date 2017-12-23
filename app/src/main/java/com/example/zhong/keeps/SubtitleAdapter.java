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

public class SubtitleAdapter extends ArrayAdapter<SubTitle> {

    private int resourceId;

    public SubtitleAdapter(Context context, int textViewResourceId,
                           List<SubTitle> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        SubTitle subTitle = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,
                parent,false);
        TextView titleName = view.findViewById(R.id.title_name);
        titleName.setText(subTitle.getSubtitle());
        return view;
    }
}
