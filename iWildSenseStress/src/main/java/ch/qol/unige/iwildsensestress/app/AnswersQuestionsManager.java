package ch.qol.unige.iwildsensestress.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.qol.unige.iwildsensestress.notification_manager.AlarmFinalQuestionnaireManager;
import ch.qol.unige.iwildsensestress.notification_manager.AlarmSurveyManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AnswersQuestionsManager extends BroadcastReceiver {

    private static final String LOG_STRING = "AnswerQuestionsManager";
	
	public static final String SURVEY_ANSWER_PROVIDED_IDENTIFIER = "esm.survey.answers.DONE";
	public static final String SURVEY_ANSWER_NOT_PROVIDED_IDENTIFIER = "esm.survey.answers.DISCARD";
	public static final String QUESTIONNAIRE_ANSWER_PROVIDED_IDENTIFIER = "esm.questionnaire.answer.DONE";
	public static final String QUESTIONNAIRE_ANSWER_NOT_PROVIDED_IDENTIFIER = "esm.questionnaire.answer.DISCARD";
	public static final String ANSWER_IDENTIFIER = "answer";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Method called when a Broadcast is sent, it takes the one sent from the
	 * surveys and the questionnaire to save the data
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(SURVEY_ANSWER_PROVIDED_IDENTIFIER)) {
			
			// First I have to remove the notification from the Notification Bar
			NotificationManager mNotificationManager = 
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(AlarmSurveyManager.NOTIFICATION_ID);
			
			String answers = intent.getExtras().getString(ANSWER_IDENTIFIER);
			// answers is energy,value,stress values provided by the user
			Calendar calendar = new GregorianCalendar();
			
			int surveyHour = calendar.get(Calendar.HOUR_OF_DAY), 
					surveyMinute = calendar.get(Calendar.MINUTE);

            /**
             * stress_survey at the beginning is used to identify as a row with
             * the answer of the survey
             */
			String finalString = "(stress_survey," + dateFormat.format(calendar.getTime()) + ","
					 + System.currentTimeMillis() + "," + 
					(surveyHour - 2) + ":" + surveyMinute + "-" + 
					 surveyHour + ":" + surveyMinute + "," + answers + ")";
			
			Log.d(LOG_STRING, finalString);
            /**
             * TODO Code from mattia to save the data
             */

            /**
             * If the stress is rated more than 3, we save the time range inside a file to scan
             * at the end of the day to build the questionnaire
             */
            String stressEvaluation = (answers.split(","))[2];
            if (Integer.valueOf(stressEvaluation) > 3) {

                Calendar now = new GregorianCalendar();
                String stringToSaveForQuestionnaire = "(stress_survey," + now.get(Calendar.YEAR) +
                        "," + now.get(Calendar.MONTH) + "," + now.get(Calendar.DAY_OF_MONTH) + "," +
                        (surveyHour - 2) + ":" + surveyMinute + "," + surveyHour + ":" + surveyMinute
                        + "," + answers + ")";

                OutputFileManager.writeLineOnOutputFile(context, stringToSaveForQuestionnaire);
            }
		}
		else if (intent.getAction().equals(SURVEY_ANSWER_NOT_PROVIDED_IDENTIFIER)) {
			
		}
		else if (intent.getAction().equals(QUESTIONNAIRE_ANSWER_PROVIDED_IDENTIFIER)) {
			
			NotificationManager mNotificationManager = 
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(AlarmFinalQuestionnaireManager.NOTIFICATION_ID);

            String answersProvided = intent.getStringExtra(ANSWER_IDENTIFIER);

            Calendar calendar = new GregorianCalendar();

            String finalString = "(stress_questionnaire," + dateFormat.format(calendar.getTime()) + ","
                    + System.currentTimeMillis() + answersProvided + ")";

            Log.d(LOG_STRING, finalString);
            //OutputFileManager.writeLineOnOutputFile(context, finalString);
            /**
             * TODO Code from Mattia to save the output string
             */
            OutputFileManager.deleteOutputFile(context);
		}
		else if (intent.getAction().equals(QUESTIONNAIRE_ANSWER_NOT_PROVIDED_IDENTIFIER)) {
			
		}
	}

}
