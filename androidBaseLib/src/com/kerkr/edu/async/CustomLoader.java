package com.kerkr.edu.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

public abstract class CustomLoader<T> extends AsyncTaskLoader<List<T>> {
	public CustomLoader(Context context) {
		super(context);
		mContext = context;
	
		registerContentObserver();
	}

	SimpleObserver mObserver;
	public List<T> mRingStoneList;
	public Context mContext;
	public static final int DURATION_LIMIT = 5000;

	abstract Cursor getCursor();

	abstract T addItemToList(Cursor cursor);

	@Override
	public List<T> loadInBackground() {
		Cursor cursor = getCursor();
		ArrayList<T> list = new ArrayList<T>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				list.add(addItemToList(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	@Override
	public void deliverResult(List<T> data) {
		// TODO Auto-generated method stub
		super.deliverResult(data);
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		List<T> oldData = data;
		mRingStoneList = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldData != null) {
			onReleaseResources(oldData);
		}
	}

	@Override
	protected void onStopLoading() {
		// TODO Auto-generated method stub
		// At this point we can release the resources associated with 'apps'
		// if needed.
		cancelLoad();
	}

	@Override
	protected void onStartLoading() {
		if (mRingStoneList != null) {
			deliverResult(mRingStoneList);
		}

		// Start watching for changes in the app data.
		// if (mPackageObserver == null) {
		// mPackageObserver = new PackageIntentReceiver(this);
		// }

		// Has something interesting in the configuration changed since we
		// last built the app list?
		// boolean configChange =
		// mLastConfig.applyNewConfig(getContext().getResources());

		if (takeContentChanged() || mRingStoneList == null) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	@Override
	public void onCanceled(List<T> data) {
		// At this point we can release the resources associated with 'apps'
		// if needed.
		super.onCanceled(data);
		onReleaseResources(data);
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
		onStopLoading();
		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (mRingStoneList != null) {
			onReleaseResources(mRingStoneList);
			mRingStoneList = null;
		}

		// Stop monitoring for changes.
		if(mObserver != null){
			unRegisterContentObserver();
			mObserver  = null;
		}
	}

	@Override
	protected List<T> onLoadInBackground() {
		// TODO Auto-generated method stub
		return super.onLoadInBackground();
	}

	protected void onReleaseResources(List<T> apps) {
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}

	public Uri getContentProviderUri() {
		return null;
	}

	public void registerContentObserver() {
		if (getContentProviderUri() != null) {
			mObserver = new SimpleObserver(new Handler(Looper.getMainLooper()));
			mObserver.setOncontentChanged(new OnContentChangedListner() {
				
				@Override
				public void onChanged() {
					onContentChanged();
				}
			});
			getContext().getContentResolver().registerContentObserver(
					getContentProviderUri(), false, mObserver);
		}
	}
	public void unRegisterContentObserver() {
		if (getContentProviderUri() != null && mObserver != null) {

			getContext().getContentResolver().unregisterContentObserver(mObserver);
		}
	}

	public static class SimpleObserver extends ContentObserver {
		OnContentChangedListner onContentChanged;
		private Handler handler;

		public SimpleObserver(Handler h) {
			super(h);
			this.handler = h;

		}

		public void setOncontentChanged(OnContentChangedListner l) {
			this.onContentChanged = l;
		}

		@Override
		public void onChange(boolean selfChange) {

			Message message = handler.obtainMessage();
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					onContentChanged.onChanged();
				}
			});
		}

	}

	public interface OnContentChangedListner {
		void onChanged();
	}
}
