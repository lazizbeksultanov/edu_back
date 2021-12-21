package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Toplam;
import uz.gvs.admin_crm.entity.Weekday;
import uz.gvs.admin_crm.entity.enums.WeekdayName;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ClientDto;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.payload.ToplamDto;
import uz.gvs.admin_crm.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ToplamService {
    @Autowired
    ToplamRepository toplamRepository;
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    WeekdayRepository weekdayRepository;

    public ApiResponse saveToplam(ToplamDto toplamDto) {
        try {
            if (toplamDto.getName().isEmpty() || toplamDto.getCourseId() == null || toplamDto.getTeacherId() == null
                    || toplamDto.getWeekdays().isEmpty() || toplamDto.getTime().isEmpty())
                return apiResponseService.notEnoughErrorResponse();
            Toplam toplam = new Toplam();
            toplam.setActive(toplamDto.isActive());
            toplam.setName(toplamDto.getName());
            toplam.setTime(toplamDto.getTime());
            toplam.setTeacher(teacherRepository.findById(toplamDto.getTeacherId()).orElseThrow(() -> new ResourceNotFoundException("get teacher")));
            toplam.setCourse(courseRepository.findById(toplamDto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("get course")));
            Set<Weekday> weekdayNameSet = new HashSet<>();
            for (String weekday : toplamDto.getWeekdays()) {
                Optional<Weekday> byWeekday = weekdayRepository.findByWeekdayName(WeekdayName.valueOf(weekday));
                weekdayNameSet.add(byWeekday.get());
            }
            toplam.setWeekdays(weekdayNameSet);
            toplamRepository.save(toplam);
            return apiResponseService.saveResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editToplam(Integer id, ToplamDto toplamDto) {
        try {
            Optional<Toplam> byId = toplamRepository.findById(id);
            if (!byId.isPresent())
                return apiResponseService.notFoundResponse();

            Toplam toplam = byId.get();
            toplam.setActive(toplamDto.isActive());
            toplam.setName(toplamDto.getName());
            toplam.setName(toplamDto.getTime());
            toplam.setTeacher(teacherRepository.findById(toplamDto.getTeacherId()).orElseThrow(() -> new ResourceNotFoundException("get teacher")));
            toplam.setCourse(courseRepository.findById(toplamDto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("get course")));
            Set<Weekday> weekdayNameSet = new HashSet<>();
            for (String weekday : toplamDto.getWeekdays()) {
                Optional<Weekday> byWeekday = weekdayRepository.findByWeekdayName(WeekdayName.valueOf(weekday));
                weekdayNameSet.add(byWeekday.get());
            }
            toplam.setWeekdays(weekdayNameSet);
            toplamRepository.save(toplam);
            return apiResponseService.updatedResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getOneToplam(Integer id) {
        try {
            List<Object> object = toplamRepository.getClientListByToplam(id);
            List<ClientDto> clientDtos = new ArrayList<>();
            Toplam getToplam = toplamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("get toplam"));
            ToplamDto toplamDto = makeToplamDto(getToplam);
            for (Object obj : object) {
                Object[] client = (Object[]) obj;
                UUID clientId = UUID.fromString(client[0].toString());
                String fullName = client[1].toString();
                String phoneNumber = client[2].toString();
                String vaqt = client[3].toString();
                clientDtos.add(new ClientDto(
                        clientId,
                        fullName,
                        phoneNumber,
                        vaqt
                ));
            }

            toplamDto.setClientDtos(clientDtos);
            toplamDto.setSoni(clientDtos.size());
            return apiResponseService.getResponse(toplamDto);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getToplamList(int page, int size) {
        try {
            Page<Toplam> all = toplamRepository.findAll(PageRequest.of(page, size));
            List<ToplamDto> collect = all.getContent().stream().map(this::makeToplamDto).collect(Collectors.toList());
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            collect
                    ));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ToplamDto makeToplamDto(Toplam toplam) {
        Set<String> stringSet = new HashSet<>();
        for (Weekday weekday : toplam.getWeekdays()) {
            stringSet.add(weekday.getWeekdayName().name);
        }
        return new ToplamDto(
                toplam.getId(),
                toplam.getName(),
                toplam.getCourse().getId(),
                toplam.getTeacher().getId(),
                toplam.getTeacher().getUser().getFullName(),
                stringSet,
                toplam.getTime(),
                toplam.isActive(),
                toplam.getCourse().getName()
        );
    }

    public ApiResponse getToplamListForSelect() {
        try {
            List<Toplam> allByActive = toplamRepository.findAllByActive(true);
            List<Object> object = toplamRepository.getToplamCount();
            List<ToplamDto> toplamDtos = new ArrayList<>();
            for (Object obj : object) {
                Object[] client = (Object[]) obj;
                Integer id = Integer.valueOf(client[0].toString());
                Integer soni = Integer.valueOf(client[1].toString());
                for (Toplam toplam : allByActive) {
                    if (toplam.getId().equals(id)) {
                        toplamDtos.add(new ToplamDto(
                                id, toplam.getName(),
                                toplam.getCourse().getId(),
                                toplam.getTeacher().getId(),
                                toplam.getTeacher().getUser().getFullName(),
                                makeWeekdayName(toplam.getWeekdays()),
                                toplam.getTime(),
                                toplam.isActive(),
                                toplam.getCourse().getName(),
                                soni
                        ));
                    }
                }
            }
            return apiResponseService.getResponse(toplamDtos);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public Set<String> makeWeekdayName(Set<Weekday> weekdays) {
        Set<String> stringSet = new HashSet<>();
        for (Weekday weekday : weekdays) {
            stringSet.add(weekday.getWeekdayName().name);
        }
        return stringSet;
    }

    public ApiResponse deleteToplam(Integer id) {
        try {
            toplamRepository.deleteById(id);
            return apiResponseService.deleteResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
