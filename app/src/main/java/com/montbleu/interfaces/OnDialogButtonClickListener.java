package com.montbleu.interfaces;

import android.app.AlertDialog;

public interface OnDialogButtonClickListener {
    void onPositiveButtonClicked(AlertDialog alertDialog);
    void onNegativeButtonClicked();
}
