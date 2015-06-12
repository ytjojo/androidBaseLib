package com.kerkr.edu.download.aidl;
interface IDownloadService {

    void addTask(String url);
    void pauseTask(String url);
    void deleteTask(String url);
    void continueTask(String url);
}