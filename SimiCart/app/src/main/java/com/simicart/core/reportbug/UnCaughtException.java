package com.simicart.core.reportbug;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;

import com.simicart.core.config.DataLocal;
import com.simicart.core.splashscreen.SplashActivity;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class UnCaughtException implements UncaughtExceptionHandler {
	private Context mContext;
	private static Context context;

	public UnCaughtException(Context ctx) {
		mContext = ctx;
		context = ctx;
	}

	private StatFs getStatFs() {
		File path = Environment.getDataDirectory();
		return new StatFs(path.getPath());
	}

	private long getAvailableInternalMemorySize(StatFs stat) {
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	private long getTotalInternalMemorySize(StatFs stat) {
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	// private void addInformation(StringBuilder message) {
	// message.append("Locale: ").append(Locale.getDefault()).append('\n');
	// try {
	// PackageManager pm = mContext.getPackageManager();
	// PackageInfo pi;
	// pi = pm.getPackageInfo(mContext.getPackageName(), 0);
	// message.append("Version: ").append(pi.versionName).append('\n');
	// message.append("Package: ").append(pi.packageName).append('\n');
	// } catch (Exception e) {
	// Log.e("CustomExceptionHandler", "Error", e);
	// message.append("Could not get Version information for ").append(
	// mContext.getPackageName());
	// }
	// message.append("Phone Model ").append(android.os.Build.MODEL)
	// .append('\n');
	// message.append("Android Version : ")
	// .append(android.os.Build.VERSION.RELEASE).append('\n');
	// message.append("Board: ").append(android.os.Build.BOARD).append('\n');
	// message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
	// message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
	// message.append("Host: ").append(android.os.Build.HOST).append('\n');
	// message.append("ID: ").append(android.os.Build.ID).append('\n');
	// message.append("Model: ").append(android.os.Build.MODEL).append('\n');
	// message.append("Product: ").append(android.os.Build.PRODUCT)
	// .append('\n');
	// message.append("Type: ").append(android.os.Build.TYPE).append('\n');
	// StatFs stat = getStatFs();
	// message.append("Total Internal memory: ")
	// .append(getTotalInternalMemorySize(stat)).append('\n');
	// message.append("Available Internal memory: ")
	// .append(getAvailableInternalMemorySize(stat)).append('\n');
	// }

	public void uncaughtException(Thread t, Throwable e) {
		try {
			StringBuilder report = new StringBuilder();
			Date curDate = new Date();
			report.append("Error Report collected on : ")
					.append(curDate.toString()).append('\n').append('\n');
			// report.append("Informations :").append('\n');
			// addInformation(report);
			report.append('\n').append('\n');
			report.append("Stack:\n");
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			report.append(result.toString());
			printWriter.close();
			report.append('\n');
			report.append("**** End of current Report ***");
			Log.e(UnCaughtException.class.getName(),
					"Error while sendErrorMail" + report);
			// check resume
			int count = DataLocal.getRestartCount();
			if (count < 3) {
				count++;
				DataLocal.saveRestartCount(count);
				restartApp();
			} else {
				DataLocal.saveRestartCount(0);
				// android.os.Process.killProcess(android.os.Process.myPid());
				// ((Activity) context).finish();
			}
			sendErrorMail(report);
		} catch (Throwable ignore) {
			Log.e(UnCaughtException.class.getName(),
					"Error while sending error e-mail", ignore);
		}
	}

	public void sendErrorMail(final StringBuilder errorContent) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				builder.setTitle("Sorry...!");
				builder.create();
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								restartApp();
							}
						});
				builder.setPositiveButton("Report",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent sendIntent = new Intent(
										Intent.ACTION_SEND);
								String subject = "Your App crashed! Fix it!";
								StringBuilder body = new StringBuilder(
										"SimiCart");
								body.append('\n').append('\n');
								body.append(errorContent).append('\n')
										.append('\n');

								String title = "Log error (version 3.2)."
										+ "\n";
								String packageName = title
										+ "PackageName: "
										+ context.getApplicationContext()
												.getPackageName() + "\n";

								// sendIntent.setType("text/plain");
								sendIntent.setType("message/rfc822");
								sendIntent
										.putExtra(
												Intent.EXTRA_EMAIL,
												new String[] { "duclong.bui@trueplus.vn" });
								sendIntent.putExtra(Intent.EXTRA_TEXT,
										packageName + body.toString());
								sendIntent.putExtra(Intent.EXTRA_SUBJECT,
										subject);
								sendIntent.setType("message/rfc822");

								context.startActivity(sendIntent);
								System.exit(0);
							}
						});
				builder.setMessage("Your application has crashed");
				builder.show();
				Looper.loop();
			}
		}.start();
	}

	private void restartApp() {
		Intent mStartActivity = new Intent(context, SplashActivity.class);
		int mPendingIntentId = 123456;
		PendingIntent mPendingIntent = PendingIntent.getActivity(context,
				mPendingIntentId, mStartActivity,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager mgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,
				mPendingIntent);
		System.exit(0);
	}
}
