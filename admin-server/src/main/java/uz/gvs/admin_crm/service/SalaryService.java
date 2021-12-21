package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Teacher;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.TeacherDto;
import uz.gvs.admin_crm.repository.TeacherRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    ApiResponseService apiResponseService;


    public ApiResponse saveSalary(UUID id,TeacherDto teacherDto) {
        try {
            Optional<Teacher> optional = teacherRepository.findById(id);
            if (optional.isPresent()) {
                if (teacherDto.getSalary() != null) {
                    Teacher teacher = optional.get();
                        teacher.setSalary(teacherDto.getSalary());
                        teacher.setPercent(teacherDto.isPercent());
                        teacherRepository.save(teacher);
                        return apiResponseService.saveResponse();
                }
                return apiResponseService.notEnoughErrorResponse();
            }
            return apiResponseService.notFoundResponse();
        }catch (Exception e){
            return apiResponseService.tryErrorResponse();
        }
    }

}
