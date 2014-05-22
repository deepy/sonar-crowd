/*
 * Sonar Crowd Plugin
 * Copyright (C) 2009 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.crowd;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.service.client.CrowdClient;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

import org.junit.Test;

import org.sonar.api.utils.SonarException;

import java.util.LinkedList;
import java.util.List;

public class CrowdGroupsProviderTest {

  @Test
  public void returnsNullIfTheUserWasNotFound() throws Exception {
    CrowdClient client = mock(CrowdClient.class);
    when(client.getGroupsForNestedUser(anyString(), anyInt(), anyInt())).thenThrow(
        new UserNotFoundException(""));
    assertThat(new CrowdGroupsProvider(client).doGetGroups("user"), is(nullValue()));
  }

  @Test(expected = SonarException.class)
  public void throwsSonarExceptionIfCommunicationWithCrowdFails() throws Exception {
    CrowdClient client = mock(CrowdClient.class);
    when(client.getGroupsForNestedUser(anyString(), anyInt(), anyInt())).thenThrow(
        new OperationFailedException(""));
    new CrowdGroupsProvider(client).doGetGroups("user");
  }

  private List<Group> makeGroups(int count, int offset) {
    Builder<Group> builder = new Builder<Group>();
    for (int i = 0; i < count; i++) {
      Group group = mock(Group.class);
      when(group.getName()).thenReturn("group" + (offset + i));
      builder.add(group);
    }
    return builder.build();
  }

  private List<Group> makeGroups(int count) {
    return makeGroups(count, 0);
  }

  @Test
  public void returnsGroups() throws Exception {
    CrowdClient client = mock(CrowdClient.class);

    List<Group> groups = makeGroups(10);
    when(client.getGroupsForNestedUser(anyString(), anyInt(), anyInt())).thenReturn(groups);

    List<String> resolvedGroups =
        Lists.newLinkedList(new CrowdGroupsProvider(client).doGetGroups("user"));
    assertThat(resolvedGroups.size(), is(groups.size()));
    for (int i = 0; i < groups.size(); i++) {
      assertThat(resolvedGroups.get(i), is(groups.get(i).getName()));
    }
    verify(client, times(1)).getGroupsForNestedUser(anyString(), anyInt(), anyInt());
  }

  @Test
  public void performsPagination() throws Exception {
    CrowdClient client = mock(CrowdClient.class);

    List<Group> firstGroups = makeGroups(100);
    List<Group> secondGroups = makeGroups(25, 100);
    List<Group> allGroups = new LinkedList<Group>();
    allGroups.addAll(firstGroups);
    allGroups.addAll(secondGroups);
    when(client.getGroupsForNestedUser(anyString(), eq(0), anyInt())).thenReturn(firstGroups);
    when(client.getGroupsForNestedUser(anyString(), eq(100), anyInt())).thenReturn(secondGroups);

    List<String> resolvedGroups =
        Lists.newLinkedList(new CrowdGroupsProvider(client).doGetGroups("user"));
    assertThat(resolvedGroups.size(), is(125));
    for (int i = 0; i < resolvedGroups.size(); i++) {
      assertThat(resolvedGroups.get(i), is(allGroups.get(i).getName()));
    }
    verify(client, times(2)).getGroupsForNestedUser(anyString(), anyInt(), anyInt());
  }
}
