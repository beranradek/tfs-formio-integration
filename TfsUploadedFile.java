/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.formio.tfs;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

import net.formio.upload.UploadedFile;

import org.twinstone.tools.file.FileData;

/**
 * Implementation of {@link UploadedFile} that uses {@link FileData} from twinstone tools.
 * @author Radek Beran
 */
public class TfsUploadedFile implements UploadedFile {
	private static final long serialVersionUID = 1345521975622862277L;
	protected FileData fileData;
	private final String fileName;
	private final String contentType;
	private final long size;
	
	public TfsUploadedFile(FileData fileData) {
		if (fileData == null) throw new IllegalArgumentException("fileData cannot be null");
		this.fileData = fileData;
		this.fileName = fileData.getName();
		this.contentType = fileData.getContentType();
		this.size = fileData.getSize();
	}
	
	@Override
	public ReadableByteChannel getContent() throws IOException {
		assertNotCleared();
		return fileData.getContent();
	}

	@Override
	public void deleteTempFile() {
		// TODO: Call cleanup method in FileData implementation
		// if available in future version of TFS.
		// if (this.fileData instanceof RequestFileData) {
		//   ((RequestFileData)this.fileData)deleteFile();
		// }
		this.fileData = null;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public long getSize() {
		return this.size;
	}
	
	@Override
	public String toString() {
		return "File " + getFileName() + ", size=" + getSize()+", type=" + getContentType();
	}
	
	/**
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		deleteTempFile();
	}
	
	private void assertNotCleared() {
		if (fileData == null) throw new IllegalStateException("file item has been already cleared");
	}

}
