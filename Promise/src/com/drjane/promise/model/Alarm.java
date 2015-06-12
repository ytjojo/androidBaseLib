/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.drjane.promise.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.R.integer;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
@DatabaseTable(tableName ="tb_alarm")
public class Alarm implements Serializable {
    
    public enum Difficulty {
        EASY, MEDIUM, HARD;
        
        @Override
        public String toString() {
            switch (this.ordinal()) {
                case 0:
                    return "Easy";
                case 1:
                    return "Medium";
                case 2:
                    return "Hard";
            }
            return super.toString();
        }
    }
    
    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
        
        @Override
        public String toString() {
            switch (this.ordinal()) {
                case 0:
                    return "Sunday";
                case 1:
                    return "Monday";
                case 2:
                    return "Tuesday";
                case 3:
                    return "Wednesday";
                case 4:
                    return "Thursday";
                case 5:
                    return "Friay";
                case 6:
                    return "Saturday";
            }
            return super.toString();
        }
        
    }
    
    private static final long serialVersionUID = 8699489847426803789L;
    @DatabaseField(generatedId = true)  
    private int id;
    
    @DatabaseField
    private Boolean alarmActive = true;
    
    private Calendar alarmTime = Calendar.getInstance();
    
    @DatabaseField(unknownEnumName ="")
    private Day[] days = { Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY, Day.SUNDAY };
    @DatabaseField
    private String alarmTonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    @DatabaseField
    private Boolean vibrate = true;
    @DatabaseField
    private String alarmName = "Alarm Clock";
    
    @DatabaseField
    private Difficulty difficulty = Difficulty.EASY;
    @DatabaseField
    public int repeatCount;
    
    public Alarm() {
        
    }
    
    //	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    //		out.defaultWriteObject();
    //		out.writeObject(getAlarmToneUri().getEncodedPath());
    //	}
    
    //	private void readObject(java.io.ObjectInputStream in) throws IOException {
    //		try {
    //			in.defaultReadObject();
    //			this.setAlarmToneUri(Uri.parse(in.readObject().toString()));
    //		} catch (ClassNotFoundException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}		
    //	}
    
    /**
     * @return the alarmActive
     */
    public Boolean getAlarmActive() {
        return alarmActive;
    }
    
    /**
     * @param alarmActive
     *            the alarmActive to set
     */
    public void setAlarmActive(Boolean alarmActive) {
        this.alarmActive = alarmActive;
    }
    
    /**
     * @return the alarmTime
     */
    public Calendar getAlarmTime() {
        if (alarmTime.before(Calendar.getInstance()))
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        while (!Arrays.asList(getDays()).contains(Day.values()[alarmTime.get(Calendar.DAY_OF_WEEK) - 1])) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        return alarmTime;
    }
    
    /**
     * @param alarmTime
     *            the alarmTime to set
     */
    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }
    
   
    /**
     * @return the repeatDays
     */
    public Day[] getDays() {
        return days;
    }
    
    /**
     * @param set
     *            the repeatDays to set
     */
    public void setDays(Day[] days) {
        this.days = days;
    }
    
    public void addDay(Day day) {
        boolean contains = false;
        for (Day d : getDays())
            if (d.equals(day))
                contains = true;
        if (!contains) {
            List<Day> result = new LinkedList<Day>();
            for (Day d : getDays())
                result.add(d);
            result.add(day);
            setDays(result.toArray(new Day[result.size()]));
        }
    }
    
    public void removeDay(Day day) {
        
        List<Day> result = new LinkedList<Day>();
        for (Day d : getDays())
            if (!d.equals(day))
                result.add(d);
        setDays(result.toArray(new Day[result.size()]));
    }
    
    /**
     * @return the alarmTonePath
     */
    public String getAlarmTonePath() {
        return alarmTonePath;
    }
    
    /**
     * @param alarmTonePath the alarmTonePath to set
     */
    public void setAlarmTonePath(String alarmTonePath) {
        this.alarmTonePath = alarmTonePath;
    }
    
    /**
     * @return the vibrate
     */
    public Boolean getVibrate() {
        return vibrate;
    }
    
    /**
     * @param vibrate
     *            the vibrate to set
     */
    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }
    
    /**
     * @return the alarmName
     */
    public String getAlarmName() {
        return alarmName;
    }
    
    /**
     * @param alarmName
     *            the alarmName to set
     */
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
   

}
