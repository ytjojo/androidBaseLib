package com.kerkr.edu.utill;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ShortCutUtils {
	static final String ACTION_INSTALL = "com.android.launcher.action.INSTALL_SHORTCUT";
	static final String ACTION_UNINSTALL = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	public static void createShortcut(Activity context, int appName, int icon,Class launchActivityClazz) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(appName));
		shortcut.putExtra("duplicate", false); // 不允许重复创建
		// 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
//		ComponentName comp = new ComponentName(context.getPackageName(),
//				+ context.getLocalClassName());
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
//				Intent.ACTION_MAIN).setComponent(comp));
		
	    // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(context, launchActivityClazz);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        shortcut
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
		
		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				context, icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		context.sendBroadcast(shortcut);
	}

	public static void delShortcut(Activity context) {
		String dbPath = "/data/data/com.android.launcher/databases/launcher.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		db.delete("favorites", "iconPackage=?", new String[] { context
				.getApplication().getPackageName() });
		db.close();
	}

	public static boolean isExistShortcut(Activity context) {
		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();
		final String AUTHORITY = "com.android.launcher.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI, new String[] { "iconPackage" },
				"iconPackage=?", new String[] { context.getApplication()
						.getPackageName() }, null);
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
			c.close();
		}
		return isInstallShortcut;
	}

	public static void delShortcutFromDesktop(Context paramContext,
			String paramString1, String paramString2, String paramString3) {
		Intent localIntent1 = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		String str = paramString3;
		PackageManager localPackageManager = paramContext.getPackageManager();
		int i = 8320;
		try {
			ApplicationInfo localApplicationInfo = localPackageManager
					.getApplicationInfo(paramString1, i);
			if (str == null)
				str = localPackageManager.getApplicationLabel(
						localApplicationInfo).toString();
			localIntent1.putExtra("android.intent.extra.shortcut.NAME", str);
			ComponentName localComponentName = new ComponentName(paramString1,
					paramString2);
			Intent localIntent2 = new Intent("android.intent.action.MAIN")
					.setComponent(localComponentName);
			localIntent1.putExtra("android.intent.extra.shortcut.INTENT",
					localIntent2);
			paramContext.sendBroadcast(localIntent1);
			return;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			while (true)
				localNameNotFoundException.printStackTrace();
		}
	}
}