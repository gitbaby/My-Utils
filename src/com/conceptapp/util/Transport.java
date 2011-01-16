package com.conceptapp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Transport {

	private static final String userAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; ru-ru) AppleWebKit/533.18.1 (KHTML, like Gecko) Version/5.0.2 Safari/533.18.5";

	private URLConnection connection;

	public Transport() {
	}

	public Transport(String url) throws MalformedURLException, IOException {
		setUrl(url);
	}

	public void setUrl(String url) throws MalformedURLException, IOException {
		connection = new URL(url).openConnection();
		connection.setRequestProperty("User-Agent", userAgent);
	}

	public void setConnection(URLConnection connection) {
		this.connection = connection;
	}

	public URLConnection getConnection() {
		return connection;
	}

	public String getContentType() {
		return connection.getContentType();
	}

	public byte[] fetchBinary() throws IOException {
		int contentLength = connection.getContentLength();
		InputStream raw = connection.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1) {
				break;
			}
			offset += bytesRead;
		}
		in.close();
		raw.close();
		return data;
	}

	public String fetchString() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder content = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			content.append(line + "\n");
		}
		bufferedReader.close();
		return content.toString();
	}

}