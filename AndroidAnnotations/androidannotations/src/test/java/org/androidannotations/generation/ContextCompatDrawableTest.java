/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.androidannotations.generation;

import java.io.File;

import org.androidannotations.AndroidAnnotationProcessor;
import org.androidannotations.utils.AAProcessorTestHelper;
import org.junit.Before;
import org.junit.Test;

public class ContextCompatDrawableTest extends AAProcessorTestHelper {

	private static final String DRAWABLE_SIGNATURE = ".*myDrawable = resources_\\.getDrawable\\(drawable\\.myDrawable\\);.*";
	private static final String DRAWABLE_VIA_SUPPORT_SIGNATURE = ".*myDrawable = ContextCompat\\.getDrawable\\(this, drawable\\.myDrawable\\);.*";

	@Before
	public void setUp() {
		addProcessor(AndroidAnnotationProcessor.class);
		addManifestProcessorParameter(ContextCompatDrawableTest.class, "AndroidManifestForDrawable.xml");
	}

	@Test
	public void activityCompilesWithRegularDrawable() {
		CompileResult result = compileFiles(ActivityWithDrawable.class);
		File generatedFile = toGeneratedFile(ActivityWithDrawable.class);

		assertCompilationSuccessful(result);
		assertGeneratedClassMatches(generatedFile, DRAWABLE_SIGNATURE);
	}

	@Test
	public void activityCompilesWithContextCompatDrawable() {
		// To simulate android support v4 in classpath, we add
		// android.support.v4.content.ContextCompat
		// in classpath
		CompileResult result = compileFiles(toPath(ContextCompatDrawableTest.class, "ContextCompat.java"), ActivityWithDrawable.class);
		File generatedFile = toGeneratedFile(ActivityWithDrawable.class);

		assertCompilationSuccessful(result);
		assertGeneratedClassMatches(generatedFile, DRAWABLE_VIA_SUPPORT_SIGNATURE);
	}
}