package telran.java45.person.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java45.person.dao.PersonRepository;
import telran.java45.person.dto.AddressDto;
import telran.java45.person.dto.CityPopulationDto;
import telran.java45.person.dto.PersonDto;
import telran.java45.person.dto.exeptions.PersonNotFoundExeption;
import telran.java45.person.model.Address;
import telran.java45.person.model.CityPopulation;
import telran.java45.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		personRepository.deleteById(id);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		person.setName(name);
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		Address address = modelMapper.map(addressDto, Address.class);
		person.setAddress(address);
		;
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByCity(String city) {
		List<PersonDto> list = personRepository.findPersonsByAddressCity(city).map(p -> modelMapper.map(p, PersonDto.class)).toList();
		return () -> list.iterator();
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByName(String name) {
		List<PersonDto> list = personRepository.findPersonsByName(name).map(p -> modelMapper.map(p, PersonDto.class))
				.toList();
		return () -> list.iterator();
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsBetweenAges(Integer minAge, Integer maxAge) {
		Iterator<Person> personsIterator = personRepository.findAll().iterator();
		List<Person> personsList = new ArrayList<>();
		while (personsIterator.hasNext()) {
			Person person = personsIterator.next();
			if (person.getAge() >= minAge && person.getAge() < maxAge) {
				personsList.add(person);
			}
		}
		return () -> personsList.stream().map(p -> modelMapper.map(p, PersonDto.class)).iterator();
	}

	@Override
	@Transactional
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		Iterator<Person> personsIterator = personRepository.findAll().iterator();
		HashMap<String, Long> cityPopulationHashMap = new HashMap<>();
		while (personsIterator.hasNext()) {
			Person person = personsIterator.next();
			cityPopulationHashMap.computeIfPresent(person.getAddress().getCity(), (k, v) -> v + 1l);
			cityPopulationHashMap.computeIfAbsent(person.getAddress().getCity(), k -> 1l);
		}
		System.out.println("I do");
		return () -> cityPopulationHashMap.keySet().stream().map(key -> {
			return new CityPopulation(key, cityPopulationHashMap.get(key));
		}).map(cp -> modelMapper.map(cp, CityPopulationDto.class)).iterator();

	}
}
