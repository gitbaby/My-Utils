package com.conceptapp.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Dot {

	private static final char pixel[] = {'G','I','F','8','9','a','\u0001',
		'\u0000','\u0001','\u0000','\u0080','\u0000','\u0000','\u0000','\u0000',
		'\u0000','\u0000','\u0000','\u0000','\u0021','\u00f9','\u0004','\u0001',
		'\u0000','\u0000','\u0000','\u0000','\u002c','\u0000','\u0000','\u0000',
		'\u0000','\u0001','\u0000','\u0001','\u0000','\u0000','\u0002','\u0002',
		'\u0044','\u0001','\u0000','\u003b'};

	public static void writeDot(HttpServletResponse response) throws IOException {
		response.setContentType("image/gif");
		response.getWriter().write(pixel);
	}

}