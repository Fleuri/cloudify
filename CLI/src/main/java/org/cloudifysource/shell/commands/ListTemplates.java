/*******************************************************************************
 * Copyright (c) 2013 GigaSpaces Technologies Ltd. All rights reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *******************************************************************************/
package org.cloudifysource.shell.commands;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.felix.gogo.commands.Command;
import org.cloudifysource.domain.cloud.compute.ComputeTemplate;
import org.cloudifysource.dsl.internal.CloudifyConstants;
import org.cloudifysource.dsl.rest.response.ListTemplatesResponse;
import org.cloudifysource.restclient.RestClient;
import org.cloudifysource.shell.ShellUtils;
import org.cloudifysource.shell.rest.RestAdminFacade;

/**
 * 
 * List all cloud's templates.
 * 
 * Command syntax: 
 * 			list-templates 
 * 
 * @author yael
 * 
 * @since 2.3.0
 *
 */
@Command(scope = "cloudify", name = "list-templates", description = "List all cloud's templates")
public class ListTemplates extends AdminAwareCommand implements NewRestClientCommand {

	@Override
	protected Object doExecute() throws Exception {
		Map<String, ComputeTemplate> templatesList = adminFacade.listTemplates();
		String formattedList = getTemplatesListAsString(templatesList);
	    
		return formattedList;
	}

	private String getTemplatesListAsString(
			final Map<String, ComputeTemplate> templatesList) {
		if (templatesList == null || templatesList.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder("{");
		sb.append(CloudifyConstants.NEW_LINE);
		for (Entry<String, ComputeTemplate> entry : templatesList.entrySet()) {
			String cloudTemplateStr = entry.getValue().toFormatedString();
			cloudTemplateStr = cloudTemplateStr.replaceAll(CloudifyConstants.NEW_LINE, 
					CloudifyConstants.NEW_LINE 
					+ CloudifyConstants.TAB_CHAR
					+ CloudifyConstants.TAB_CHAR);
			
			sb.append(CloudifyConstants.TAB_CHAR)
			.append(ShellUtils.getBoldMessage(entry.getKey() + ":"))
			.append(CloudifyConstants.NEW_LINE)
			.append(CloudifyConstants.TAB_CHAR)
			.append(CloudifyConstants.TAB_CHAR)
			.append(cloudTemplateStr)
			.append(",")
			.append(CloudifyConstants.NEW_LINE);
		}
		String result = sb.toString();
		result = result.substring(0, result.lastIndexOf(","));
		return result + CloudifyConstants.NEW_LINE + "}";
	}

	@Override
	public Object doExecuteNewRestClient() throws Exception {
		final RestClient newRestClient = ((RestAdminFacade) getRestAdminFacade()).getNewRestClient();
		ListTemplatesResponse listTemplates = newRestClient.listTemplates();
		return getTemplatesListAsString(listTemplates.getTemplates());
	}

}
