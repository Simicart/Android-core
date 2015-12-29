package com.simicart.core.base.network.request.multi;

import java.util.concurrent.BlockingQueue;

import android.os.Process;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.response.CoreResponse;

public class SimiNetworkDispatcher extends Thread {
	protected volatile boolean mQuit = false;
	/** The queue of requests to service. */
	protected BlockingQueue<SimiRequest> mQueue;
	/** The network interface for processing requests. */
	protected SimiNetwork mNetwork;

	protected SimiExecutorDelivery mDelivery;

	public SimiNetworkDispatcher(BlockingQueue<SimiRequest> queue,
			SimiNetwork network, SimiExecutorDelivery delivery) {
		mQueue = queue;
		mNetwork = network;
		mDelivery = delivery;
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
			if (null == netResponse) {
				SimiManager.getIntance().getRequestQueue().getNetworkQueue()
						.remove(request);
				// Thread.currentThread().interrupt();
				CoreResponse response = new CoreResponse();
				mDelivery.postResponse(request, response);
				return;
			}
			CoreResponse response = request.parseNetworkResponse(netResponse);
			if (null == response) {
				SimiManager.getIntance().getRequestQueue().getNetworkQueue()
						.remove(request);
				// Thread.currentThread().interrupt();
				response = new CoreResponse();
				mDelivery.postResponse(request, response);
				return;
			}
			SimiManager.getIntance().getRequestQueue().getNetworkQueue()
					.remove(request);
			mDelivery.postResponse(request, response);
		}

	}

}
