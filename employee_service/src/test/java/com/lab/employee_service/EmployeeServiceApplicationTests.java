package com.lab.employee_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.lab.employee_service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceApplicationTests {

	@InjectMocks
	private EmployeeServiceImpl service;

	@Mock
	private EmployeeRepository repository;

	@Mock
	private EmployeeModelAssembler assembler;

	@Mock
	private DepartmentClient departmentClient;

	private AutoCloseable closeable;

	private Employee sampleEmployee;
	private EmployeeDTO sampleDTO;
	private EntityModel<EmployeeDTO> sampleModel;

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);

		sampleEmployee = new Employee();
		sampleEmployee.setId(1L);
		sampleEmployee.setName("John Doe");
		sampleEmployee.setRole("Developer");
		sampleEmployee.setEmail("john@example.com");
		sampleEmployee.setDepartmentId(100L);
		sampleEmployee.setUserId(200L);

		sampleDTO = new EmployeeDTO();
		sampleModel = EntityModel.of(sampleDTO);
		sampleModel.add(Link.of("/employees/1").withSelfRel());
	}

	@Test
	void testFindAll() {
		when(repository.findAll()).thenReturn(Arrays.asList(sampleEmployee));
		when(assembler.toModel(any())).thenReturn(sampleModel);

		CollectionModel<EntityModel<EmployeeDTO>> result = service.findAll();

		assertThat(result).isNotNull();
		assertThat(result.getContent().size()).isEqualTo(1);
		verify(repository, times(1)).findAll();
	}

	@Test
	void testNewEmployee() {
		when(repository.save(any())).thenReturn(sampleEmployee);
		when(assembler.toModel(any())).thenReturn(sampleModel);

		ResponseEntity<?> response = service.newEmployee(sampleDTO);
		assertEquals(201, response.getStatusCodeValue());
		verify(repository).save(any());
	}

	@Test
	void testFindByIdExists() {
		when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
		when(assembler.toModel(any())).thenReturn(sampleModel);

		ResponseEntity<?> response = service.findById(1L);
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	void testFindByIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void testFindByEmailExists() {
		when(repository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleEmployee));
		when(assembler.toModel(any())).thenReturn(sampleModel);

		EntityModel<EmployeeDTO> result = service.findByEmail("john@example.com");
		assertThat(result).isNotNull();
	}

	@Test
	void testFindByEmailNotFound() {
		when(repository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findByEmail("notfound@example.com"));
	}

	@Test
	void testSaveNewEmployee() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		when(repository.save(any())).thenReturn(sampleEmployee);
		when(assembler.toModel(any())).thenReturn(sampleModel);

		ResponseEntity<?> response = service.save(sampleDTO, 1L);
		assertEquals(201, response.getStatusCodeValue());
	}

	@Test
	void testSaveExistingEmployee() {
		when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
		when(repository.save(any())).thenReturn(sampleEmployee);
		when(assembler.toModel(any())).thenReturn(sampleModel);

		ResponseEntity<?> response = service.save(sampleDTO, 1L);
		assertEquals(201, response.getStatusCodeValue());
	}

	@Test
	void testDeleteById() {
		doNothing().when(repository).deleteById(1L);

		ResponseEntity<?> response = service.deleteById(1L);
		assertEquals(204, response.getStatusCodeValue());
		verify(repository, times(1)).deleteById(1L);
	}

}
