package com.simicart.core.base.networkcloud.request.multi;

import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

public class SimiNetworkResponse {

    public interface StatusRequest {
        int FAIL = 0;
        int SUCCESS = 1;
    }

    /**
     * The HTTP status code.
     */
    public int statusCode;

    /**
     * Raw data from this response.
     */
    public byte[] data;

    /**
     * Response headers.
     */
    public Map<String, String> headers;

    /**
     * True if the server returned a 304 (Not Modified).
     */
    public boolean notModified;

    /**
     * Network roundtrip time in milliseconds.
     */
    public long networkTimeMs;

    protected int statusRequest = StatusRequest.SUCCESS;

    public void setStatusRequest(int status) {
        statusRequest = status;
    }

    public int getStatusRequest() {
        return statusRequest;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public SimiNetworkResponse(int statusCode, byte[] data,
                               Map<String, String> headers, boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }

    public SimiNetworkResponse(int statusCode, byte[] data,
                               Map<String, String> headers, boolean notModified) {
        this(statusCode, data, headers, notModified, 0);
    }

    public SimiNetworkResponse(int statusCode, byte[] data) {
        this(statusCode, data, Collections.<String, String>emptyMap(), false,
                0);
    }

    public SimiNetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(),
                false, 0);
    }

    public SimiNetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false, 0);
    }

}
