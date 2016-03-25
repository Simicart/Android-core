package com.simicart.core.base.networkcloud.request.multi;

import java.util.concurrent.BlockingQueue;

import android.os.Process;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.networkcloud.response.CoreResponse;
import com.simicart.core.base.networkcloud.request.error.SimiError;

public class SimiNetworkDispatcher extends Thread {
	protected volatile boolean mQuit = false;
	/**
	 * The queue of requests to service.
	 */
	protected BlockingQueue<SimiRequest> mQueue;
	/**
	 * The network interface for processing requests.
	 */
	protected SimiNetwork mNetwork;

	protected SimiExecutorDelivery mDelivery;

	protected SimiNetworkCacheL1 mCache;

	public SimiNetworkDispatcher(BlockingQueue<SimiRequest> queue,
			SimiNetwork network, SimiExecutorDelivery delivery,
			SimiNetworkCacheL1 cache) {
		mQueue = queue;
		mNetwork = network;
		mDelivery = delivery;
		mCache = cache;
	}

	public void quit() {
		mQuit = true;
		interrupt();
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			SimiRequest request = null;
			try {
				request = mQueue.take();
			} catch (InterruptedException e) {
				Log.e("SimiNetworkDispactcher ",
						"InterruptedException" + e.getMessage());
				if (mQuit) {
					return;
				}
				continue;

			}

			if (request.isCancel()) {
				request.finish();
				continue;
			}
			SimiNetworkResponse netResponse = mNetwork.performRequest(request);
			if (netResponse.getStatusCode() == SimiNetworkResponse.StatusRequest.FAIL) {
				SimiManager.getIntance().getRequestQueue().getNetworkQueue()
						.remove(request);
				CoreResponse response = new CoreResponse();
				SimiError simiError = new SimiError();
				simiError.setStatusCode(netResponse.getStatusRequest());
				response.setSimiError(simiError);
				mDelivery.postResponse(request, response);
				return;
			}
			CoreResponse response = request.parseNetworkResponse(netResponse);
			if (null == response) {
				SimiManager.getIntance().getRequestQueue().getNetworkQueue()
						.remove(request);
				// Thread.currentThread().interrupt();
				response = new CoreResponse();
				SimiError simiError = new SimiError();
				response.setSimiError(simiError);
				mDelivery.postResponse(request, response);
				return;
			}

			if (request.isShouldCache()) {
				String url_cache = request.getCacheKey();
				response.parse();
				mCache.put(url_cache, response.getDataJSON());
			}

			SimiManager.getIntance().getRequestQueue().getNetworkQueue()
					.remove(request);
			mDelivery.postResponse(request, response);
		}

	}

}
