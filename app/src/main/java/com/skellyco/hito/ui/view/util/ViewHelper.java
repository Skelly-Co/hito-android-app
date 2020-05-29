package com.skellyco.hito.ui.view.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Helper class for various operations on views and view groups.
 */
public class ViewHelper {

    /**
     * Disables / Enables all od the views inside the given ViewGroup.
     *
     * @param view view group to disable / enable
     * @param enabled specifies if view group should be disabled / enabled
     */
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
