package com.example.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource(properties = {"spring.data.mongodb.port=0"})
public class SpringRestApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PersonRepository personRepo;

	@Before
	public void deleteAllBeforeTests() {
		personRepo.deleteAll();
	}

	@Test
	public void whenPerformGetThenReturnEndpointsLinks() throws Exception {
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.people").exists());
	}

	@Test
	public void givenEntityToSaveWhenPerformPostThenEntityIsCreated() throws Exception {
		mockMvc.perform(post("/people")
						.content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("people/")));
	}

	@Test
	public void givenSavedEntityWhenPerformGetThenReturnEntity() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people")
												.content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated())
									.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		Assert.assertNotNull(location);

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("GivenName"))
				.andExpect(jsonPath("$.lastName").value("LastName"));
	}

	@Test
	public void givenSavedEntityWhenPerformGetWithQueryThenReturnEntity() throws Exception {
		mockMvc.perform(post("/people")
						.content("{ \"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/people/search/findByLastName?name={name}", "LastName"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.people[0].firstName").value("GivenName"));
	}

	@Test
	public void givenSavedEntityWhenPerformPutThenEntityIsUpdated() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people")
												.content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated())
									.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		Assert.assertNotNull(location);

		mockMvc.perform(put(location)
							.content("{\"firstName\": \"UpdatedGivenName\", \"lastName\":\"LastName\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("UpdatedGivenName"))
				.andExpect(jsonPath("$.lastName").value("LastName"));
	}

	@Test
	public void givenSavedEntityWhenPerformPatchWithPartialContentThenEntityIsPartialUpdated() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people")
												.content("{\"firstName\": \"GivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated())
									.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		Assert.assertNotNull(location);

		mockMvc.perform(patch(location)
						.content("{\"firstName\": \"UpdatedGivenName\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("UpdatedGivenName"))
				.andExpect(jsonPath("$.lastName").value("LastName"));
	}

	@Test
	public void givenSavedEntityWhenPerformDeleteThenEntityIsDeleted() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/people")
												.content("{ \"firstName\": \"DeletedGivenName\", \"lastName\":\"LastName\"}"))
									.andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		Assert.assertNotNull(location);

		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}
}