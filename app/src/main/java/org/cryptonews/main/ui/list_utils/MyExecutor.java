package org.cryptonews.main.ui.list_utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

public class MyExecutor implements Executor {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}
