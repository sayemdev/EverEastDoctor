package evereast.co.doctor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataFetchingThread extends Thread {
    private List<String> urls;
    private List<DataFetchingListener> listeners;

    public DataFetchingThread(List<String> urls, List<DataFetchingListener> listeners) {
        this.urls = urls;
        this.listeners = listeners;
    }

    @Override
    public void run() {
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            DataFetchingListener listener = listeners.get(i);
            try {
                String jsonData = fetchData(url);
                listener.onDataFetched(jsonData);
            } catch (IOException e) {
                listener.onDataFetchError(e);
            }
        }
    }

    private String fetchData(String url) throws IOException {
        URL dataUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) dataUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } else {
            throw new IOException("Failed to fetch data. Response code: " + responseCode);
        }
    }

    public interface DataFetchingListener {
        void onDataFetched(String jsonData);
        void onDataFetchError(IOException e);
    }
}
