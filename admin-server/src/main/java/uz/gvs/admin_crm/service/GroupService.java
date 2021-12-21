package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.*;
import uz.gvs.admin_crm.entity.enums.GroupStatus;
import uz.gvs.admin_crm.entity.enums.StudentGroupStatus;
import uz.gvs.admin_crm.entity.enums.WeekdayName;
import uz.gvs.admin_crm.payload.*;
import uz.gvs.admin_crm.repository.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    WeekdayRepository weekdayRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StudentGroupRepository studentGroupRepository;

    public Group makeGroup(GroupDto groupDto) {
        try {
            Group group = new Group();
            group.setName(groupDto.getName());
            group.setDescription(groupDto.getDescription());
            group.setActive(groupDto.isActive());
            group.setTeacher(teacherRepository.findById(groupDto.getTeacherId()).orElseThrow(() -> new ResourceNotFoundException("get teacher")));
            group.setStartTime(groupDto.getStartTime());
            group.setFinishTime(groupDto.getFinishTime());
            group.setCourse(courseRepository.findById(groupDto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("get course")));
            group.setRoom(roomRepository.findById(groupDto.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("get room")));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            group.setStartDate(groupDto.getStartDate() != null ? formatter.parse(groupDto.getStartDate()) : null);
            group.setFinishDate(groupDto.getFinishDate() != null ? formatter.parse(groupDto.getFinishDate()) : null);
            group.setGroupStatus(GroupStatus.ACTIVE);

            Set<Weekday> weekdayNameSet = new HashSet<>();
            for (String weekday : groupDto.getWeekdays()) {
                Optional<Weekday> byWeekday = weekdayRepository.findByWeekdayName(WeekdayName.valueOf(weekday));
                weekdayNameSet.add(byWeekday.get());
            }
            group.setWeekdays(weekdayNameSet);
            return (groupRepository.save(group));

        } catch (Exception e) {
            return null;
        }
    }

    public GroupDto makeGroupDto(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(group.getId());
        groupDto.setName(group.getName());
        groupDto.setDescription(group.getDescription());
        groupDto.setActive(group.isActive());
        groupDto.setTeacher(group.getTeacher());
        groupDto.setStartTime(group.getStartTime());
        groupDto.setFinishTime(group.getFinishTime());
        groupDto.setCourse(group.getCourse());
        groupDto.setRoom(group.getRoom());
        groupDto.setStartDate(group.getStartDate().toString());
        groupDto.setFinishDate(group.getFinishDate().toString());
        Set<String> stringSet = new HashSet<>();
        for (Weekday weekday : group.getWeekdays()) {
            stringSet.add(weekday.getWeekdayName().name);
        }
        groupDto.setWeekdays(stringSet);
        return groupDto;
    }

    public Object makeGroupTable(Group group) {
        try {
            List<String> stringSet = new ArrayList<>();
            for (Weekday weekday : group.getWeekdays()) {
                stringSet.add(weekday.getWeekdayName().name);
            }
            return new ResGroupDto(
                    group.getId(),
                    group.getName(),
                    group.getCourse().getName(),
                    group.getTeacher().getId(),
                    group.getTeacher().getUser().getFullName(),
                    group.getStartTime(),
                    group.getFinishTime(),
                    stringSet,
                    group.getStartDate(),
                    group.getFinishDate(),
                    group.getGroupStatus(),
                    group.isActive()
            );
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse saveGroup(GroupDto groupDto) {
        if (!groupRepository.existsByNameEqualsIgnoreCaseAndCourseId(groupDto.getName(), groupDto.getCourseId())) {
            if (groupDto.getName() != null || groupDto.getCourseId() != null || groupDto.getTeacherId() != null) {
                if (makeGroup(groupDto) != null) {
                    return apiResponseService.saveResponse();
                }
                return apiResponseService.errorResponse();
            }
            return apiResponseService.notEnoughErrorResponse();
        }
        return apiResponseService.existResponse();
    }

    public ApiResponse getOneGroup(Integer id) {
        try {
            Optional<Group> group = groupRepository.findById(id);
            if (!group.isPresent()) {
                return apiResponseService.notFoundResponse();
            }
            return apiResponseService.getResponse(makeGroupDto(group.get()));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse addStudentForGroup(AddGroupDto addGroupDto) {
        try {
            Optional<Group> byId = groupRepository.findById(addGroupDto.getGroupId());
            Optional<Student> byId1 = studentRepository.findById(addGroupDto.getStudentId());
            if (byId.isPresent() && byId1.isPresent()) {
                Student student = byId1.get();
                Group group = byId.get();
                Set<StudentGroup> studentGroup = student.getStudentGroup();
                if (studentGroup != null && studentGroup.size() > 0) {
                    for (StudentGroup studentGroup1 : studentGroup) {
                        if (studentGroup1.getGroup().getId().equals(group.getId()))
                            return apiResponseService.existResponse();
                    }
                }
                StudentGroup newStudentGroup = new StudentGroup();
                newStudentGroup.setGroup(group);
                newStudentGroup.setStudentGroupStatus(StudentGroupStatus.TEST_LESSON);
                studentGroupRepository.save(newStudentGroup);
                studentGroup.add(newStudentGroup);
                student.setStudentGroup(studentGroup);
                studentRepository.save(student);
                return apiResponseService.saveResponse();
            } else
                return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getGroupsForSelect() {
        try {

            List<Group> all = groupRepository.findAll();
            List<ResSelect> resSelects = new ArrayList<>();
            for (Group group : all) {
                ResSelect resSelect = new ResSelect();
                resSelect.setId(group.getId());
                resSelect.setName("[" + group.getName() + "]" + group.getCourse().getName() + "  (" + group.getTeacher().getUser().getFullName() + " - " +
                        group.getStartTime() + ") ");
                resSelects.add(resSelect);
            }
            return apiResponseService.getResponse(resSelects);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }

    }

    public ApiResponse getGroupList(int page, int size) {
        try {
            Page<Group> all = null;
            all = groupRepository.findAll(PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(), all.stream().map(this::makeGroupTable).collect(Collectors.toList())));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editGroup(GroupDto groupDto, Integer id) {
        try {
            Optional<Group> optional = groupRepository.findById(id);
            Group group = optional.get();
            if (!groupRepository.existsByNameEqualsIgnoreCaseAndCourseIdAndIdNot(groupDto.getName(), groupDto.getCourseId(), id)) {
                if (groupDto.getName() != null || groupDto.getCourseId() != null || groupDto.getTeacherId() != null) {
                    group.setName(groupDto.getName());
                    group.setDescription(groupDto.getDescription());
                    group.setActive(groupDto.isActive());
                    group.setTeacher(teacherRepository.findById(groupDto.getTeacherId()).orElseThrow(() -> new ResourceNotFoundException("get teacher")));
                    group.setStartTime(groupDto.getStartTime());
                    group.setFinishTime(groupDto.getFinishTime());
                    group.setCourse(courseRepository.findById(groupDto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("get course")));
                    group.setRoom(roomRepository.findById(groupDto.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("get room")));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    group.setStartDate(groupDto.getStartDate() != null ? formatter.parse(groupDto.getStartDate()) : null);
                    group.setFinishDate(groupDto.getFinishDate() != null ? formatter.parse(groupDto.getFinishDate()) : null);

                    Set<Weekday> weekdayNameSet = new HashSet<>();
                    for (String weekday : groupDto.getWeekdays()) {
                        Optional<Weekday> byWeekday = weekdayRepository.findByWeekdayName(WeekdayName.valueOf(weekday));
                        weekdayNameSet.add(byWeekday.get());
                    }
                    group.setWeekdays(weekdayNameSet);
                    groupRepository.save(group);
                    return apiResponseService.updatedResponse();
                }
                return apiResponseService.notEnoughErrorResponse();
            }
            return apiResponseService.existResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse changeToArchiveStatus(GroupDto groupDto) {
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.setActive(!group.isActive());
            group.setGroupStatus(GroupStatus.ARCHIVE);
            groupRepository.save(group);
            return apiResponseService.updatedResponse();
        } else {
            return apiResponseService.notFoundResponse();
        }
    }

    public ApiResponse changeToActiveStatus(GroupDto groupDto) {
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.setActive(!group.isActive());
            group.setGroupStatus(GroupStatus.ACTIVE);
            groupRepository.save(group);
            return apiResponseService.updatedResponse();
        } else {
            return apiResponseService.notFoundResponse();
        }
    }
}
