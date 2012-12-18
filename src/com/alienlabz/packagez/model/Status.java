package com.alienlabz.packagez.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.alienlabz.packagez.R;

public enum Status {
	ACCEPTANCE("acceptance"), CHECKED("checked"), ENROUTE("enroute"), UNKNOWN("unknown"), AWAITING("awaiting"), DELIVERING(
			"delivering"), DELIVERED("delivered"), NONE("");

	private String status;

	private Status(final String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}

	public String getTranslatedStatus(final Context context) {
		String result = "";

		if (Status.ENROUTE.status.equals(status)) {
			result = context.getResources().getString(R.string.status_enroute);
		} else if (Status.AWAITING.status.equals(status)) {
			result = context.getResources().getString(R.string.status_awaiting);
		} else if (Status.DELIVERED.status.equals(status)) {
			result = context.getResources().getString(R.string.status_delivered);
		} else if (Status.DELIVERING.status.equals(status)) {
			result = context.getResources().getString(R.string.status_delivering);
		} else if (Status.CHECKED.status.equals(status)) {
			result = context.getResources().getString(R.string.status_checked);
		} else if (Status.ACCEPTANCE.status.equals(status)) {
			result = context.getResources().getString(R.string.status_acceptance);
		} else if (Status.UNKNOWN.status.equals(status)) {
			result = context.getResources().getString(R.string.status_unknown);
		}

		return result;
	}

	public static Status fromString(final String status) {
		Status result = Status.NONE;

		if (Status.ENROUTE.status.equals(status)) {
			result = Status.ENROUTE;
		} else if (Status.AWAITING.status.equals(status)) {
			result = Status.AWAITING;
		} else if (Status.DELIVERED.status.equals(status)) {
			result = Status.DELIVERED;
		} else if (Status.DELIVERING.status.equals(status)) {
			result = Status.DELIVERING;
		} else if (Status.CHECKED.status.equals(status)) {
			result = Status.CHECKED;
		} else if (Status.ACCEPTANCE.status.equals(status)) {
			result = Status.ACCEPTANCE;
		} else if (Status.UNKNOWN.status.equals(status)) {
			result = Status.UNKNOWN;
		}

		return result;
	}

	public static List<Status> all() {
		final List<Status> result = new ArrayList<Status>();

		result.add(Status.ACCEPTANCE);
		result.add(Status.ENROUTE);
		result.add(Status.DELIVERED);
		result.add(Status.DELIVERING);
		result.add(Status.UNKNOWN);
		result.add(Status.CHECKED);
		result.add(Status.AWAITING);

		return result;
	}

	public int getSmallDrawable(final Context context) {
		int result = R.drawable.status_unknown;

		if (Status.ENROUTE.status.equals(status)) {
			result = R.drawable.status_enroute;
		} else if (Status.AWAITING.status.equals(status)) {
			result = R.drawable.status_awaiting;
		} else if (Status.DELIVERED.status.equals(status)) {
			result = R.drawable.status_delivered;
		} else if (Status.DELIVERING.status.equals(status)) {
			result = R.drawable.status_delivering;
		} else if (Status.CHECKED.status.equals(status)) {
			result = R.drawable.status_checked;
		} else if (Status.ACCEPTANCE.status.equals(status)) {
			result = R.drawable.status_acceptance;
		} else if (Status.UNKNOWN.status.equals(status)) {
			result = R.drawable.status_unknown;
		}

		return result;
	}

	public int getBigDrawable(final Context context) {
		int result = R.drawable.status_unknown;

		if (Status.ENROUTE.status.equals(status)) {
			result = R.drawable.b_status_enroute;
		} else if (Status.AWAITING.status.equals(status)) {
			result = R.drawable.b_status_awaiting;
		} else if (Status.DELIVERED.status.equals(status)) {
			result = R.drawable.b_status_delivered;
		} else if (Status.DELIVERING.status.equals(status)) {
			result = R.drawable.b_status_delivering;
		} else if (Status.CHECKED.status.equals(status)) {
			result = R.drawable.b_status_checked;
		} else if (Status.ACCEPTANCE.status.equals(status)) {
			result = R.drawable.b_status_acceptance;
		} else if (Status.UNKNOWN.status.equals(status)) {
			result = R.drawable.b_status_unknown;
		}

		return result;
	}

}