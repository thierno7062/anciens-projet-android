package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.TaskEntry;

public class AddTaskViewModel extends ViewModel {

    private final AppDatabase mDb;
    private final int mTaskId;

    private LiveData<TaskEntry> taskEntry;

    public AddTaskViewModel(AppDatabase mDb, int mTaskId) {
        this.mDb = mDb;
        this.mTaskId = mTaskId;
        taskEntry = mDb.taskDao().loadTaskById(mTaskId);
    }

    public LiveData<TaskEntry> getTaskEntry() {
        return taskEntry;
    }
}
