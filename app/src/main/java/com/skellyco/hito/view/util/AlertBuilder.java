package com.skellyco.hito.view.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.skellyco.hito.R;

public class AlertBuilder {

    public static AlertDialog buildInformationDialog(Context ctx, String errorMessage)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ctx, R.style.DarkThemeDialog).create();
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }

}
