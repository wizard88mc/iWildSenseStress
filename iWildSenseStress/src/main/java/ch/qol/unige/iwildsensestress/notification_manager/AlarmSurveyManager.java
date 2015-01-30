package ch.qol.unige.iwildsensestress.notification_manager;

import ch.qol.unige.iwildsensestress.R;
import ch.qol.unige.iwildsensestress.esm.Survey;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

public class AlarmSurveyManager extends IntentService {

    private static final String LOG_STRING ="AlarmSurveyManager";
	private static final int MAX_HOUR = 20;
    private static final int MINUTES_MAX_HOUR = 30;
	public static final int NOTIFICATION_ID = 8488;
	
	public AlarmSurveyManager() {
		super("StressAlarmsSurveyQuestionnaireManager");
	}
	
	/**
	 * When onReceive is called, this class sends a notification to the 
	 * user asking to answer to the survey
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

        Log.d(LOG_STRING, "onHandleIntent() method");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_menu_paste_holo_dark)
            .setContentTitle(getResources().getString(R.string.notification_title))
            .setContentText(getResources().getString(R.string.notification_text));

        // Setting vibration
        mBuilder.setVibrate(new long[]{100, 250, 500, 250});

        // Setting notification color
        mBuilder.setLights(Color.GREEN, 1000, 5000);

        Intent intentForSurvey = new Intent(this, Survey.class);

        // To be sure that when the survey leaves we go back to the HOME screen
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Survey.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intentForSurvey);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(NOTIFICATION_ID);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        Calendar now = Calendar.getInstance();
        Calendar maxTime = Calendar.getInstance();
        maxTime.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
        maxTime.set(Calendar.MINUTE, MINUTES_MAX_HOUR);

		if (now.after(maxTime)) {
            // Probabilmente devo usare broadcast x far andare questa roba
            Log.d(LOG_STRING, "disabling Alarm for Survey");
			AllAlarmsManager.stopSchedulingAlarms(this);
		}
	}

}
