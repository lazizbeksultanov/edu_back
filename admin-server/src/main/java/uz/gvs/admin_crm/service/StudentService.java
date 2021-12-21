package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.*;
import uz.gvs.admin_crm.entity.enums.Gender;
import uz.gvs.admin_crm.entity.enums.RoleName;
import uz.gvs.admin_crm.entity.enums.StudentGroupStatus;
import uz.gvs.admin_crm.payload.*;
import uz.gvs.admin_crm.repository.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    UserService userService;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    StudentPaymentRepository studentPaymentRepository;
    @Autowired
    PayTypeRepository payTypeRepository;
    @Autowired
    StudentGroupRepository studentGroupRepository;
    @Autowired
    CashbackRepository cashbackRepository;

    public ApiResponse saveStudent(StudentDto studentDto) {
        try {
            if (userRepository.existsByPhoneNumber(studentDto.getPhoneNumber()))
                return apiResponseService.existResponse();
            Student student = new Student();
            User user = userService.makeUser(new UserDto(
                    studentDto.getFullName(),
                    studentDto.getPhoneNumber(),
                    studentDto.getDescription(),
                    studentDto.getRegionId(),
                    studentDto.getGender(),
                    studentDto.getBirthDate()), RoleName.STUDENT);
            if (user == null)
                return apiResponseService.tryErrorResponse();
            student.setUser(user);
            student.setParentPhone(studentDto.getParentPhone());
            studentRepository.save(student);
            return apiResponseService.saveResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editStudent(UUID id, StudentDto studentDto) {
        try {
            Optional<Student> byId = studentRepository.findById(id);
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            if (byId.isPresent()) {
                Student student = byId.get();
                User user = student.getUser();
                boolean b = userRepository.existsByPhoneNumberAndIdNot(studentDto.getPhoneNumber(), user.getId());
                if (b) {
                    return apiResponseService.existResponse();
                }
                user.setPhoneNumber(studentDto.getPhoneNumber());
                user.setFullName(studentDto.getFullName());
                user.setDescription(studentDto.getDescription());
                user.setBirthDate(user.getBirthDate() != null ? formatter1.parse(studentDto.getBirthDate()) : null);
                user.setGender(Gender.valueOf(studentDto.getGender()));
                user.setRegion(studentDto.getRegionId() != null && studentDto.getRegionId() > 0 ? regionRepository.findById(studentDto.getRegionId()).get() : null);
                student.setUser(userRepository.save(user));
                student.setParentPhone(studentDto.getParentPhone());
                student.setBalans(studentDto.getBalans());
                studentRepository.save(student);
                return apiResponseService.saveResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getGroupStudents(Integer id) {
        try {
            List<Student> studentList = studentRepository.findAllByStudentGroup_Group_id(id);
            List<StudentDto> studentDtos = new ArrayList<>();
            for (Student student : studentList) {
                UUID groupId = null;
                for (StudentGroup studentGroup : student.getStudentGroup()) {
                    if (studentGroup.getGroup().getId().equals(id)) {
                        groupId = studentGroup.getId();
                        break;
                    }
                }
                studentDtos.add(makeGroupStudentDtoList(student, groupId));
            }
            return apiResponseService.getResponse(studentDtos);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public StudentDto makeGroupStudentDtoList(Student student, UUID id) {
        return new StudentDto(
                student.getId(),
                student.getUser().getFullName(),
                student.getUser().getPhoneNumber(),
                studentGroupRepository.findById(id).get()
        );
    }

    public ApiResponse getStudents(int page, int size) {
        try {
            Page<Student> all = studentRepository.findAll(PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeStudentDto).collect(Collectors.toList())
                    ));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getStudent(UUID id) {
        try {
            Optional<Student> byId = studentRepository.findById(id);
            if (byId.isPresent()) {
                return apiResponseService.getResponse(makeStudentDto(byId.get()));
            } else {
                return apiResponseService.notFoundResponse();
            }
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public StudentDto makeStudentDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getUser().getId(),
                student.getUser().getFullName(),
                student.getUser().getPhoneNumber(),
                student.getParentPhone(),
                student.getUser().getDescription(),
                student.getUser().getRegion(),
                student.getUser().getRegion() != null ? student.getUser().getRegion().getId() : null,
                student.getUser().getGender().toString(),
                student.getUser().getBirthDate() != null ? student.getUser().getBirthDate().toString() : "",
                student.getUser().getRoles(),
                student.getBalans(),
                student.getStudentGroup()
        );
    }

    public ApiResponse deleteStudent(UUID id) {
        try {
            Optional<Student> byId = studentRepository.findById(id);
            if (byId.isPresent()) {
                Student student = byId.get();
                if (student.getStudentGroup() != null && student.getStudentGroup().size() > 0) {
                    return apiResponseService.errorResponse();
                } else {
                    studentRepository.deleteById(id);
                    userRepository.deleteById(student.getId());
                    return apiResponseService.deleteResponse();
                }
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse saveStudentPayment(UUID id, StudentPaymentDto studentPaymentDto) {
        try {
            Optional<Student> byId = studentRepository.findById(id);
            if (byId.isPresent()) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                StudentPayment studentPayment = new StudentPayment();
                studentPayment.setStudent(studentPaymentDto.getStudentId() != null ? studentRepository.findById(studentPaymentDto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("get StudentId")) : null);
                studentPayment.setGroup(studentPaymentDto.getGroupId() != null ? groupRepository.findById(studentPaymentDto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("get Group")) : null);
                studentPayment.setPayType(studentPaymentDto.getPayTypeId() != null ? payTypeRepository.findById(studentPaymentDto.getPayTypeId()).orElseThrow(() -> new ResourceNotFoundException("get PayType")) : null);
                studentPayment.setSum(studentPaymentDto.getSum());
                studentPayment.setPayDate(studentPaymentDto.getPayDate() != null ? formatter1.parse(studentPaymentDto.getPayDate()) : null);
                studentPayment.setComment(studentPaymentDto.getComment());
                studentPaymentRepository.save(studentPayment);
                Student student = byId.get();
                Cashback byPrice = cashbackRepository.getByPrice(studentPaymentDto.getSum());
                studentPayment.setCashback(byPrice);
                if (byPrice != null) {
                    student.setBalans((byPrice.getPercent() * (studentPaymentDto.getSum() / 100)) + student.getBalans() + studentPaymentDto.getSum());
                    studentPayment.setCashSum(byPrice.getPercent() * (studentPaymentDto.getSum() / 100));
                } else {
                    student.setBalans(student.getBalans() + studentPaymentDto.getSum());
                }
                studentRepository.save(student);
                return apiResponseService.saveResponse();
            }
            return apiResponseService.notFoundResponse();

        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }

    }

    public ApiResponse editStudentPayment(UUID id, StudentPaymentDto studentPaymentDto) {
        try {
            Optional<StudentPayment> byId = studentPaymentRepository.findById(id);
            if (byId.isPresent()) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                StudentPayment studentPayment = byId.get();
                studentPayment.setStudent(studentPaymentDto.getStudentId() != null ? studentRepository.findById(studentPaymentDto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("get StudentId")) : null);
                studentPayment.setGroup(studentPaymentDto.getGroupId() != null ? groupRepository.findById(studentPaymentDto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("get Group")) : null);
                studentPayment.setPayType(studentPaymentDto.getPayTypeId() != null ? payTypeRepository.findById(studentPaymentDto.getPayTypeId()).orElseThrow(() -> new ResourceNotFoundException("get PayType")) : null);
                //// OLD OMOUNT
                double oldAmount = studentPayment.getSum();
                ////// NEW AMOUT
                double newAmount = studentPaymentDto.getSum();
                studentPayment.setSum(studentPaymentDto.getSum());
                studentPayment.setPayDate(studentPaymentDto.getPayDate() != null ? formatter1.parse(studentPaymentDto.getPayDate()) : null);
                studentPayment.setComment(studentPaymentDto.getComment());
                studentPaymentRepository.save(studentPayment);
                ////Balans uchun
                Optional<Student> byId1 = studentRepository.findById(studentPaymentDto.getStudentId());
                Cashback byPrice = cashbackRepository.getByPrice(studentPaymentDto.getSum());
                studentPayment.setCashback(byPrice);
                if (byId1.isPresent()) {
                    if (newAmount != oldAmount) {
                        Student student = byId1.get();
                        if (byPrice != null) {
                            student.setBalans((student.getBalans() - (oldAmount + studentPayment.getCashSum())) + (newAmount + (byPrice.getPercent() * newAmount / 100)));
                            studentPayment.setCashSum(byPrice.getPercent() * (newAmount / 100));
                        } else {
                            student.setBalans((student.getBalans() - (oldAmount + studentPayment.getCashSum())) + newAmount);
                            studentPayment.setCashSum(0.0);
                        }
                        studentRepository.save(student);
                        return apiResponseService.updatedResponse();
                    }
                }
                return apiResponseService.notFoundResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception exception) {
            return apiResponseService.tryErrorResponse();
        }
    }


    //
    public StudentPaymentDto makeStudentPaymentDto(StudentPayment studentPayment) {
        return new StudentPaymentDto(
                studentPayment.getId(),
                studentPayment.getPayType(),
                studentPayment.getStudent(),
                studentPayment.getCashback(),
                studentPayment.getCashSum(),
                studentPayment.getSum(),
                studentPayment.getPayDate() != null ? studentPayment.getPayDate().toString() : null,
                studentPayment.getComment(),
                studentPayment.getGroup()
        );
    }

    public StudentPaymentDto makeStudentPaymentCashbacks(StudentPayment studentPayment) {
            return new StudentPaymentDto(
                    studentPayment.getId(),
                    studentPayment.getPayType(),
                    studentPayment.getStudent(),
                    studentPayment.getCashback(),
                    studentPayment.getCashSum(),
                    studentPayment.getSum(),
                    studentPayment.getPayDate() != null ? studentPayment.getPayDate().toString() : null,
                    studentPayment.getComment(),
                    studentPayment.getGroup()
            );
    }

    public ApiResponse getStudentPaymentList(int page, int size) {
        try {
            Sort sort;
            Page<StudentPayment> all = studentPaymentRepository.findAll(PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeStudentPaymentDto).collect(Collectors.toList())
                    )
            );
        } catch (Exception exception) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getStudentPaymentListStudent(UUID id, int page, int size) {
        try {
            Sort sort;
            Page<StudentPayment> all = studentPaymentRepository.findAllByStudent_id(id, PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeStudentPaymentDto).collect(Collectors.toList())
                    )
            );
        } catch (Exception exception) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getStudentPayment(UUID id) {
        try {
            Optional<StudentPayment> optional = studentPaymentRepository.findById(id);
            if (optional.isPresent()) {
                return apiResponseService.getResponse(makeStudentPaymentDto(optional.get()));
            } else {
                return apiResponseService.notFoundResponse();
            }
        } catch (Exception exception) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse deleteStudentPayment(UUID id) {
        try {
            Optional<StudentPayment> studentOptional = studentPaymentRepository.findById(id);
            if (studentOptional.isPresent()) {
                StudentPayment studentPayment = studentOptional.get();
                studentPaymentRepository.deleteById(studentPayment.getId());
                Optional<Student> byId = studentRepository.findById(studentPayment.getStudent().getId());
                Student student = byId.get();
                if (studentPayment.getCashSum() != 0) {
                    student.setBalans(student.getBalans() - (studentPayment.getSum() + studentPayment.getCashSum()));
                } else {
                    student.setBalans(student.getBalans() - studentPayment.getSum());
                }
                studentRepository.save(student);
                return apiResponseService.deleteResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception exception) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse makeSituation(SituationDto situationDto) {
        try {
            Optional<Student> optional = studentRepository.findById(situationDto.getStudentId());
            if (optional.isPresent() && situationDto.getSituation() != null && situationDto.getSituation().length() > 3) {
                Student student = optional.get();
                for (StudentGroup studentGroup : student.getStudentGroup()) {
                    if (studentGroup.getGroup().getId().equals(situationDto.getGroupId())) {
                        if (situationDto.getSituation().equals("TRANSFER")) {
                            studentGroup.setStudentGroupStatus(StudentGroupStatus.valueOf(situationDto.getSituation()));
                            if (isHaveStudentGroup(student.getStudentGroup(), situationDto.getNewGroupId())) {
                                StudentGroup newStudentGroup = studentGroupRepository.save(new StudentGroup(
                                        groupRepository.findById(situationDto.getNewGroupId()).get(),
                                        StudentGroupStatus.TEST_LESSON,
                                        0,
                                        ""
                                ));
                                Set<StudentGroup> studentGroup1 = student.getStudentGroup();
                                studentGroup1.add(newStudentGroup);

                                student.setStudentGroup(studentGroup1);
                            } else {
                                return apiResponseService.existResponse();
                            }
                        } else {
                            studentGroup.setStudentGroupStatus(StudentGroupStatus.valueOf(situationDto.getSituation()));
                        }
                        studentRepository.save(student);
                        return apiResponseService.updatedResponse();
                    }
                }
                return apiResponseService.notFoundResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public boolean isHaveStudentGroup(Set<StudentGroup> studentGroupList, int groupId) {
        for (StudentGroup studentGroup : studentGroupList) {
            if (studentGroup.getGroup().getId() == groupId)
                return false;
        }
        return true;
    }

    /// StudentGroups for studentPayment
    public ApiResponse getStudentGroups(UUID id) {
        try {
            List<Group> studentGroupList = groupRepository.getStudentGroupList(id);
            List<ResSelect> resSelects = new ArrayList<>();
            for (Group group : studentGroupList) {
                ResSelect resSelect = new ResSelect();
                String key = ("[" + group.getName() + "] " + group.getCourse().getName() + " " + group.getTeacher().getUser().getFullName() + " " +
                        group.getStartTime() + " - " + group.getFinishTime());
                resSelect.setId(group.getId());
                resSelect.setName(key);
                resSelects.add(resSelect);
            }
            return apiResponseService.getResponse(resSelects);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }


    public ApiResponse getDebtorStudents(int page, int size) {
        Page<Student> all = studentRepository.getDebtorStudents(PageRequest.of(page, size));
        return apiResponseService.getResponse(
                new PageableDto(
                        all.getTotalPages(),
                        all.getTotalElements(),
                        all.getNumber(),
                        all.getSize(),
                        all.get().map(this::makeStudentDtoForDeptors).collect(Collectors.toList())
                ));
    }

    public StudentDto makeStudentDtoForDeptors(Student student) {
        if (student.getBalans() < 0) {
            return new StudentDto(
                    student.getId(),
                    student.getUser().getId(),
                    student.getBalans(),
                    student.getUser().getFullName(),
                    student.getUser().getPhoneNumber(),
                    student.getParentPhone(),
                    student.getUser().getRegion(),
                    student.getUser().getRegion() != null ? student.getUser().getRegion().getId() : null,
                    student.getStudentGroup()
            );
        }
        return null;
    }

    public ApiResponse getStudentPaymentCashbacks(int page, int size) {
        try {
            Sort sort;
            Page<StudentPayment> all = studentPaymentRepository.getStudentPaymentByCashback(PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeStudentPaymentCashbacks).collect(Collectors.toList())
                    )
            );
        } catch (Exception exception) {
            return apiResponseService.tryErrorResponse();
        }
    }


}
