package com.kerkr.edu.app;

import android.os.Bundle;
 
/**
 * Created by nuuneoi on 11/16/2014.
 */
public abstract class StatedFragment extends BaseFragment {
  
    Bundle savedState;
    public static final String KEY_BUNDLE= "internalSavedViewState";
  
    public StatedFragment() {
        super();
    }
  
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }
  
    protected void onFirstTimeLaunched() {
  
    }
  
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }
  
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }
  
    ////////////////////
    // Don't Touch !!
    ////////////////////
  
    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveStatePrivate();
        if (savedState != null) {
            Bundle b = getArguments();
            b.putBundle(KEY_BUNDLE, savedState);
        }
    }
  
    ////////////////////
    // Don't Touch !!
    ////////////////////
  
    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        savedState = b.getBundle(KEY_BUNDLE);
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }
  
    /////////////////////////////////
    // Restore Instance State Here
    /////////////////////////////////
  
    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString(text));
            onRestoreState(savedState);
        }
    }
  
    protected void onRestoreState(Bundle savedInstanceState) {
  
    }
  
    //////////////////////////////
    // Save Instance State Here
    //////////////////////////////
  
    private Bundle saveStatePrivate() {
        Bundle state = new Bundle();
        // For Example
        //state.putString(text, tv1.getText().toString());
        onSaveState(state);
        return state;
    }
  
    protected void onSaveState(Bundle outState) {
  
    }
}