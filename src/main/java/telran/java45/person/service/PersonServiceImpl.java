package telran.java45.person.service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import telran.java45.person.dao.PersonRepository;
import telran.java45.person.dto.AddressDto;
import telran.java45.person.dto.ChildDto;
import telran.java45.person.dto.CityPopulationDto;
import telran.java45.person.dto.EmployeeDto;
import telran.java45.person.dto.PersonDto;
import telran.java45.person.dto.exeptions.PersonNotFoundExeption;
import telran.java45.person.model.Address;
import telran.java45.person.model.Child;
import telran.java45.person.model.Employee;
import telran.java45.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;
	private final Map<Type, Type> typeMap = Map.of(
			PersonDto.class, Person.class,
			ChildDto.class, Child.class,
			EmployeeDto.class, Employee.class,
			Person.class, PersonDto.class,
			Child.class, ChildDto.class,
			Employee.class, EmployeeDto.class,
			Address.class, AddressDto.class,
			AddressDto.class, Address.class
			);

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(fromDto(personDto));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);

		return toDto(person);
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		personRepository.deleteById(id);
		return toDto(person);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		person.setName(name);
		return toDto(person);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		Address address = modelMapper.map(addressDto, typeMap.get(addressDto.getClass()));
		person.setAddress(address);
		return toDto(person);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		List<PersonDto> list = personRepository.findPersonsByAddressCity(city)
				.map(p -> toDto(p))
				.toList();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		List<PersonDto> list = personRepository.findPersonsByName(name).map(p -> toDto(p))
				.toList();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAges(Integer minAge, Integer maxAge) {
		List<PersonDto> list = personRepository
				.findPersonsByBirthDateBetween(LocalDate.now().minusYears(maxAge), LocalDate.now().minusYears(minAge))
				.map(p -> toDto(p)).toList();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		List<CityPopulationDto> list = personRepository.getCitiesPopulation();
		return list;

	}

	private PersonDto toDto(Person person) {
		return modelMapper.map(person, typeMap.get(person.getClass()));
	}
	
	private Person fromDto(PersonDto personDto) {
		return modelMapper.map(personDto, typeMap.get(personDto.getClass()));
	}
	
	@Override
	public void run(String... args) {
		if (personRepository.count() == 0) {
			Person person = new Person(1000, "John", LocalDate.of(1995, 4, 1),
					new Address("Rehovot", "Ben Gvirol", "87"));
			Child child = new Child(2000, "Moshe", LocalDate.of(2005, 4, 1),
					new Address("Rehovot", "Ben Gvirol", "87"), "Shalom");
			Employee employee = new Employee(3000, "Sarah", LocalDate.of(1985, 4, 1),
					new Address("Rehovot", "Ben Gvirol", "87"), "Motorola", 20000);
			personRepository.save(person);
			personRepository.save(child);
			personRepository.save(employee);
		}
	}
}
