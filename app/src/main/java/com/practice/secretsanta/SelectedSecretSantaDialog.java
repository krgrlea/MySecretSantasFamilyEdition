package com.practice.secretsanta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SelectedSecretSantaDialog extends AppCompatDialogFragment {
    String nameOfUserToBeSecretSantaFor;
    TextView textViewSelectedSecretSanta;
    String wishOfUserToBeSecretSantaFor;

    public SelectedSecretSantaDialog(String nameOfUserToBeSecretSantaFor, String wishOfUserToBeSecretSantaFor) {
        this.nameOfUserToBeSecretSantaFor = nameOfUserToBeSecretSantaFor;
        this.wishOfUserToBeSecretSantaFor = wishOfUserToBeSecretSantaFor;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_dialog_selected_secret_santa);
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_selected_secret_santa, (ViewGroup) null);
        builder.setView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.textViewSelectedSecretSanta);
        this.textViewSelectedSecretSanta = textView;
        textView.setText("Du bist der Secret Santa für " + this.nameOfUserToBeSecretSantaFor + " und dein Wichtel wünscht sich:\n" + this.wishOfUserToBeSecretSantaFor + "!");
        builder.setCancelable(true);
        return builder.create();
    }
}
