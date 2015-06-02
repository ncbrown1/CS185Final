package edu.ucsb.cs.cs185.cs185final;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Activity;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.app.DatePickerDialog;
import java.util.*;
import android.view.*;


public class NoticeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    /* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
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
        // Build the dialog and set up the button click handlers
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.YEAR);
        int minute = c.get(Calendar.MONTH);
        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        // Do something with the date chosen by the user
        TextView x = (TextView)getActivity().findViewById(R.id.textView);
        if(minute < 10){
            x.setText("Game will end at " + hour + ":" + 0 + minute + ".");
        }
        else {
            x.setText("Game will end at " + hour + ":" + minute + ".");
        }
    }

}

