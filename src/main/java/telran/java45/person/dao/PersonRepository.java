package telran.java45.person.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import telran.java45.person.model.CityPopulation;
import telran.java45.person.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

	Stream<Person> findPersonsByName(String name);

	Stream<Person> findPersonsByAddressCity(String city);

//	@Query("SELECT * FROM PERSON WHERE (YEAR(LocalDate.now())-YEAR(birthDate))BETWEEN minAge AND maxAge")
//	Stream<Person> findPersonsBetweenAges(Integer minAge, Integer maxAge);

//	@Query("SELECT city, COUNT(*) As population FROM PERSON GROUP BY address")
//	Stream<CityPopulation> getCitiesPopulation();

}
