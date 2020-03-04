package com.patelheggere.farmconnect.activity.languegae;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.patelheggere.farmconnect.R;

import java.util.List;

public class CustomLanguageListAdapter extends ArrayAdapter<String> {

    private List<String> dataSet;
    private Context mContext;
    private int selectedItem;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }

    public CustomLanguageListAdapter(List<String> data, Context context) {
        super(context, R.layout.language_list_item, data);
        this.dataSet = data;
        this.mContext=context;
    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
       // LanguageListResponse.Language  data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.language_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textViewLanguage);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


       // Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(getItem(position));

        // Return the completed view to render on screen
        return convertView;
    }


}