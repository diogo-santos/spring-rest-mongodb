/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationRestTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepo;

	@Before
	public void deleteAllBeforeTests() throws Exception {
		personRepo.deleteAll();
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.people").exists());
	}

	@Test
	public void shouldCreateEntity() throws Exception {
		mockMvc.perform(post("/people")
						.content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("people/")));
	}

	@Test
	public void shouldRetrieveEntity() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people").content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("GivenName"))
				.andExpect(jsonPath("$.lastName").value("LastName"));
	}

	@Test
	public void shouldQueryEntity() throws Exception {
		mockMvc.perform(post("/people").content("{ \"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/people/search/findByLastName?name={name}", "LastName"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.people[0].firstName").value("GivenName"));
	}

	@Test
	public void shouldUpdateEntity() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people").content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated())
									.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content("{\"firstName\": \"Bilbo\", \"lastName\":\"LastName\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Bilbo"))
				.andExpect(jsonPath("$.lastName").value("LastName"));
	}

	@Test
	public void shouldPartiallyUpdateEntity() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people").content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated())
									.andReturn();
		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(patch(location).content("{\"firstName\": \"Bilbo Jr.\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Bilbo Jr."))
				.andExpect(jsonPath("$.lastName").value("LastName"));
	}

	@Test
	public void shouldDeleteEntity() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people").content("{ \"firstName\": \"Bilbo\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}
}