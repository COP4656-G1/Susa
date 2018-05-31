package com.susa.ajayioluwatobi.susa;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class QueryFragment extends Fragment {
    public static final String TAG = QueryFragment.class.getCanonicalName();


    public interface OnButtonClickListener {
        public void onButtonClick(Bundle bundle);
    }

    private OnButtonClickListener mClickListener;

    private EditText tLoc;
    private EditText tPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View rootView = inflater.inflate(R.layout.fragment_query, container, false);

        Button bSearch = rootView.findViewById(R.id.button_search);
        tLoc = rootView.findViewById(R.id.edit_location);
        tPrice = rootView.findViewById(R.id.edit_price);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToSecondFragment();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            mClickListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    public void sendDataToSecondFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("location", tLoc.getText().toString());
        bundle.putString("price", tPrice.getText().toString());


        mClickListener.onButtonClick(bundle);
    }
}
