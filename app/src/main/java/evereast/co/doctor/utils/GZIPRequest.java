package evereast.co.doctor.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class GZIPRequest extends Request<byte[]> {

    private static final String TAG = GZIPRequest.class.getSimpleName();

    private final Response.Listener<byte[]> mListener;

    public GZIPRequest(int method, String url, Response.Listener<byte[]> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        try {
            // Check if the response is GZIP compressed
            String contentEncoding = response.headers.get("Content-Encoding");
            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                // Decompress the GZIP data
                InputStream inputStream = new ByteArrayInputStream(response.data);
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] decompressedData = outputStream.toByteArray();
                return Response.success(decompressedData, HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (IOException e) {
            Log.e(TAG, "Error decompressing response: " + e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // Set the "Accept-Encoding" header to indicate support for GZIP compression
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip");
        return headers;
    }
}
