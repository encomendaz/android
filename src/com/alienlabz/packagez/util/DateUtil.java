package com.alienlabz.packagez.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

final public class DateUtil {

	private DateUtil() {
	}

	public static String format(final String date) {
		String result = null;

		if (!Strings.isEmpty(date)) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

			try {
				Date formatedDate = dateFormat.parse(date);
				dateFormat = new SimpleDateFormat("EE, dd MMM yyyy", Locale.getDefault());
				result = dateFormat.format(formatedDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static Date parse(final String date) {
		Date result = null;

		if (!Strings.isEmpty(date)) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

			try {
				result = dateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	public static String format(final Date date, final Context context) {
		String result = null;
		final Locale locale = context.getResources().getConfiguration().locale;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM d HH:mm:ss yyyy", locale);
		result = dateFormat.format(date);
		return result;
	}

	public static int calculateDays(Date data1, Date data2) {
		Calendar calendarData1 = Calendar.getInstance();
		calendarData1.setTime(data1);
		Long dateStamp1 = (calendarData1.getTimeInMillis() - (calendarData1.getTimeInMillis() % (1000 * 60 * 60 * 24)))
				/ (1000 * 60 * 60 * 24);
		Calendar calendarData2 = Calendar.getInstance();
		calendarData2.setTime(data2);
		Long dateStamp2 = (calendarData2.getTimeInMillis() - (calendarData2.getTimeInMillis() % (1000 * 60 * 60 * 24)))
				/ (1000 * 60 * 60 * 24);
		Long diff = dateStamp1 - dateStamp2;
		return Math.abs(diff.intValue());
	}
}
