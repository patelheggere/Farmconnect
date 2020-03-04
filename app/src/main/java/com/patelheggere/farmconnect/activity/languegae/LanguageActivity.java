package com.patelheggere.farmconnect.activity.languegae;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.welcome.WelcomeActivity;
import com.patelheggere.farmconnect.base.BaseActivity;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LANGUAGE;


public class LanguageActivity extends BaseActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButtonKannada, radioButtonEnglish;
    private Button btnNext;
    private ActionBar mActionBar;
    private LinearLayout mLinearLayoutNext;
    private ListView mListViewLanguage;
    private CustomLanguageListAdapter mCustomLanguageListAdapter;
    private List<String> mLanguageList;
    private String mSelectedLanguage;
    private boolean isRecreated = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_language;
    }

    @Override
    protected void initView() {
      //  radioGroup = findViewById(R.id.rg_language);
       // radioButtonEnglish = findViewById(R.id.rb_english);
       // radioButtonKannada = findViewById(R.id.rb_kannada);
      //  btnNext = findViewById(R.id.btn_next);
        mListViewLanguage = findViewById(R.id.listViewLanguage);
        mLinearLayoutNext = findViewById(R.id.linearLayoutNext);
    }

    @Override
    protected void initData() {
        mActionBar = getSupportActionBar();
        if(mActionBar!=null)
        {
            mActionBar.setTitle(getString(R.string.language));
        }
    }

    @Override
    protected void initListener() {
        mLanguageList = new ArrayList<>();
        mLanguageList.add(getString(R.string.kannada));
        mLanguageList.add(getString(R.string.engish));
    /*    btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
                startActivity(new Intent(LanguageActivity.this, WelcomeActivity.class));
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rb_kannada) {
                    Log.d(TAG, "onCheckedChanged: kannada");
                    SharedPrefsHelper.getInstance().save(LANGUAGE, "ka");
                }
                else if(i==R.id.rb_english)
                {
                    Log.d(TAG, "onCheckedChanged: english");
                    SharedPrefsHelper.getInstance().save(LANGUAGE, "en");
                }

            }

        });*/

    mLinearLayoutNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            recreate();
            startActivity(new Intent(LanguageActivity.this, WelcomeActivity.class));
            finish();
        }
    });


        mCustomLanguageListAdapter = new CustomLanguageListAdapter(mLanguageList, LanguageActivity.this);
        mListViewLanguage.setAdapter(mCustomLanguageListAdapter);
        mListViewLanguage.requestFocusFromTouch();
        mListViewLanguage.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       // mListViewLanguage.setSelection(0);
        mListViewLanguage.setItemChecked(0, true);
        mListViewLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //selectItem(position);
                setItemNormal();
                mSelectedLanguage = mLanguageList.get(i);
              if(mSelectedLanguage.equalsIgnoreCase(getString(R.string.kannada)))
              {
                  SharedPrefsHelper.getInstance().save(LANGUAGE, "ka");
              }
              else if(mSelectedLanguage.equalsIgnoreCase(getString(R.string.engish))){
                  SharedPrefsHelper.getInstance().save(LANGUAGE, "en");
              }
                View rowView = view;
                setItemSelected(rowView);
            }
        });

        setItemNormal();
        View rowView = getViewByPosition(0, mListViewLanguage);
        setItemSelected(rowView);
       /* if(!isRecreated)
        {
            recreate();
            isRecreated = true;
        }*/

        //mListViewLanguage.setSelection(0);

    }


    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    public void setItemSelected(View view) {
        try {
            View rowView = view;
            TextView tv = rowView.findViewById(R.id.textViewLanguage);
            tv.setTextColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setItemNormal() {
        for (int i = 0; i < mListViewLanguage.getChildCount(); i++) {
            View v = mListViewLanguage.getChildAt(i);
            TextView txtview = v.findViewById(R.id.textViewLanguage);
            txtview.setTextColor(Color.BLACK);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
       // isRecreated = false;
    }
}
