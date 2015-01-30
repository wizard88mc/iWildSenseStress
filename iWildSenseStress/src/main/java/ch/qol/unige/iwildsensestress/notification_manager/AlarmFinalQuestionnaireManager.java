package ch.qol.unige.iwildsensestress.notification_manager;

import java.util.ArrayList;

import ch.qol.unige.iwildsensestress.R;
import ch.qol.unige.iwildsensestress.app.OutputFileManager;
import ch.qol.unige.iwildsensestress.esm.Survey;
import ch.qol.unige.iwildsensestress.questionnaire.Questionnaire;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmFinalQuestionnaireManager extends IntentService {

    public static final String LOG_STRING = "AlarmFinalQuestionnaireManager";
	public final static int NOTIFICATION_ID = 8400;

    public AlarmFinalQuestionnaireManager() {super("AlarmFinalQuestionnaireManager"); }

	@Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_STRING, "onReceive() method");
        /**
         * Retrieves from the OutputFileManager the list of answers for the day that are
         * marked with a stress value higher than 3 ( = 4 or = 5)
         */
        Context context = getApplicationContext();
		ArrayList<String> answersProvidedForToday = 
				OutputFileManager.getProvidedAnswersForToday(context);
		
		/**
		 * Check if any answer has been given to the ESM during the day
		 */
		if (!answersProvidedForToday.isEmpty()) {
				
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_menu_today)
				.setContentTitle(context.getResources().getString(R.string.notification_questionnaire_title))
				.setContentText(context.getResources().getString(R.string.notification_questionnaire_text));
			
            Intent intentForQuestionnaire = new Intent(context, Questionnaire.class);

            // To be sure that when the survey leaves we go back to the HOME screen
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Questionnaire.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intentForQuestionnaire);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Deleting possible other notification
            mNotificationManager.cancel(AlarmFinalQuestionnaireManager.NOTIFICATION_ID);
            mNotificationManager.cancel(NOTIFICATION_ID);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            Log.d(LOG_STRING, "set notification for questionnaire");
		}
	}
}
