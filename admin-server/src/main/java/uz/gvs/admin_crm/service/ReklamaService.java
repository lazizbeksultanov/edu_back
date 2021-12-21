package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Reklama;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.payload.ReklamaDto;
import uz.gvs.admin_crm.payload.ResSelect;
import uz.gvs.admin_crm.repository.ReklamaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReklamaService {
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    ReklamaRepository reklamaRepository;

    public ApiResponse addReklama(ReklamaDto reklamaDto) {
        try {
            if (!reklamaRepository.existsByNameEqualsIgnoreCase(reklamaDto.getName())) {
                Reklama reklama = new Reklama();
                reklama.setId(reklamaDto.getId());
                reklama.setName(reklamaDto.getName());
                reklama.setDescription(reklamaDto.getDescription());
                reklama.setActive(reklamaDto.isActive());
                reklamaRepository.save(reklama);
                return apiResponseService.saveResponse();
            }
            return apiResponseService.existResponse();
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editReklama(ReklamaDto reklamaDto, Integer id) {
        try {
            Optional<Reklama> optionalReklama = reklamaRepository.findById(id);
            if (optionalReklama.isPresent()) {
                Reklama reklama = optionalReklama.get();
                reklama.setName(reklamaDto.getName());
                reklama.setDescription(reklamaDto.getDescription());
                reklama.setActive(reklamaDto.isActive());
                reklamaRepository.save(reklama);
                return apiResponseService.updatedResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getReklamaListForSelect() {
        try {
            List<Reklama> allByActiveIsTrue = reklamaRepository.findAllByActiveIsTrue();
            List<ResSelect> resSelects = new ArrayList<>();
            for (Reklama reklama : allByActiveIsTrue) {
                resSelects.add(new ResSelect(reklama.getId(), reklama.getName()));
            }
            return apiResponseService.getResponse(resSelects);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getReklamaList(int page, int size, User user) {
        try {
            Page<Reklama> all = null;
            all = reklamaRepository.findAll(PageRequest.of(page, size));
            return apiResponseService.getResponse(new PageableDto(
                    all.getTotalPages(),
                    all.getTotalElements(),
                    all.getNumber(),
                    all.getSize(),
                    all.get().map(this::makeReklama).collect(Collectors.toList())
            ));
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ReklamaDto makeReklama(Reklama reklama) {
        return new ReklamaDto(
                reklama.getId(),
                reklama.getName(),
                reklama.getDescription(),
                reklama.isActive()
        );
    }

    public ApiResponse deleteReklama(Integer id) {
        try {
            Optional<Reklama> optional = reklamaRepository.findById(id);
            if (optional.isPresent())
                reklamaRepository.deleteById(id);
            apiResponseService.deleteResponse();
            return apiResponseService.deleteResponse();
        } catch (Exception a) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
