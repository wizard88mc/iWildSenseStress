package ch.qol.unige.iwildsensestress.notification_manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AllAlarmsManager extends BroadcastReceiver {

    private static final String LOG_STRING = "AllAlarmsManager";

	private static AlarmManager mAlarmManager = null;
	private static PendingIntent pendingIntentSurvey = null;
	private static PendingIntent pendingIntentQuestionnaire = null;

    //private static final int HOUR_OF_DAY_BEGIN_SURVEY = 8;
    private static final int HOUR_OF_DAY_BEGIN_SURVEY = 0;
    //private static final int MINUTE_OF_HOUR_BEGIN_SURVEY = 30;
    private static final int MINUTE_OF_HOUR_BEGIN_SURVEY = 22;
    //private static final int HOUR_OF_DAY_QUESTIONNAIRE = 21;
    private static final int HOUR_OF_DAY_QUESTIONNAIRE = 0;

    //private static final int DEFAULT_ALARMS_DISTANCE = 1000 * 60 * 60 * 2; // 2 hours
    private static final int DEFAULT_ALARMS_DISTANCE = 1000 * 60 * 10; // 10 minutes

    /**
     * Called when a broadcast is received (Can be the one of device turned on or the first
     * time to setup the alarms)
     * @param context
     * @param intent
     */
	@Override
	public void onReceive(Context context, Intent intent) {

        Log.d(LOG_STRING, "onReceive() method");

		 if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			 scheduleAlarms(context, false);
             //scheduleAlarms(context, true);
		 }
        else if (intent.getAction().equals("ch.qol.unige.iwildsensestress.SETUP_ALARMS")) {
             scheduleAlarms(context, true);
         }
	}
	
	public static void scheduleAlarms(Context context, boolean rescheduling) {

		if (mAlarmManager == null) {
			mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		}
		
		Intent intentSurvey = new Intent(context, AlarmSurveyManager.class);
        Intent intentQuestionnaire = new Intent(context, AlarmFinalQuestionnaireManager.class);

        pendingIntentSurvey =
                PendingIntent.getService(context, 0, intentSurvey, 0);
        pendingIntentQuestionnaire =
                PendingIntent.getService(context, 0, intentQuestionnaire, 0);

        /**
         * If we are rescheduling Alarm manager, we set the default behavior of starting
         * at 8.30 a.m.
         */
        if (rescheduling) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY_BEGIN_SURVEY);
            calendar.set(Calendar.MINUTE, MINUTE_OF_HOUR_BEGIN_SURVEY);

            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    DEFAULT_ALARMS_DISTANCE, pendingIntentSurvey);
        }
        /**
         * If not rescheduling, means that the device has been booted and no Alarm manager
         * is set up. In this case Alarm will start one hour after, and rescheduled every 2
         * hours as normal behavior
         */
        else {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 1000 * 60 * 60 * 2,
                    DEFAULT_ALARMS_DISTANCE, pendingIntentSurvey);
        }

        Calendar timeWakeUpQuestionnaire = Calendar.getInstance();
        timeWakeUpQuestionnaire.setTimeInMillis(System.currentTimeMillis());
        timeWakeUpQuestionnaire.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY_QUESTIONNAIRE);
        timeWakeUpQuestionnaire.set(Calendar.MINUTE, 42);

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeWakeUpQuestionnaire.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntentQuestionnaire);

        Toast.makeText(context, "All alarms ready", Toast.LENGTH_LONG).show();
        Log.d(LOG_STRING, "All alarms set");
	}

    /**
     * Method called because we don't need any more to schedule alarms for the day.
     * It stops the service that manages the alarms and reschedule it for the next day
     * @param context
     */
	public static void stopSchedulingAlarms(Context context) {
		
		mAlarmManager.cancel(pendingIntentSurvey);
		scheduleAlarms(context, true);
	}

}
