package com.susa.ajayioluwatobi.susa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPasswordDialog extends DialogFragment implements TextView.OnEditorActionListener{
    private static String email;
    private EditText memail;


    public static ForgotPasswordDialog newInstance(int title) {
        ForgotPasswordDialog frag = new ForgotPasswordDialog();
        Bundle args = new Bundle();
        args.putString("email", email);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            NoticeDialogListener listener = (NoticeDialogListener) getActivity();
            listener.onDialogPositiveClick(ForgotPasswordDialog.this,memail.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }

        return false;
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String email);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = (inflater.inflate(R.layout.dialog_forgot_password, null));

    final EditText memail= (EditText) view.findViewById(R.id.forgot_email);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Reset Password")
        // Get the layout inflater






        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

                // Add action buttons
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        email= memail.getText().toString();
                        mListener.onDialogPositiveClick(ForgotPasswordDialog.this, email);
                        // sign in the user ...

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ForgotPasswordDialog.this);
                    }
                });
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View dialogView= inflater.inflate(R.layout.dialog_forgot_password, container, false);
        memail= (EditText)dialogView.findViewById(R.id.forgot_email);

        return dialogView;
    }
}
