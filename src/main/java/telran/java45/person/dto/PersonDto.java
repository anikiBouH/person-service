package telran.java45.person.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PersonDto {
    Integer id;
    String name;
    LocalDate birthDate;
    AddressDto address;
}
