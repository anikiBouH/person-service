package telran.java45.person.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonTypeName("ChildDto")
public class ChildDto extends PersonDto{
	String kindergarten;
}
