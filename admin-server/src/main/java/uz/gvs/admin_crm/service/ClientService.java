package uz.gvs.admin_crm.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Client;
import uz.gvs.admin_crm.entity.enums.Gender;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ClientDto;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.repository.ClientRepository;
import uz.gvs.admin_crm.repository.RegionRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RegionRepository regionRepository;

    public ApiResponse addClient(ClientDto clientDto) {
        try {
//                if (!clientRepository.existsByPhoneNumber(clientDto.getPhoneNumber())) {
//                    Client client = new Client(
//                            clientDto.getFullName(),
//                            clientDto.getPhoneNumber(),
//                            clientDto.getDescription(),
//                            clientDto.getAge(),
//                            (clientDto.getRegionId() != null ? regionRepository.findById(clientDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("Get region")) : null),
//                            Gender.valueOf(clientDto.getGender())
//                    );
//                    clientRepository.save(client);
//                    return apiResponseService.saveResponse();
//            }
            return apiResponseService.existResponse();
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editClient(UUID id, ClientDto clientDto) {
        try {
            Optional<Client> optionalClient = clientRepository.findById(id);
            if (optionalClient.isEmpty()) {
                return apiResponseService.notFoundResponse();
            }
            if (clientRepository.existsByFullNameEqualsIgnoreCaseAndIdNot(clientDto.getFullName(), id)) {
                return apiResponseService.existResponse();
            }
            Client client = optionalClient.get();
            client.setFullName(clientDto.getFullName());
            client.setPhoneNumber(clientDto.getPhoneNumber());
            client.setAge(clientDto.getAge());
            client.setDescription(clientDto.getDescription());
            client.setRegion(regionRepository.findById(clientDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("get Region")));
            client.setGender(Gender.valueOf(clientDto.getGender()));
            clientRepository.save(client);
            return apiResponseService.updatedResponse();
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }


    public ApiResponse getClient(UUID id) {
        try {
            Optional<Client> optionalClient = clientRepository.findById(id);
            if (optionalClient.isEmpty())
                return apiResponseService.notFoundResponse();
            return apiResponseService.getResponse(makeClient(optionalClient.get()));

        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ClientDto makeClient(Client client) {
        return new ClientDto(
                client.getId(),
                client.getFullName(),
                client.getPhoneNumber(),
                client.getDescription(),
                client.getAge(),
                client.getRegion() != null ? client.getRegion().getId() : null,
                client.getRegion(),
                client.getGender().toString()
        );
    }

    public ApiResponse getClientList(int page, int size) {
        try {
            Page<Client> all = clientRepository.findAll(PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeClient).collect(Collectors.toList())
                    )
            );
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse deleteClient(UUID id) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(id);
            if (clientOptional.isEmpty())
                return apiResponseService.notFoundResponse();

            clientRepository.deleteById(id);
            return apiResponseService.deleteResponse();
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
