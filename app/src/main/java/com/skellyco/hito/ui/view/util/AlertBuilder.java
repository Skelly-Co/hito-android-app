package com.skellyco.hito.ui.view.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.skellyco.hito.R;

/**
 * Class for creating alerts for the whole application.
 * We created separated class for it for better cohesion and less code repetition as well as to be able
 * to easily switch the dialog theme by modifying only this class.
 */
public class AlertBuilder {

    /**
     * Creates an error alert dialog.
     *
     * @param ctx context
     * @param errorMessage error message to display
     * @return configured alert dialog
     */
    public static AlertDialog buildErrorDialog(Context ctx, String errorMessage)
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
