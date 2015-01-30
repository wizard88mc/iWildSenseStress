package ch.qol.unige.iwildsensestress.app;

import ch.qol.unige.iwildsensestress.R;
import ch.qol.unige.iwildsensestress.esm.Survey;
import ch.qol.unige.iwildsensestress.esm.SurveyHelp;
import ch.qol.unige.iwildsensestress.notification_manager.AllAlarmsManager;
import ch.qol.unige.iwildsensestress.questionnaire.Questionnaire;
import ch.qol.unige.iwildsensestress.questionnaire.QuestionnaireHelp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

public class MainActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        Intent intentStartAlarms = new Intent();
        intentStartAlarms.setAction("ch.qol.unige.iwildsensestress.SETUP_ALARMS");
        sendBroadcast(intentStartAlarms);

		finish();
		
		//Intent intent = new Intent(this, Questionnaire.class);
		//startActivity(intent);
	}
}
