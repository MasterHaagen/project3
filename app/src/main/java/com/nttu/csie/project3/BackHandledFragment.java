package com.nttu.csie.project3;


import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by MasterHa on 2016/11/19.
 */

public abstract class BackHandledFragment extends Fragment {
    protected BackHandlerInterface backHandlerInterface;
    public abstract String getTagText();
    public abstract boolean onBackPressedFlag();

    public interface BackHandlerInterface
    {
        public void setSelectedFragment(BackHandledFragment backHandledFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(!(getActivity()  instanceof BackHandlerInterface))
        {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        }
        else
        {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Mark this fragment as the selected Fragment.
        backHandlerInterface.setSelectedFragment(this);
    }
}
