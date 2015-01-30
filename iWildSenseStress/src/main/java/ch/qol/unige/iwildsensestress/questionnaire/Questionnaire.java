package ch.qol.unige.iwildsensestress.questionnaire;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ch.qol.unige.iwildsensestress.R;
import ch.qol.unige.iwildsensestress.app.AnswersQuestionsManager;
import ch.qol.unige.iwildsensestress.app.OutputFileManager;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class Questionnaire extends Activity
        implements TimePickerStressPeriod.OnEndEditTimeDialogFragmentListener {
	
	private static final String LOG_STRING = "LOG_QUESTIONNAIRE";
    private ArrayList<TextView> listStartTime = new ArrayList<TextView>();
    private ArrayList<TextView> listEndTime = new ArrayList<TextView>();
    private ArrayList<TextView> listStressValues = new ArrayList<TextView>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.questionnaire_layout);
		
		ArrayList<String> listAnswers = OutputFileManager.getProvidedAnswersForToday(this);
		
		/**
		 * With all the stress moment of the day, I can build the layout adding
		 * all the timepicker + stress evaluation
		 */
		LinearLayout listOfStressMomentsContainer = (LinearLayout) findViewById(R.id.listStress);
        int counterForTag = 1;
        if (!listAnswers.isEmpty()) {
            for (String stress : listAnswers) {

                Log.d(LOG_STRING, stress);
                final String[] elements = stress.split(",");
                /**
                 * elements[0]: "stress_survey"
                 * elements[1]: year
                 * elements[2]: month
                 * elements[3]: day
                 * elements[4]: HH:MM is the starting time
                 * elements[5]: HH:MM is the end time
                 * elements[6]: stress value
                 */

                LinearLayout externalHorizontalLayout = new LinearLayout(this);
                externalHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                externalHorizontalLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                LayoutParams paramsExternalHorizontalLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                paramsExternalHorizontalLayout.setMargins(0,
                        (int) getResources().getDimension(R.dimen.margin_between_widgets), 0,
                        (int) getResources().getDimension(R.dimen.margin_between_widgets));

                LinearLayout layoutFromContainer = new LinearLayout(this);
                layoutFromContainer.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout layoutToContainer = new LinearLayout(this);
                layoutToContainer.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout layoutStressContainer = new LinearLayout(this);
                layoutStressContainer.setOrientation(LinearLayout.HORIZONTAL);

                LayoutParams paramsFirstLayoutContainer = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                LayoutParams paramsLayoutContainers = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                paramsLayoutContainers.setMargins((int) getResources().getDimension(R.dimen.medium_margin),
                        0, 0, 0);

                float mediumFontSize = getResources().getDimension(R.dimen.l_font_size) /
                        getResources().getDisplayMetrics().density;
                float largeFontSize = getResources().getDimension(R.dimen.xl_font_size) /
                        getResources().getDisplayMetrics().density;

                TextView textViewFrom = new TextView(this),
                        textViewTo = new TextView(this),
                        textViewStress = new TextView(this);
                textViewFrom.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mediumFontSize);
                textViewTo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mediumFontSize);
                textViewStress.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mediumFontSize);

                textViewFrom.setText(R.string.from);
                textViewTo.setText(R.string.to);
                textViewStress.setText(R.string.stress_value);

                LayoutParams paramsTextViewLabel =
                        new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                paramsTextViewLabel.setMargins(0, 0,
                        (int) getResources().getDimension(R.dimen.medium_margin), 0);

                final TextView startTime = new TextView(this), endTime = new TextView(this),
                        stressValue = new TextView(this);
                startTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, largeFontSize);
                endTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, largeFontSize);
                stressValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, largeFontSize);

                startTime.setText(elements[4]);
                endTime.setText(elements[5]);
                stressValue.setText(elements[6]);
                if (elements[6].equals("4")) {
                    stressValue.setTextColor(Color.rgb(255, 165, 0));
                } else if (elements[6].equals("5")) {
                    stressValue.setTextColor(Color.RED);
                }
                startTime.setPaintFlags(startTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                endTime.setPaintFlags(endTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                startTime.setTag("startTime" + counterForTag);
                endTime.setTag("endTime" + counterForTag);

                listStartTime.add(startTime);
                listEndTime.add(endTime);
                listStressValues.add(stressValue);

                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment timePickerDialog = new TimePickerStressPeriod();
                        Bundle parameters = new Bundle();
                        parameters.putString("time", elements[4]);
                        parameters.putString("editTextTag", startTime.getTag().toString());
                        timePickerDialog.setArguments(parameters);

                        timePickerDialog.show(getFragmentManager(), "TimePicker");
                    }
                });

                endTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment timePickerDialog = new TimePickerStressPeriod();
                        Bundle parameters = new Bundle();
                        parameters.putString("time", elements[5]);
                        parameters.putString("editTextTag", endTime.getTag().toString());
                        timePickerDialog.setArguments(parameters);

                        timePickerDialog.show(getFragmentManager(), "TimePicker");
                    }
                });

                LayoutParams paramsTextViewValue =
                        new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                layoutFromContainer.addView(textViewFrom, paramsTextViewLabel);
                layoutFromContainer.addView(startTime, paramsTextViewValue);
                layoutToContainer.addView(textViewTo, paramsTextViewLabel);
                layoutToContainer.addView(endTime, paramsTextViewValue);
                layoutStressContainer.addView(textViewStress, paramsTextViewLabel);
                layoutStressContainer.addView(stressValue, paramsTextViewValue);

                externalHorizontalLayout.addView(layoutFromContainer, paramsFirstLayoutContainer);
                externalHorizontalLayout.addView(layoutToContainer, paramsLayoutContainers);
                externalHorizontalLayout.addView(layoutStressContainer, paramsLayoutContainers);

                listOfStressMomentsContainer.addView(externalHorizontalLayout,
                        paramsExternalHorizontalLayout);

                /**
                 * Creating an horizontal bar to divide stress moments on the dialog
                 */
                View horizontalBar = new View(this);
                horizontalBar.setBackgroundColor(Color.rgb(0, 153, 204));

                DisplayMetrics metrics = this.getResources().getDisplayMetrics();

                LayoutParams paramsHorizontalBar = new LayoutParams(0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics));
                paramsHorizontalBar.weight = 9;
                paramsHorizontalBar.gravity = Gravity.CENTER;

                LinearLayout layoutContainerHorizontalBar = new LinearLayout(this);
                layoutContainerHorizontalBar.setWeightSum(10);
                layoutContainerHorizontalBar.setGravity(Gravity.CENTER);

                LayoutParams layoutParamsContainerHorizontalBar = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                layoutParamsContainerHorizontalBar.gravity = Gravity.CENTER;

                layoutContainerHorizontalBar.addView(horizontalBar, paramsHorizontalBar);

                listOfStressMomentsContainer.addView(layoutContainerHorizontalBar,
                        layoutParamsContainerHorizontalBar);

                counterForTag++;
            }
        }
        else {
            this.finish();
        }
	}

    /**
     * Method called once the TimePickerDialog has been closed and the new value has been
     * set
     * @param timeTag the tag of the TextView that needs to be updates
     * @param hourOfDay the new value of the hour
     * @param minuteOfHour the new value of the minutes of the hour
     */
    @Override
    public void onEndEditTime(String timeTag, int hourOfDay, int minuteOfHour) {
        /**
         * Received the new time value from the last Time Picker. We have to update the value of
         * the textEdit
         */
        LinearLayout layout = (LinearLayout) findViewById(R.id.listStress);
        TextView textViewToChange = (TextView) layout.findViewWithTag(timeTag);

        String hourString = String.valueOf(hourOfDay),
                minutesString = String.valueOf(minuteOfHour);

        if (hourString.length() == 1) {
            hourString = "0" + hourString;
        }

        if (minutesString.length() == 1) {
            minutesString = "0" + minutesString;
        }

        textViewToChange.setText(hourString + ":" + minutesString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.help: {
                startActivity(new Intent(this, QuestionnaireHelp.class));
                return true;
            }
        }
        return true;
    }


    /**
     * The user dismiss the questionnaire, simply closes the Activity
     * @param view the button clicked
     */
    public void onBtnCancelClicked(View view) {
        this.finish();
    }

    /**
     * User clicked on the button to confirm the answers provided to the questionnaire.
     * Data is collected, putted together and sent to the service that is responsible to save
     * it on the file
     * @param view the button clicked
     */
    public void onBtnConfirmClicked(View view) {

        String finalStringToSave = "";
        for (int i = 0; i < listStartTime.size(); i++) {
            String singleValue = "[" + listStartTime.get(i).getText() + "," +
                    listEndTime.get(i).getText() + "," + listStressValues.get(i).getText() + "]";

            finalStringToSave += "," + singleValue;
        }

        /**
         * Sending our broadcast with data provided by the user
         */

        Log.d("FINAL_STRING_ANSWER", finalStringToSave);

        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(AnswersQuestionsManager.QUESTIONNAIRE_ANSWER_PROVIDED_IDENTIFIER);
        //intentBroadcast.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intentBroadcast.putExtra("answer", finalStringToSave);
        sendBroadcast(intentBroadcast);

        this.finish();
        return;
    }
}
