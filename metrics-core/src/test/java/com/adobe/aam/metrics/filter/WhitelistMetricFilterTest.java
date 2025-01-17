/*
 * Copyright 2018 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */

package com.adobe.aam.metrics.filter;

import com.adobe.aam.metrics.metric.Metric;
import com.adobe.aam.metrics.metric.SimpleMetric;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class WhitelistMetricFilterTest {

	@Test
	public void testWhitelistAll() {
		List<String> whitelist = Lists.newArrayList();
		whitelist.add("*");
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertTrue(filter.isAllowed(metric("metric.name")));
	}

	@Test
	public void testWhitelistWithValidItems() {
		List<String> whitelist = Lists.newArrayList();
		whitelist.add("good.metric");
		whitelist.add("better.metric");
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertTrue(filter.isAllowed(metric("good.metric")));
		Assert.assertTrue(filter.isAllowed(metric("better.metric")));
		Assert.assertFalse(filter.isAllowed(metric("bad.metric")));
	}

	@Test
	public void testCaseInsensitive() {
		List<String> whitelist = Lists.newArrayList();
		whitelist.add("GooD");
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertTrue(filter.isAllowed(metric("good")));
		Assert.assertTrue(filter.isAllowed(metric("gOOd")));
		Assert.assertTrue(filter.isAllowed(metric("gOOD")));
		Assert.assertFalse(filter.isAllowed(metric("bad")));
	}

	@Test
	public void testEmptyWhitelist() {
		List<String> whitelist = Lists.newArrayList();
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertFalse(filter.isAllowed(metric("metric.name")));
		Assert.assertFalse(filter.isAllowed(metric("other.metric.name")));
	}

	@Test
	public void testBlankItem() {
		List<String> whitelist = Lists.newArrayList();
		whitelist.add("metric.name");
		whitelist.add("");
		whitelist.add("  ");
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertTrue(filter.isAllowed(metric("metric.name")));
	}

	@Test
	public void testItemWithEndingRegex() {
		List<String> whitelist = Lists.newArrayList();
		whitelist.add("pcs.*");
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertTrue(filter.isAllowed(metric("pcs.metric1")));
		Assert.assertFalse(filter.isAllowed(metric("other.metric")));
		Assert.assertFalse(filter.isAllowed(metric("other.metric.pcs")));
	}

	@Test
	public void testItemWithStartAndEndRegex() {
		List<String> whitelist = Lists.newArrayList();
		whitelist.add("*pcs*");
		WhitelistMetricFilter filter = new WhitelistMetricFilter(whitelist);
		Assert.assertTrue(filter.isAllowed(metric("some.pcs.metric")));
		Assert.assertTrue(filter.isAllowed(metric("pcs.metric")));
		Assert.assertTrue(filter.isAllowed(metric("metric.pcs")));
		Assert.assertFalse(filter.isAllowed(metric("other.metric")));
	}

	private Metric metric(String name) {
		return new SimpleMetric(name, Metric.Type.COUNT, 0);
	}
}
