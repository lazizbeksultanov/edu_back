package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Room;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.RoomDto;
import uz.gvs.admin_crm.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ApiResponseService apiResponseService;

    public ApiResponse save(RoomDto roomDto) {
        try {
            if (!(roomDto.getName().replaceAll(" ", "").length() > 1))
                return apiResponseService.notEnoughErrorResponse();
            Room room = new Room();
            // edit bo'lsa
            if (roomDto.getId() != null) {
                if (roomRepository.existsByNameEqualsIgnoreCaseAndIdNot(roomDto.getName(), roomDto.getId()))
                    return apiResponseService.existResponse();
                room = roomRepository.findById(roomDto.getId()).orElseThrow(() -> new ResourceNotFoundException("get room"));
            } else {
                // yangi qo'shilsa bo'lsa
                if (roomRepository.existsByNameEqualsIgnoreCase(roomDto.getName()))
                    return apiResponseService.existResponse();
            }
            room.setName(roomDto.getName());
            room.setActive(roomDto.isActive());
            roomRepository.save(room);
            return apiResponseService.saveResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }

    }

    public ApiResponse getRoomList() {
        try {
            List<Room> allRooms = roomRepository.findAll();
            if (allRooms.isEmpty()) return apiResponseService.notFoundResponse();
            return apiResponseService.getResponse(allRooms);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getRoom(Integer id) {
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isEmpty()) return apiResponseService.notFoundResponse();
            return apiResponseService.getResponse(room);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse deleteRoom(Integer id) {
        try{
            if (roomRepository.findById(id).isEmpty()) return apiResponseService.notFoundResponse();
            roomRepository.deleteById(id);
            return apiResponseService.deleteResponse();
        }catch (Exception e){
            return apiResponseService.tryErrorResponse();
        }
    }

}
