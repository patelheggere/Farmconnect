package com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.model.FilterCheckBoxModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCheckboxCropAdapter extends ArrayAdapter<FilterCheckBoxModel> {
    private Context mContext;
    private ArrayList<FilterCheckBoxModel> listState;
    private SpinnerCheckboxCropAdapter myAdapter;
    private boolean isFromView = false;
    private CheckedValueCrop checkedValue;

    public SpinnerCheckboxCropAdapter(Context context, int resource, List<FilterCheckBoxModel> objects, CheckedValueCrop checkedValue) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<FilterCheckBoxModel>) objects;
        this.myAdapter = this;
       this.checkedValue = checkedValue;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.filter_spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                    if(checkedValue!=null)
                    {
                        checkedValue.getCropPosition(listState, position);
                    }
                }
            }
        });
        return convertView;
    }

    public interface CheckedValueCrop{
        public void getCropPosition(List<FilterCheckBoxModel> liststate, int pos);
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}