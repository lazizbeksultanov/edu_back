package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.ClientStatus;
import uz.gvs.admin_crm.entity.enums.ClientStatusEnum;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ClientStatusDto;
import uz.gvs.admin_crm.repository.ClientStatusRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientStatusService {
    @Autowired
    ClientStatusRepository clientStatusRepository;
    @Autowired
    ApiResponseService apiResponseService;

    public ApiResponse saveClientStatus(ClientStatusDto clientStatusDto) {
        try {
            boolean exists = clientStatusRepository.existsByNameEqualsIgnoreCaseAndClientStatusEnum(clientStatusDto.getName(), ClientStatusEnum.valueOf(clientStatusDto.getClientStatusEnum()));
            if (exists)
                return apiResponseService.existResponse();
            ClientStatus clientStatus = new ClientStatus();
            clientStatus.setName(clientStatusDto.getName());
            clientStatus.setClientStatusEnum(ClientStatusEnum.valueOf(clientStatusDto.getClientStatusEnum()));
            clientStatus.setActive(clientStatusDto.isActive());
            clientStatusRepository.save(clientStatus);
            return apiResponseService.saveResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editClientStatus(Integer id, ClientStatusDto clientStatusDto) {
        try {
            boolean exists = clientStatusRepository.existsByNameEqualsIgnoreCaseAndClientStatusEnumAndIdNot(clientStatusDto.getName(), ClientStatusEnum.valueOf(clientStatusDto.getClientStatusEnum()), id);
            if (exists)
                return apiResponseService.existResponse();
            ClientStatus clientStatus = clientStatusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("get client status"));
            clientStatus.setName(clientStatusDto.getName());
            clientStatus.setClientStatusEnum(ClientStatusEnum.valueOf(clientStatusDto.getClientStatusEnum()));
            clientStatus.setActive(clientStatusDto.isActive());
            clientStatusRepository.save(clientStatus);
            return apiResponseService.updatedResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getClientStatus(String type) {
        try {
            List<ClientStatus> clientStatusList = new ArrayList<>();
            if (type.equals("all")) {
                clientStatusList = clientStatusRepository.findAll();
            } else {
                clientStatusList = clientStatusRepository.findAllByClientStatusEnum(ClientStatusEnum.valueOf(type));
            }
            return apiResponseService.getResponse(clientStatusList);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse deleteClientStatus(Integer id) {
        try {
            clientStatusRepository.deleteById(id);
            return apiResponseService.deleteResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
