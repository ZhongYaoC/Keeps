package com.example.zhong.keeps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhong on 17-12-25.
 */

public class SettingsAdapter extends ArrayAdapter<Settings> {

    private int resourceId;

    public SettingsAdapter(Context context, int textViewResourceId,
                           List<Settings> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Settings settings = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,
                false);
        TextView SettingName = view.findViewById(R.id.settings_name);
        SettingName.setText(settings.getName());
        return view;
    }
}
