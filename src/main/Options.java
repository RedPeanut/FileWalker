/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package main;

/**
 * Command line options for google-java-format.
 *
 * <p>google-java-format doesn't depend on AutoValue, to allow AutoValue to depend on google-java-format.
 */
final class Options {

	private String dir;

	Options(String dir) {
		this.dir = dir;
	}

	String dir() {
		return dir;
	}

	static Builder builder() {
		return new Builder();
	}

	static class Builder {

		private String dir = ".";

		Builder dir(String dir) {
			this.dir = dir;
			return this;
		}

		Options build() {
			return new Options(dir);
		}
	}
}
