package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.*;
import uz.gvs.admin_crm.entity.enums.ClientStatusEnum;
import uz.gvs.admin_crm.entity.enums.Gender;
import uz.gvs.admin_crm.payload.*;
import uz.gvs.admin_crm.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppealService {
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    ReklamaRepository reklamaRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientStatusRepository clientStatusRepository;
    @Autowired
    ClientAppealRepository clientAppealRepository;
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    ToplamRepository toplamRepository;
    @Autowired
    ClientStatusConnectRepository clientStatusConnectRepository;

    public ApiResponse saveAppeal(AppealDto appealDto) {
        try {
            if (appealDto.getPhoneNumber() == null || appealDto.getFullName() == null || appealDto.getPhoneNumber().length() != 9
                    || appealDto.getFullName().length() < 2)
                return apiResponseService.notFoundResponse();
            // clientni saqlash
            Client client = new Client();
            client.setFullName(appealDto.getFullName());
            client.setPhoneNumber(appealDto.getPhoneNumber());
            if (appealDto.getAge() != null)
                client.setAge(appealDto.getAge());
            client.setGender(Gender.valueOf(appealDto.getGender()));
            client.setDescription(appealDto.getDescription());

            if (appealDto.getRegionId() != null && appealDto.getRegionId() > 0) {
                Region region = regionRepository.findById(appealDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("get region"));
                client.setRegion(region);
            }
            if (appealDto.getReklamaId() != null && appealDto.getReklamaId() > 0) {
                Reklama reklama = reklamaRepository.findById(appealDto.getReklamaId()).orElseThrow(() -> new ResourceNotFoundException("get Reklama"));
                client.setReklama(reklama);
            }
            ClientStatus clientStatus = clientStatusRepository.findById(appealDto.getClientStatusId()).orElseThrow(() -> new ResourceNotFoundException("get client status"));
            Client savedClient = clientRepository.save(client);

            // clientning murojaat turini qo'shish
            ClientStatusConnect clientStatusConnect = new ClientStatusConnect();
            clientStatusConnect.setClient(savedClient);
            if (appealDto.getStatusEnum().equals("COLLECTION")) {
                toplamRepository.findById(appealDto.getClientStatusId()).orElseThrow(() -> new ResourceNotFoundException("get toplam"));
                clientStatusConnect.setStatusId(appealDto.getClientStatusId().toString());
                clientStatusConnect.setToplam(true);
            } else {
                clientStatusConnect.setToplam(false);
                clientStatusConnect.setStatusId(appealDto.getClientStatusId().toString());
            }
            clientStatusConnect.setClient(savedClient);
            ClientStatusConnect saveClientStatusConnect = clientStatusConnectRepository.save(clientStatusConnect);

            // statistika murojaat voronkasi uchun murojaatni saqlab olish
            ClientAppeal clientAppeal = new ClientAppeal();
            clientAppeal.setClient(savedClient);
            clientAppeal.setStatusEnum(clientStatus.getClientStatusEnum());
            clientAppeal.setStatusId(saveClientStatusConnect.getStatusId());
            clientAppealRepository.save(clientAppeal);

            return apiResponseService.saveResponse();
        } catch (Exception e) {
            return apiResponseService.errorResponse();
        }
    }

    public ApiResponse changeStatus(UUID id, AppealDto appealDto) {
        try {
            Optional<Client> byId = clientRepository.findById(id);
            if (!byId.isPresent())
                return apiResponseService.notFoundResponse();
            // clientni saqlash
            Client client = byId.get();
            ClientStatus clientStatus = clientStatusRepository.findById(appealDto.getClientStatusId()).orElseThrow(() -> new ResourceNotFoundException("get client status"));

            Optional<ClientStatusConnect> byClient_id = clientStatusConnectRepository.findByClient_id(client.getId());

            ClientStatusConnect clientStatusConnect = byClient_id.get();
            if (appealDto.getStatusEnum().equals("COLLECTION")) {
                toplamRepository.findById(appealDto.getClientStatusId()).orElseThrow(() -> new ResourceNotFoundException("get toplam"));
                clientStatusConnect.setStatusId(appealDto.getClientStatusId().toString());
                clientStatusConnect.setToplam(true);
            } else {
                clientStatusConnect.setToplam(false);
                clientStatusConnect.setStatusId(appealDto.getClientStatusId().toString());
            }
            ClientStatusConnect saveClientStatusConnect = clientStatusConnectRepository.save(clientStatusConnect);

            // statistika murojaat voronkasi uchun murojaatni saqlab olish
            ClientAppeal clientAppeal = null;
            Optional<ClientAppeal> optionalClientAppeal = clientAppealRepository.findByClient_idAndStatusEnum(client.getId(),
                    ClientStatusEnum.valueOf(appealDto.getStatusEnum()));
            if (optionalClientAppeal.isPresent()) {
                clientAppeal = optionalClientAppeal.get();
                clientAppeal.setStatusId(saveClientStatusConnect.getStatusId());
            } else {
                clientAppeal = new ClientAppeal();
                clientAppeal.setClient(client);
                if (appealDto.getStatusEnum().equals("COLLECTION")) {
                    clientAppeal.setStatusEnum(ClientStatusEnum.COLLECTION);
                } else {
                    clientAppeal.setStatusEnum(clientStatus.getClientStatusEnum());
                }
                clientAppeal.setStatusId(saveClientStatusConnect.getStatusId());
            }
            clientAppealRepository.save(clientAppeal);
            return apiResponseService.updatedResponse();
        } catch (Exception e) {
            return apiResponseService.errorResponse();
        }
    }

    /*
     enumType - murojaatning qaysi bo'limga tegishli ekanligi, So'rov = REQUEST
     typeId  - murojaatning qaysi bo'limning qaysi qismiga tegishli ekanligi, enumType turiga qarab
     UUID yoki Integer bo'lishi mumkin,
    */
    public ApiResponse getClientList(String enumType, Integer typeId, int page, int size) {
        try {
            ClientStatusEnum clientStatusEnum = ClientStatusEnum.valueOf(enumType);
            List<Object> object = new ArrayList<>();
            List<AppealDto> clientList = new ArrayList<>();
            Integer totalItems = 0;

            if (typeId == 0) {
                if (clientStatusEnum.equals(ClientStatusEnum.COLLECTION)) {
                    object = clientRepository.getClientByFilterEnumSet(page, size);
                    totalItems = clientRepository.getCountByEnumSet();
                } else {
                    object = clientRepository.getClientByFilterEnumType(enumType, page, size);
                    totalItems = clientRepository.getCountByEnumType(enumType);
                }
            } else {
                if (clientStatusEnum.equals(ClientStatusEnum.COLLECTION)) {
                    object = clientRepository.getClientByFilterToplamStatus(typeId, page, size);
                    totalItems = clientRepository.getCountByStatusToplam(typeId);
                } else {
                    object = clientRepository.getClientByFilterStatus(enumType, typeId, page, size);
                    totalItems = clientRepository.getCountByStatusType(enumType, typeId);
                }
            }
            for (Object obj : object) {
                Object[] client = (Object[]) obj;
                UUID id = UUID.fromString(client[0].toString());
                String fullName = client[1].toString();
                String phoneNumber = client[2].toString();
                Integer statusId = Integer.valueOf(client[3].toString());
                String statusName = client[4].toString();
                String statusEnum = "";
                if (clientStatusEnum.equals(ClientStatusEnum.COLLECTION)) {
                    statusEnum = "COLLECTION";
                } else {
                    statusEnum = client[5].toString();
                }
                AppealDto appealDto = new AppealDto(id, fullName, phoneNumber, statusName, statusEnum, statusId);
                clientList.add(appealDto);
            }
            return apiResponseService.getResponse(new PageableDto(Long.valueOf(totalItems.toString()), page, size, clientList));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }


    public ApiResponse getOneAppeal(UUID id) {
        try {
            Optional<Client> byId = clientRepository.findById(id);
            Optional<ClientStatusConnect> byClient_id = clientStatusConnectRepository.findByClient_id(id);
            if (byId.isPresent() && byClient_id.isPresent()) {
                List<Object> object = clientAppealRepository.getClientAppealList(id);
                List<ClientAppealDto> clientAppealDtos = new ArrayList<>();
                for (Object obj : object) {
                    Object[] client = (Object[]) obj;
                    UUID clientId = UUID.fromString(client[0].toString());
                    String statusEnum = (client[1].toString().equals("WAITING") ? "Kutish" : "So'rov");
                    String statusName = client[2].toString();
                    Integer statusId = Integer.valueOf(client[3].toString());
                    String updateTime = client[4].toString();
                    UUID caId = UUID.fromString(client[5].toString());
                    clientAppealDtos.add(
                            new ClientAppealDto(
                                    caId,
                                    statusEnum,
                                    clientId,
                                    statusId,
                                    updateTime,
                                    statusName));
                }
                object = clientAppealRepository.getClientAppealListByToplam(id);
                for (Object obj : object) {
                    Object[] client = (Object[]) obj;
                    UUID clientId = UUID.fromString(client[0].toString());
                    String statusEnum = "To'plam";
                    String statusName = client[2].toString();
                    Integer statusId = Integer.valueOf(client[3].toString());
                    String updateTime = client[4].toString();
                    UUID caId = UUID.fromString(client[5].toString());
                    clientAppealDtos.add(
                            new ClientAppealDto(
                                    caId,
                                    statusEnum,
                                    clientId,
                                    statusId,
                                    updateTime,
                                    statusName));
                }

                return apiResponseService.getResponse(
                        new ClientDto(
                                byClient_id.get(),
                                clientAppealDtos
                        ));
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse deleteAppeal(UUID id) {
        try {
            return apiResponseService.deleteResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
