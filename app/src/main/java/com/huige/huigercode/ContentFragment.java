package com.huige.huigercode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huige.library.utils.Utils;

/**
 * Created by lzh on 2017/8/31.
 */

public class ContentFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_content, container, false);
//        rootView.findViewById(R.id.contentLayout).setBackgroundColor(Utils.getRandomColor());
        rootView.findViewById(R.id.contentLayout).setBackgroundColor(Utils.getRandomColorUnAlpha());
        return rootView;
    }


}
