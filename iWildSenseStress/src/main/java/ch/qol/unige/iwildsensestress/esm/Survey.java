package ch.qol.unige.iwildsensestress.esm;

import ch.qol.unige.iwildsensestress.R;
import ch.qol.unige.iwildsensestress.app.AnswersQuestionsManager;
import ch.qol.unige.iwildsensestress.questionnaire.QuestionnaireHelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Survey extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.survey_layout);
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
                startActivity(new Intent(this, SurveyHelp.class));
                return true;
            }
        }
        return true;
    }
	
	/**
	 * Called when the user clicked on the "Discard" button. It closes the 
	 * dialog
	 * @param view the button clicked
	 */
	public void buttonDiscardClicked(View view) {
		
		this.finish();
	}
	
	/**
	 * Called when the user clicked on the "Done" button. It stores the values 
	 * of energy, valence and stress and dismiss the notification
	 * @param view
	 */
	public void buttonDoneClicked(View view) {
		
		int radioEnergyID = ((RadioGroup) findViewById(R.id.groupEnergy))
				.getCheckedRadioButtonId();
		int radioValenceID = ((RadioGroup) findViewById(R.id.groupValence))
				.getCheckedRadioButtonId();
		int radioStressID = ((RadioGroup) findViewById(R.id.groupStress))
				.getCheckedRadioButtonId();
		
		if (radioEnergyID != -1 && radioValenceID != -1 && radioStressID != -1) {
			
			String valueEnergy = (String) ((RadioButton) findViewById(radioEnergyID))
					.getText();
			String valueValence = (String) ((RadioButton) findViewById(radioValenceID))
					.getText();
			String valueStress = (String) ((RadioButton) findViewById(radioStressID))
					.getText();
			
			String resultSurvey = valueEnergy + "," + valueValence + "," + valueStress;
			 
			/**
			 * Sends broadcast with the answers provided and finish the activity
			 */
			Intent intent = new Intent();
			intent.setAction(AnswersQuestionsManager.SURVEY_ANSWER_PROVIDED_IDENTIFIER);
			intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			intent.putExtra(AnswersQuestionsManager.ANSWER_IDENTIFIER, resultSurvey);
			sendBroadcast(intent);
			
			this.finish();
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.missing_answers_survey, 
					Toast.LENGTH_LONG).show();
		}
	}
}
