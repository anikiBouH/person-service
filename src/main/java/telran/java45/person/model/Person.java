package telran.java45.person.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class Person implements Serializable{
	
	private static final long serialVersionUID = -800424324198884032L;
	@Id
	Integer id;
	@Setter
	String name;
	LocalDate birthDate;
	@Setter
//	@EmbeddedId
	Address address;
	
	public Integer getAge() {
		return Period.between(birthDate, LocalDate.now()).getYears();
	}
}
