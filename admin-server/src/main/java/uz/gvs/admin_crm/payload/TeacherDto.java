package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    private UUID id;
    private UserDto userDto;
    private Double balance;
    private boolean isPercent;
    private Double salary;



    public TeacherDto(UserDto userDto, Double balance) {
        this.userDto = userDto;
        this.balance = balance;
    }


}
