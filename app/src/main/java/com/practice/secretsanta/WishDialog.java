package com.practice.secretsanta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;

public class WishDialog extends AppCompatDialogFragment {
    EditText editTextEnterWish;
    boolean secretSantasSelected;
    String wish;
    WishDialogListener wishDialogListener;

    public interface WishDialogListener {
        void saveWish(String str);
    }

    public WishDialog(String str, boolean z) {
        this.wish = str;
        this.secretSantasSelected = z;
    }

    public void setCancelable(boolean z) {
        super.setCancelable(z);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(C0859R.string.title_dialog_wish);
        View inflate = getActivity().getLayoutInflater().inflate(C0859R.layout.dialog_wish, (ViewGroup) null);
        builder.setView(inflate);
        EditText editText = (EditText) inflate.findViewById(C0859R.C0862id.editTextEnterWish);
        this.editTextEnterWish = editText;
        String str = this.wish;
        if (str != null) {
            editText.setText(str);
        }
        if (this.secretSantasSelected) {
            this.editTextEnterWish.setEnabled(false);
        } else {
            builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    WishDialog wishDialog = WishDialog.this;
                    wishDialog.wish = wishDialog.editTextEnterWish.getText().toString().trim();
                    WishDialog.this.wishDialogListener.saveWish(WishDialog.this.wish);
                    dialogInterface.dismiss();
                }
            });
        }
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.wishDialogListener = (WishDialogListener) context;
        } catch (ClassCastException unused) {
            throw new ClassCastException(context.toString() + " must implement WishDialogListener.");
        }
    }
}
