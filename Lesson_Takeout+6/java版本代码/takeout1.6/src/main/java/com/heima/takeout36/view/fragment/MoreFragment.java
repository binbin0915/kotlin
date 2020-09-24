package com.heima.takeout36.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heima.takeout36.R;

/**
 * Created by lidongzhi on 2017/6/5.
 */

public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_, null);
        ((TextView) rootView.findViewById(R.id.tv)).setText("更多");
        return rootView;
    }
}
