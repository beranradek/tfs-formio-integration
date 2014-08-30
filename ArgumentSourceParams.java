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

import java.util.ArrayList;
import java.util.List;

import net.formio.AbstractRequestParams;
import net.formio.RequestParams;
import net.formio.upload.RequestProcessingError;
import net.formio.upload.UploadedFile;

import org.twinstone.tiles.args.ArgumentSource;
import org.twinstone.tools.file.FileData;

/**
 * Implementation of {@link RequestParams} for Twinstone {@link ArgumentSource}.
 * Values of parameters are retrieved directly from the underlying argument
 * source.
 * @author Radek Beran
 */
public class ArgumentSourceParams extends AbstractRequestParams {
	private final ArgumentSource as;
	
	public ArgumentSourceParams(ArgumentSource as) {
		if (as == null) throw new IllegalArgumentException("argument source cannot be null");
		this.as = as;
	}

	@Override
	public Iterable<String> getParamNames() {
		return as.argumentNames();
	}

	@Override
	public String[] getParamValues(String paramName) {
		List<String> ret = new ArrayList<String>();
		int valuesCnt = as.sizeOf(paramName);
		if (valuesCnt > 0) {
			for (int i = 0; i < valuesCnt; i++) {
				String v = as.tryGet(String.class, paramName, null, i, null);
				if (v != null) {
					ret.add(v);
				}
			}
		} else {
			List<String> values = as.tryGetAll(String.class, paramName, null, null, null);
			if (values != null) {
				ret.addAll(values);
			}
		}
		return ret.toArray(new String[0]);
	}

	@Override
	public UploadedFile[] getUploadedFiles(String paramName) {
		List<UploadedFile> files = new ArrayList<UploadedFile>();
		int valuesCnt = as.sizeOf(paramName);
		if (valuesCnt > 0) {
			for (int i = 0; i < valuesCnt; i++) {
				FileData fd = as.tryGet(FileData.class, paramName, null, i, null);
				if (fd != null) {
					files.add(new TfsUploadedFile(fd));
				}
			}
		} else {
			List<FileData> values = as.tryGetAll(FileData.class, paramName, null, null, null);
			if (values != null) {
				for (FileData fd : values) {
					if (fd != null) {
						files.add(new TfsUploadedFile(fd));
					}
				}
			}
		}
		return files.toArray(new UploadedFile[0]);
	}

	@Override
	public RequestProcessingError getRequestError() {
		// ArgumentSource itself controls possible thresholds and request/file size limits
		return null;
	}
	
}