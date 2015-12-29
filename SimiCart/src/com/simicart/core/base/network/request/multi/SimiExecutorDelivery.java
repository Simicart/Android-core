package com.simicart.core.base.network.request.multi;

import java.util.concurrent.Executor;

import android.os.Handler;

import com.simicart.core.base.network.response.CoreResponse;

public class SimiExecutorDelivery {
	/** Used for posting responses, typically to the main thread. */
	protected Executor mResponsePoster;

	/**
	 * Creates a new response delivery interface.
	 * 
	 * @param handler
	 *            {@link Handler} to post responses on
	 */
	public SimiExecutorDelivery(final Handler handler) {
		// Make an Executor that just wraps the handler.
		mResponsePoster = new Executor() {
			@Override
			public void execute(Runnable command) {
				handler.post(command);
			}
		};
	}

	/**
	 * Creates a new response delivery interface, mockable version for testing.
	 * 
	 * @param executor
	 *            For running delivery tasks
	 */
	public SimiExecutorDelivery(Executor executor) {
		mResponsePoster = executor;
	}

	public void postResponse(SimiRequest request, CoreResponse response) {
		mResponsePoster
				.execute(new ResponseDeliveryRunnable(request, response));
	}

	private class ResponseDeliveryRunnable implements Runnable {
		private final SimiRequest mRequest;
		private final CoreResponse mResponse;

		public ResponseDeliveryRunnable(SimiRequest request,
				CoreResponse response) {
			mRequest = request;
			mResponse = response;
		}

		@Override
		public void run() {
			mRequest.deliveryCoreResponse(mResponse);
		}
	}

}
