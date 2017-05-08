package im.unicolas.trolltablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by unicolas on 2017/4/18.
 */

public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";

    private boolean isLoaded= false;
    private int index = -1;

    public static TestFragment instance(int index) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        TextView tipTextView = (TextView) view.findViewById(R.id.ft_tv_tip);

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
            tipTextView.setText("左右滑动\n" + index);
            Log.e(TAG, "onCreateView: Fragment\n" + index);
        }
        isLoaded = true;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint: index-" + index + "\nisVisibleToUser-" + isVisibleToUser);

        if (isLoaded) {
            Log.e(TAG, "setUserVisibleHint: fragment加载完成-" + index);
        }
    }
}
