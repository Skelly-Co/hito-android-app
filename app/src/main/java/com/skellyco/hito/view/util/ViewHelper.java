package com.skellyco.hito.view.util;

import android.view.View;
import android.view.ViewGroup;

public class ViewHelper {

    public static void setViewGroupEnabled(ViewGroup view, boolean enabled)
    {
        int children = view.getChildCount();
        for (int i = 0; i< children ; i++)
        {
            View child = view.getChildAt(i);
            if (child instanceof ViewGroup)
            {
                setViewGroupEnabled((ViewGroup) child, enabled);
            }
            child.setEnabled(enabled);
        }
        view.setEnabled(enabled);
    }

}
