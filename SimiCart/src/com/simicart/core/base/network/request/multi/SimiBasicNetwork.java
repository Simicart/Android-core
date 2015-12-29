package com.simicart.core.base.network.request.multi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;

import android.util.Log;

public class SimiBasicNetwork implements SimiNetwork {
	private static int DEFAULT_POOL_SIZE = 4096;

	protected final SimiHttpStack mHttpStack;

	protected final ByteArrayPool mPool;

	public SimiBasicNetwork(SimiHttpStack httpStack) {
		// If a pool isn't passed in, then build a small default pool that will
		// give us a lot of
		// benefit and not use too much memory.
		this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 * @param pool
	 *            a buffer pool that improves GC performance in copy operations
	 */
	public SimiBasicNetwork(SimiHttpStack httpStack, ByteArrayPool pool) {
		mHttpStack = httpStack;
		mPool = pool;
	}

	@Override
	public SimiNetworkResponse performRequest(SimiRequest request) {
		while (true) {
			HttpResponse httpResponse = null;
			byte[] responseContents = null;
			try {
				// Gather headers.
				httpResponse = mHttpStack.performRequest(request);
				if (null == httpResponse) {
					throw new IOException();
				}
				StatusLine statusLine = httpResponse.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				Log.e("SimiBasicNetwork " + request.getUrl(), "002 "
						+ statusCode);
				// Some responses such as 204s do not have content. We must
				// check.
				if (httpResponse.getEntity() != null) {
					responseContents = entityToBytes(httpResponse.getEntity());
				} else {
					// Add 0 byte response as a way of honestly representing a
					// no-content request.
					responseContents = new byte[0];
				}

				if (statusCode < 200 || statusCode > 299) {
					throw new IOException();
				}
				return new SimiNetworkResponse(statusCode, responseContents);
			} catch (SocketTimeoutException e) {
				Log.e("SimiBasicNetwork " + request.getUrl(),
						"SocketTimeoutException " + e.getMessage());
				break;
			} catch (ConnectTimeoutException e) {
				Log.e("SimiBasicNetwork " + request.getUrl(),
						"ConnectTimeoutException " + e.getMessage());
				break;
			} catch (MalformedURLException e) {
				Log.e("SimiBasicNetwork " + request.getUrl(),
						"MalformedURLException " + e.getMessage());
				break;
			} catch (IOException e) {
				Log.e("SimiBasicNetwork " + request.getUrl(), "IOException "
						+ e.getMessage());
				break;
			}
		}
		return null;
	}

	/** Reads the contents of HttpEntity into a byte[]. */
	private byte[] entityToBytes(HttpEntity entity) throws IOException {
		PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
				mPool, (int) entity.getContentLength());
		byte[] buffer = null;
		try {
			InputStream in = entity.getContent();
			if (null != in) {
				buffer = mPool.getBuf(1024);
				int count;
				while ((count = in.read(buffer)) != -1) {
					bytes.write(buffer, 0, count);
				}
				return bytes.toByteArray();
			}
		} finally {
			try {
				// Close the InputStream and release the resources by
				// "consuming the content".
				entity.consumeContent();
			} catch (IOException e) {
				// This can happen if there was an exception above that left the
				// entity in
				// an invalid state.

			}
			mPool.returnBuf(buffer);
			bytes.close();
		}
		return null;
	}

}
