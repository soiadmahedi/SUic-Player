package com.soiadmahedi.suicTh;

import static android.content.Context.UI_MODE_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.media.AudioManager;
import android.os.Build;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.util.Rational;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.File;

import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import java.util.List;
import android.view.Window;
import android.os.LocaleList;

public final class SoiadMahediUtils {
	
	public static boolean fileExists(final Context context, final Uri uri) {
		final String scheme = uri.getScheme();
		if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			try {
				final InputStream inputStream = context.getContentResolver().openInputStream(uri);
				inputStream.close();
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			String path;
			if (ContentResolver.SCHEME_FILE.equals(scheme)) {
				path = uri.getPath();
			} else {
				path = uri.toString();
			}
			final File file = new File(path);
			return file.exists();
		}
	}
	
	public static final String FEATURE_FIRE_TV = "amazon.hardware.fire_tv";
	
	public static final String[] supportedExtensionsVideo = new String[] { "3gp", "m4v", "mkv", "mov", "mp4", "ts", "webm" };
	public static final String[] supportedExtensionsSubtitle = new String[] { "srt", "ssa", "ass", "vtt", "ttml", "dfxp", "xml" };
	
	public static final String[] supportedMimeTypesVideo = new String[] {
		MimeTypes.VIDEO_MATROSKA,
		MimeTypes.VIDEO_MP4,
		MimeTypes.VIDEO_WEBM,
		"video/quicktime",
		"video/mp2ts",
		MimeTypes.VIDEO_H263,
		"video/x-m4v",
	};
	public static final String[] supportedMimeTypesSubtitle = new String[] {
		MimeTypes.APPLICATION_SUBRIP,
		MimeTypes.TEXT_SSA,
		MimeTypes.TEXT_VTT,
		MimeTypes.APPLICATION_TTML,
		"text/*",
		"application/octet-stream"
	};
	
	public static boolean hasNavigationBar(Context context) {
		boolean z = true;
		if (VERSION.SDK_INT >= 17) {
			Display defaultDisplay = getWindowManager(context).getDefaultDisplay();
			Point point = new Point();
			Point point2 = new Point();
			defaultDisplay.getSize(point);
			defaultDisplay.getRealSize(point2);
			if (point2.x == point.x) {
				if (point2.y == point.y) {
					z = false;
				}
			}
			return z;
		}
		boolean hasPermanentMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
		boolean deviceHasKey = KeyCharacterMap.deviceHasKey(4);
		if (hasPermanentMenuKey || deviceHasKey) {
			z = false;
		}
		return z;
	}
	
