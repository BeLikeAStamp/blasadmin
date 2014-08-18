package com.belikeastamp.admin.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

public class CustomMultiPartEntity
{

	private final ProgressListener listener;
	private MultipartEntityBuilder builder;
	private HttpEntity entity;
	
	public CustomMultiPartEntity(final ProgressListener listener)
	{
		this.builder = MultipartEntityBuilder.create();
		this.entity = this.builder.build();
		this.listener = listener;
	}
	
	public CustomMultiPartEntity(MultipartEntityBuilder builder, final ProgressListener listener)
	{
		this.builder = builder;
		this.entity = this.builder.build();
		this.listener = listener;
	}
	
	
	public CustomMultiPartEntity(final HttpMultipartMode mode, final ProgressListener listener)
	{
		this.builder = MultipartEntityBuilder.create();
		this.builder.setMode(mode);
		this.entity = this.builder.build();
		this.listener = listener;
	}

	public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener)
	{
		this.builder = MultipartEntityBuilder.create();
		this.builder.setMode(mode);
		this.builder.setCharset(charset);
		this.entity = this.builder.build();
		this.listener = listener;
	}

	public void writeTo(final OutputStream outstream) throws IOException
	{
		
		this.entity.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener
	{
		void transferred(long num);
	}

	public static class CountingOutputStream extends FilterOutputStream
	{

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out, final ProgressListener listener)
		{
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException
		{
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

		public void write(int b) throws IOException
		{
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
	
	public void addPart(String s, FileBody fb) {
		this.builder.addPart(s, fb);
	}
	
	public long getContentLength() {
		return this.entity.getContentLength();
	}
	
	public HttpEntity getEntity() {
		return this.entity;
	}
}