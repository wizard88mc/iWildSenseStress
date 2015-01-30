package ch.qol.unige.iwildsensestress.app;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.Log;

public class OutputFileManager {

	public static final String LOG_STRING = "OUTPUT_FILE_MANAGER";
	public static final String BASE_OUTPUT_FILE = "surveyAnswers.csv";

    private static File outputFile = null;

    /**
     * Method used to instantiate a new File where we write all the responses to the survey
     * and the questionnaire
     */
	public static void instantiateFile(Context context) {
		
		try {
			File root = context.getFilesDir();
			outputFile = new File(root, BASE_OUTPUT_FILE);
			
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			
		}
		catch(IOException exc) {
			Log.e(LOG_STRING, "In instantiateFile: " + exc.toString());
		}
	}
	
	/**
	 * Checks if the outputFile exists, meaning that some answers has been
	 * given to the survey
	 * @return If the file exists or not
	 */
	private static boolean checkIfFileExists(Context context) {

        File root = context.getFilesDir();
        outputFile = new File(root, BASE_OUTPUT_FILE);
		return outputFile.exists();
	}
	
	/**
	 * Deletes the output file once it has been uploaded to the server
	 * @return if the delete succeeded or not
	 */
	public static boolean deleteOutputFile(Context context) {
		return new File(context.getFilesDir(), BASE_OUTPUT_FILE).delete();
	}
	
	/**
	 * Retrieves all the answers provided by the user during the day
	 * @return a list of answers for the current day
	 */
	public static ArrayList<String> getProvidedAnswersForToday(Context context) {
		
		ArrayList<String> answers = new ArrayList<String>();
		Calendar today = new GregorianCalendar();
		
		if (checkIfFileExists(context)) {
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));
				
				String line;
				while ((line = reader.readLine()) != null) {

                    String[] elements = line.split(",");
                    /**
                     * elements[0]: "stress_survey"
                     * elements[1]: year
                     * elements[2]: month
                     * elements[3]: day
                     * elements[4]: HH:MM is the starting time
                     * elements[5]: HH:MM is the end time
                     * elements[6]: stress value
                     */
					Calendar dayAnswer = new GregorianCalendar();
                    dayAnswer.set(Calendar.DAY_OF_MONTH, Integer.valueOf(elements[3].trim()));
                    dayAnswer.set(Calendar.MONTH, Integer.valueOf(elements[2].trim()));
                    dayAnswer.set(Calendar.YEAR, Integer.valueOf(elements[1].trim()));

                    if (today.get(Calendar.YEAR) == dayAnswer.get(Calendar.YEAR) &&
                            today.get(Calendar.MONTH) == dayAnswer.get(Calendar.MONTH) &&
                            today.get(Calendar.DAY_OF_MONTH) == dayAnswer.get(Calendar.DAY_OF_MONTH)) {

                        answers.add(line);
                    }

				}
				
				reader.close();
			}
			catch(IOException exc) {
				Log.e(LOG_STRING, "In getProvidedAnswersToday: " + exc.toString());
			}
		}
		
		return answers;
	}

    /**
     * Writes the line to the output file
     * @param context context of the application
     * @param line the line to write on the file
     */
    public static void writeLineOnOutputFile(Context context, String line) {

        try {

           if (!checkIfFileExists(context)) {
                instantiateFile(context);
            }

            cleanFile(context);

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile, true));
            outputWriter.write(line); outputWriter.newLine(); outputWriter.flush(); outputWriter.close();
        }
        catch(FileNotFoundException exc) {
            Log.d(LOG_STRING, "In writeLineOnOutputFile (FileNotFound): " + exc.toString());
        }
        catch(IOException exc) {
            Log.d(LOG_STRING, "In writeLineOnOutputFile (IOException): " + exc.toString());
        }

    }

    private static void cleanFile(Context context) {

        Calendar today = new GregorianCalendar();

        if (checkIfFileExists(context)) {

            try {
                ArrayList<String> linesToRewriteInTheFile = new ArrayList<String>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));

                String line;
                while ((line = reader.readLine()) != null) {

                    String[] elements = line.split(",");
                    /**
                     * elements[0]: "stress_survey"
                     * elements[1]: year
                     * elements[2]: month
                     * elements[3]: day
                     * elements[4]: HH:MM is the starting time
                     * elements[5]: HH:MM is the end time
                     * elements[6]: stress value
                     */
                    /**
                     * Here I have to eliminate all the lines that should be kept in the file (are
                     * of today)
                     */
                    Calendar dateOfLine = new GregorianCalendar();
                    dateOfLine.set(Calendar.DAY_OF_MONTH, Integer.valueOf(elements[3].trim()));
                    dateOfLine.set(Calendar.MONTH, Integer.valueOf(elements[2].trim()));
                    dateOfLine.set(Calendar.YEAR, Integer.valueOf(elements[1].trim()));
                    if (!(dateOfLine.get(Calendar.MONTH) != today.get(Calendar.MONTH) ||
                            dateOfLine.get(Calendar.YEAR) != today.get(Calendar.YEAR) ||
                            dateOfLine.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                    dateOfLine.get(Calendar.DAY_OF_MONTH) < today.get(Calendar.DAY_OF_MONTH))) {

                        /**
                         * This is a line to keep and not to eliminate
                         */
                        linesToRewriteInTheFile.add(line);
                    }
                }

                reader.close();

                if (!linesToRewriteInTheFile.isEmpty()) {

                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));
                    for (String toWrite: linesToRewriteInTheFile) {
                        writer.write(toWrite); writer.newLine();
                    }

                    writer.flush(); writer.close();
                }
            }
            catch(FileNotFoundException exc) {
                Log.d(LOG_STRING, "FileNotFoundException in cleanFile: " + exc.toString());
            }
            catch(IOException exc) {
                Log.d(LOG_STRING, "IOException in cleanFile: " + exc.toString());
            }
        }

    }
}