	public static boolean isVolumeMin(final AudioManager audioManager) {
		int min = Build.VERSION.SDK_INT >= 28 ? audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC) : 0;
		return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == min;
	}
	
	private static int getVolume(final Context context, final boolean max, final AudioManager audioManager) {
		if (Build.VERSION.SDK_INT >= 30 && Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
			try {
				Method method;
				Object result;
				Class<?> clazz = Class.forName("com.samsung.android.media.SemSoundAssistantManager");
				Constructor<?> constructor = clazz.getConstructor(Context.class);
				final Method getMediaVolumeInterval = clazz.getDeclaredMethod("getMediaVolumeInterval");
				result = getMediaVolumeInterval.invoke(constructor.newInstance(context));
				if (result instanceof Integer) {
					int mediaVolumeInterval = (int) result;
					if (mediaVolumeInterval < 10) {
						method = AudioManager.class.getDeclaredMethod("semGetFineVolume", int.class);
						result = method.invoke(audioManager, AudioManager.STREAM_MUSIC);
						if (result instanceof Integer) {
							if (max) {
								return 150 / mediaVolumeInterval;
							} else {
								int fineVolume = (int) result;
								return fineVolume / mediaVolumeInterval;
							}
						}
					}
				}
			} catch (Exception e) {}
		}
		if (max) {
			return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		} else {
			return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		}
	}
	
	public static boolean isSupportedNetworkUri(final Uri uri) {
		if (uri == null)
		return false;
		final String scheme = uri.getScheme();
		if (scheme == null)
		return false;
		return scheme.startsWith("http") || scheme.equals("rtsp");
	}
	
	public static String[] getDeviceLanguages() {
		final List<String> locales = new ArrayList<>();
		if (Build.VERSION.SDK_INT >= 24) {
			final LocaleList localeList = Resources.getSystem().getConfiguration().getLocales();
			for (int i = 0; i < localeList.size(); i++) {
				locales.add(localeList.get(i).getISO3Language());
			}
		} else {
			final Locale locale = Resources.getSystem().getConfiguration().locale;
			locales.add(locale.getISO3Language());
		}
		return locales.toArray(new String[0]);
	}
	
	public static void toggleSystemUi(final Activity activity, final View viewSection, final boolean show) {
		if (Build.VERSION.SDK_INT >= 31) {
			Window window = activity.getWindow();
			if (window != null) {
				WindowInsetsController windowInsetsController = window.getInsetsController();
				if (windowInsetsController != null) {
					if (show) {
						windowInsetsController.show(WindowInsets.Type.systemBars());
					} else {
						windowInsetsController.hide(WindowInsets.Type.systemBars());
					}
				}
			}
		} else {
			if (show) {
				viewSection.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			} else {
				viewSection.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			}
		}
	}
	
	public static boolean isEdge(Context context, MotionEvent motionEvent) {
		int dp2px = dp2px(context, 40.0f);
		float f = (float) dp2px;
		if (motionEvent.getRawX() < f || motionEvent.getRawX() > ((float) (getScreenWidth(context, true) - dp2px)) || motionEvent.getRawY() < f) {
			return true;
		}
		return motionEvent.getRawY() > ((float) (getScreenHeight(context, true) - dp2px));
	}
	
	public static int getNavigationBarHeight(Context context) {
		if (!hasNavigationBar(context)) {
			return 0;
		}
		Resources resources = context.getResources();
		return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
	}
	
	public static Activity scanForActivity(Context context) {
		if (context == null) {
			return null;
		}
		if (context instanceof Activity) {
			return (Activity) context;
		}
		return context instanceof ContextWrapper ? scanForActivity(((ContextWrapper) context).getBaseContext()) : null;
	}
	
	/**
	Soiad Mahedi 
	From Bangladesh 
	
	Soiad Mahedi's Profiles 
	Facebook : https://www.facebook.com/soiadmahediofficial
	Instagram : https://www.instagram.com/soiadmahedi
	Twitter : https://www.twitter.com/soiadmahedi
	LinkedIn : https://www.linkedin.com/in/soiadmahedi  
	
	Line : 4446
	
	*/
	
	@SuppressLint({"PrivateApi"})
	@Deprecated
	public static Application getApplication() {
		try {
			return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFileName(Context context, Uri uri) {
		String result = null;
		try {
			if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
				try (Cursor cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
					if (cursor != null && cursor.moveToFirst()) {
						final int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
						if (columnIndex > -1)
						result = cursor.getString(columnIndex);
					}
				}
			}
			if (result == null) {
				result = uri.getPath();
				int cut = result.lastIndexOf('/');
				if (cut != -1) {
					result = result.substring(cut + 1);
				}
			}
			if (result.indexOf(".") > 0)
			result = result.substring(0, result.lastIndexOf("."));
		} catch (Exception e) {
			result =  uri.getLastPathSegment();
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getCurrentSystemTime() {
		return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
	}
	
	public static int getScreenHeight(Context context, boolean z) {
		return z ? context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight(context) : context.getResources().getDisplayMetrics().heightPixels;
	}
	
	public static int getScreenWidth(Context context, boolean z) {
		return z ? context.getResources().getDisplayMetrics().widthPixels + getNavigationBarHeight(context) : context.getResources().getDisplayMetrics().widthPixels;
	}
	
	public static int getStatusBarHeight(Context context) {
		Resources resources = context.getResources();
		return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
	}
	
	public static int getStatusBarHeightPortrait(Context context) {
		Resources resources = context.getResources();
		return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height_portrait", "dimen", "android"));
	}
	
	public static void setViewMargins(final View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
		layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		view.setLayoutParams(layoutParams);
	}
	
	public static void setViewParams(final View view, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		setViewMargins(view, marginLeft, marginTop, marginRight, marginBottom);
	}
	
	private SoiadMahediUtils() {
	}
	
	public static int dp2px(Context context, float f) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, context.getResources().getDisplayMetrics());
	}
	
	public static int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}
	
	public static WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	public static int sp2px(Context context, float f) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, f, context.getResources().getDisplayMetrics());
	}
	
	public static float normalizeFontScale(float fontScale, boolean small) {
		// https://bbc.github.io/subtitle-guidelines/#Presentation-font-size
		// Soiad Mahedi - SUic Player 
		float newScale;
		if (fontScale > 1.01f) {
			if (fontScale >= 1.99f) {
				// 2.0
				newScale = (small ? 1.15f : 1.2f);
			} else {
				// 1.5
				newScale = (small ? 1.0f : 1.1f);
			}
		} else if (fontScale < 0.99f) {
			if (fontScale <= 0.26f) {
				// 0.25
				newScale = (small ? 0.65f : 0.8f);
			} else {
				// 0.5
				newScale = (small ? 0.75f : 0.9f);
			}
		} else {
			newScale = (small ? 0.85f : 1.0f);
		}
		return newScale;
	}
	
	public static boolean isRotated(final Format format) {
		return format.rotationDegrees == 90 || format.rotationDegrees == 270;
	}
	
	public static boolean isPortrait(final Format format) {
		if (isRotated(format)) {
			return format.width > format.height;
		} else {
			if (format.width == format.height) {
				return true;
			} else {
				return format.height > format.width;
			}
		} 
	}
	
	public static Rational getRational(final Format format) {
		if (isRotated(format))
		return new Rational(format.height, format.width);
		else
		return new Rational(format.width, format.height);
	}
	
	public static String convertMicrosecondsToDuration(long microseconds) {
		StringBuilder durationString = new StringBuilder();
		
		// Handle negative values
		if (microseconds < 0) {
			microseconds = -microseconds;
			durationString.append("-");
		}
		
		// Separate calculations for hours, minutes, seconds, milliseconds
		int hours = (int) (microseconds / (1000 * 60 * 60));
		int minutes = (int) ((microseconds % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) ((microseconds % (1000 * 60)) / 1000);
		int milliseconds = (int) (microseconds % 1000);
		
		// Include hours only if greater than zero
		if (hours > 0) {
			durationString.append(hours).append(":");
		}
		
		// Include minutes with leading zero if less than 10
		durationString.append(String.format("%02d", minutes)).append(":");
		
		// Include seconds with leading zero if less than 10
		durationString.append(String.format("%02d", seconds));
		
		// Include milliseconds if greater than zero
		if (milliseconds > 0) {
			durationString.append(",").append(milliseconds);
		}
		
		return durationString.toString().trim();
	}
	
	public static String formatBitrate(int bitrate) {
		// Define suffixes for different bitrate units
		String[] suffixes = {"bps", "Kbps", "Mbps", "Gbps"};
		
		// Convert bitrate to base-10 units (bits per second)
		double bitrate_bps = bitrate;
		
		// Find the appropriate suffix based on the magnitude
		int i = 0;
		while (bitrate_bps >= 1024 && i < suffixes.length - 1) {
			bitrate_bps /= 1024;
			i++;
		}
		
		// Format the bitrate with one decimal place and the chosen suffix
		return String.format("%.1f %s", bitrate_bps, suffixes[i]);
	}
	
	public static boolean isSubtitleType(String mimeType) {
		if (mimeType != null) {
			for (String mime : SoiadMahediUtils.supportedMimeTypesSubtitle) {
				if (mimeType.equals(mime)) {
					return true;
				}
			}
			if (mimeType.equals("text/plain") || mimeType.equals("text/3gpp-tt")  || mimeType.equals("text/x-ssa") || mimeType.equals("application/octet-stream") ||
			mimeType.equals("application/ass") || mimeType.equals("application/ssa") || mimeType.equals("application/vtt")) {
				return true;
			}
		}
		return false;
	}
	
	public static String getLanguageFullName(String languageCode) {
		Locale locale = new Locale(languageCode);
		return locale.getDisplayLanguage(Locale.ENGLISH);
	}
	
}
