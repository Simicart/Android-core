package com.simicart.core.base.networkcloud.request.multi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;

public class SimiRequestQueueCloud {

    /**
     * Used for generating monotonically-increasing sequence numbers for
     * requests.
     */
    protected AtomicInteger mSequenceGenerator = new AtomicInteger();

    protected final Map<String, Queue<SimiRequest>> mWaitingRequests = new HashMap<String, Queue<SimiRequest>>();

    /**
     * The set of all requests currently being processed by this RequestQueue. A
     * Request will be in this set if it is waiting in any queue or currently
     * being processed by any dispatcher.
     */
    protected final Set<SimiRequest> mCurrentRequests = new HashSet<SimiRequest>();

    /**
     * The cache triage queue.
     */
    protected final PriorityBlockingQueue<SimiRequest> mCacheQueue = new PriorityBlockingQueue<SimiRequest>();

    /**
     * The queue of requests that are actually going out to the network.
     */
    protected final PriorityBlockingQueue<SimiRequest> mNetworkQueue = new PriorityBlockingQueue<SimiRequest>();

    /**
     * Number of network request dispatcher threads to start.
     */
    protected static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;

    /**
     * Network interface for performing requests.
     */
    protected SimiNetwork mNetwork;

    /**
     * The network dispatchers.
     */
    protected SimiNetworkDispatcher[] mDispatchers;

    protected SimiExecutorDelivery mDelivery;

    protected SimiNetworkCacheL1 mCacheL1;

    public SimiRequestQueueCloud() {
        this(DEFAULT_NETWORK_THREAD_POOL_SIZE);
    }

    public SimiRequestQueueCloud(int size) {
        mDispatchers = new SimiNetworkDispatcher[size];
        mCacheL1 = new SimiNetworkCacheL1();
        SimiUrlStack urlStack = new SimiUrlStack();
        mNetwork = new SimiBasicNetwork(urlStack);
        mDelivery = new SimiExecutorDelivery(
                new Handler(Looper.getMainLooper()));
    }

    public JSONObject getDataFromCacheL1(SimiRequest request) {

        String key = request.getCacheKey();

        if (null != key) {
            return mCacheL1.get(key);
        }

        return null;
    }

    public void removeFromCacheL1(SimiRequest request) {
        String key = request.getCacheKey();
        mCacheL1.remove(key);
    }

    public void clearCacheL1() {
        mCacheL1.clear();
    }

    public PriorityBlockingQueue<SimiRequest> getNetworkQueue() {
        return mNetworkQueue;
    }

    public void start() {
        stop(); // Make sure any currently running dispatchers are stopped.

        // Create network dispatchers (and corresponding threads) up to the pool
        // size.
        for (int i = 0; i < mDispatchers.length; i++) {
            SimiNetworkDispatcher networkDispatcher = new SimiNetworkDispatcher(
                    mNetworkQueue, mNetwork, mDelivery,mCacheL1);
            mDispatchers[i] = networkDispatcher;
            networkDispatcher.start();
        }

    }

    public void stop() {
        for (int i = 0; i < mDispatchers.length; i++) {
            if (mDispatchers[i] != null) {
                mDispatchers[i].quit();
            }
        }
    }

    public int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }

    public void add(SimiRequest request) {
        request.setRequestQueue(this);
        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }

        request.setSequence(getSequenceNumber());
        synchronized (mNetworkQueue) {
            mNetworkQueue.add(request);
        }

    }

    public void finish(SimiRequest request) {
        synchronized (mCurrentRequests) {
            mCurrentRequests.remove(request);
        }
    }

}
