package telran.java45.person.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import telran.java45.person.dto.CityPopulationDto;
import telran.java45.person.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

	Stream<Person> findPersonsByName(String name);

	Stream<Person> findPersonsByAddressCity(String city);


	Stream<Person> findPersonsByBirthDateBetween(LocalDate from, LocalDate to);

	@Query("select new telran.java45.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city")
	List<CityPopulationDto> getCitiesPopulation();

}
