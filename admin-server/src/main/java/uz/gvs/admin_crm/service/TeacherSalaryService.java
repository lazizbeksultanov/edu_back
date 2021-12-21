package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Teacher;
import uz.gvs.admin_crm.entity.TeacherSalary;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.payload.TeacherSalaryDto;
import uz.gvs.admin_crm.repository.PayTypeRepository;
import uz.gvs.admin_crm.repository.TeacherRepository;
import uz.gvs.admin_crm.repository.TeacherSalaryRepository;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherSalaryService {
    @Autowired
    TeacherSalaryRepository teacherSalaryRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    PayTypeRepository payTypeRepository;

    public ApiResponse minusAmount(TeacherSalaryDto teacherSalaryDto) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherSalaryDto.getTeacherId());
        if (teacher.isPresent()) {
            if (teacherSalaryDto.getPayTypeId() != null && teacherSalaryDto.getAmountDate() != null) {
                if (makeAmount(teacherSalaryDto) != null) {
                    Teacher teacher1 = teacher.get();
                    teacher1.setBalance(teacher1.getBalance() - teacherSalaryDto.getAmount());
                    teacherRepository.save(teacher1);
                    return apiResponseService.saveResponse();
                }
                return apiResponseService.tryErrorResponse();
            }
            return apiResponseService.notEnoughErrorResponse();
        }
        return apiResponseService.notFoundResponse();
    }

    public TeacherSalary makeAmount(TeacherSalaryDto teacherSalaryDto) {
        try {
            TeacherSalary teacherSalary = new TeacherSalary();
            teacherSalary.setTeacher(teacherRepository.findById(teacherSalaryDto.getTeacherId()).orElseThrow(() -> new ResourceNotFoundException("get Teacher")));
            teacherSalary.setPayType(payTypeRepository.findById(teacherSalaryDto.getPayTypeId()).orElseThrow(() -> new ResourceNotFoundException("get PayType")));
            teacherSalary.setAmount(teacherSalaryDto.getAmount());
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            teacherSalary.setAmountDate(teacherSalaryDto.getAmountDate() != null ? formatter1.parse(teacherSalaryDto.getAmountDate()) : null);
            teacherSalary.setDescription(teacherSalaryDto.getDescription());
            return teacherSalaryRepository.save(teacherSalary);
        } catch (Exception e) {
            return null;
        }
    }

    public ApiResponse getSalaries(UUID id, int page, int size) {
        Teacher byId = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getTeacher"));
        Page<TeacherSalary> all = teacherSalaryRepository.findAllByTeacher(byId, PageRequest.of(page, size));
        return apiResponseService.getResponse(
                new PageableDto(
                        all.getTotalPages(),
                        all.getTotalElements(),
                        all.getNumber(),
                        all.getSize(),
                        all.get().map(this::makeTeacherSalaryDto).collect(Collectors.toList())
                ));
    }

    public TeacherSalaryDto makeTeacherSalaryDto(TeacherSalary teacherSalary) {
        return new TeacherSalaryDto(
                teacherSalary.getId(),
                teacherSalary.getTeacher().getUser().getFullName(),
                teacherSalary.getTeacher().getId(),
                teacherSalary.getAmount(),
                teacherSalary.getAmountDate() != null ? teacherSalary.getAmountDate().toString() : "",
                teacherSalary.getDescription(),
                teacherSalary.getPayType()
        );
    }

    public ApiResponse editSalary(UUID id, TeacherSalaryDto teacherSalaryDto) {
        try {
            Optional<TeacherSalary> optional = teacherSalaryRepository.findById(id);
            if (optional.isPresent()) {
                if (teacherSalaryDto.getTeacherId() != null && teacherSalaryDto.getPayTypeId() != null && teacherSalaryDto.getAmountDate() != null) {
                    TeacherSalary teacherSalary = optional.get();
                    if (teacherSalaryDto.getTeacherId().equals(teacherSalary.getTeacher().getId())) {
                        teacherSalary.setAmount(teacherSalaryDto.getAmount());
                        teacherSalary.setPayType(payTypeRepository.findById(teacherSalaryDto.getPayTypeId()).orElseThrow(() -> new ResourceNotFoundException("get Pay Type")));
                        teacherSalary.setAmountDate(Date.valueOf(teacherSalaryDto.getAmountDate()));
                        teacherSalary.setDescription(teacherSalaryDto.getDescription());
                        teacherSalaryRepository.save(teacherSalary);
                        return apiResponseService.updatedResponse();
                    }
                    return apiResponseService.errorResponse();
                }
                return apiResponseService.notEnoughErrorResponse();
            }
            return apiResponseService.notFoundResponse();
        }catch (Exception e){
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getAllSalaries(int page, int size) {
        Page<TeacherSalary> all = teacherSalaryRepository.findAll(PageRequest.of(page, size));
        return apiResponseService.getResponse(
                new PageableDto(
                        all.getTotalPages(),
                        all.getTotalElements(),
                        all.getNumber(),
                        all.getSize(),
                        all.get().map(this::makeSalaryList).collect(Collectors.toList())
                ));
    }

    public TeacherSalaryDto makeSalaryList(TeacherSalary teacherSalary) {
        return new TeacherSalaryDto(
                teacherSalary.getId(),
                teacherSalary.getTeacher().getUser().getFullName(),
                teacherSalary.getTeacher().getId(),
                teacherSalary.getAmount(),
                teacherSalary.getAmountDate() != null ? teacherSalary.getAmountDate().toString() : "",
                teacherSalary.getDescription(),
                teacherSalary.getPayType()
        );
    }
}
