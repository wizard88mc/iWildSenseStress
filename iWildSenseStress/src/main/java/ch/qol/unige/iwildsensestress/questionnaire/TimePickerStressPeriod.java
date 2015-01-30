package ch.qol.unige.iwildsensestress.questionnaire;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

/**
 * Created by Matteo on 26/01/2015.
 */
public class TimePickerStressPeriod extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int hourOfDay = 0;
    private int minuteOfDay = 0;
    private String editTextTag = null;

    public interface OnEndEditTimeDialogFragmentListener {
        void onEndEditTime(String timeTag, int hourOfDay, int minuteOfHour);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String time = getArguments().getString("time");
        String[] timeParts = time.split(":");
        hourOfDay = Integer.valueOf(timeParts[0]);
        minuteOfDay = Integer.valueOf(timeParts[1]);

        editTextTag = getArguments().getString("editTextTag");

        return new TimePickerDialog(getActivity(), this, hourOfDay, minuteOfDay, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((OnEndEditTimeDialogFragmentListener) getActivity())
                .onEndEditTime(editTextTag, hourOfDay, minute);
    }
}
