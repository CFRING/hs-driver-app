/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hongshi.wuliudidi.thread;

import java.util.concurrent.Callable;

//import com.luxtone.lib.utils.Log;

// NOTE: If the Callable throws any Throwable, the result value will be null.
public class FutureTask<T> implements Runnable, Future<T> {
    private static final String TAG = "FutureTask";
    private Callable<T> mCallable;
    private FutureListener<T> mListener;
    private volatile boolean mIsCancelled;
    private boolean mIsDone;
    private T mResult;

    public FutureTask(Callable<T> callable, FutureListener<T> listener) {
        mCallable = callable;
        mListener = listener;
    }

    public FutureTask(Callable<T> callable) {
        this(callable, null);
    }

    @Override
    public void cancel() {
        mIsCancelled = true;
    }

    @Override
    public synchronized T get() {
        while (!mIsDone) {
            try {
                wait();
            } catch (InterruptedException t) {
                // ignore.
            }
        }
        return mResult;
    }

    @Override
    public void waitDone() {
        get();
    }

    @Override
    public synchronized boolean isDone() {
        return mIsDone;
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }

    @Override
    public void run() {
        T result = null;

        if (!mIsCancelled) {
            try {
                result = mCallable.call();
            } catch (Throwable ex) {
            }
        }

        synchronized(this) {
            mResult = result;
            mIsDone = true;
            if (mListener != null) {
                mListener.onFutureDone(this);
            }
            notifyAll();
        }
    }
}
