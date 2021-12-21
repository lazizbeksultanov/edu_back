package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Region;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.payload.RegionDto;
import uz.gvs.admin_crm.repository.RegionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class RegionService {
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    ApiResponseService apiResponseService;

    @Autowired
    CheckRole checkRole;

    public ApiResponse saveRegion(RegionDto regionDto) {
        if (regionDto.getRegionId() != null) {
            Region parentRegion = regionRepository.findById(regionDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("get Region"));
            boolean exists = regionRepository.existsByNameEqualsIgnoreCaseAndRegionId(regionDto.getName(), parentRegion.getId());
            if (exists) {
                return apiResponseService.existResponse();
            }
            makeRegion(regionDto, parentRegion);
            return apiResponseService.saveResponse();
        }
        if (regionRepository.existsByNameEqualsIgnoreCaseAndRegion(regionDto.getName(), null)) {
            return apiResponseService.existResponse();
        }
        makeRegion(regionDto, null);
        return apiResponseService.saveResponse();
    }


    public ApiResponse editRegion(RegionDto regionDto, Integer id) {
        try {
            Optional<Region> optionalRegion = regionRepository.findById(id);
            if (optionalRegion.isPresent()) {
                Region region = optionalRegion.get();
                if (regionDto.getRegionId() != null) {
                    if (region.getRegion() != null && !regionDto.getRegionId().equals(region.getRegion().getId())) {
                        if (regionRepository.existsByNameEqualsIgnoreCaseAndIdNotAndRegionId(regionDto.getName(), id, regionDto.getRegionId()))
                            return apiResponseService.existResponse();
                    }
                    region.setRegion(regionRepository.findById(regionDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("get region")));
                    if (id.equals(regionDto.getRegionId())) {
                        return apiResponseService.errorResponse();
                    }
                } else {
                    if (regionRepository.existsByNameEqualsIgnoreCaseAndRegionAndIdNot(regionDto.getName(), null, id))
                        return apiResponseService.existResponse();
                    region.setRegion(null);
                }
                region.setName(regionDto.getName());
                region.setDescription(regionDto.getDescription());
                region.setActive(regionDto.isActive());
                regionRepository.save(region);
                return apiResponseService.updatedResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }

    }


    public ApiResponse getSearchRegion(String key) {
        try {
            if (key.length() > 3) {
                return apiResponseService.errorResponse();
            }
            List<Region> regionList = regionRepository.getSearchRegionList(key.toLowerCase());
            List<RegionDto> collect = regionList.stream().map(this::makeRegionForGet).collect(Collectors.toList());
            return apiResponseService.getResponse(collect);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getOneRegion(Integer id) {
        try {
            Optional<Region> byId = regionRepository.findById(id);
            if (!byId.isPresent()) {
                return apiResponseService.notFoundResponse();
            }
            return apiResponseService.getResponse(makeRegionForGet(byId.get()));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getListRegion(int page, int size, User user) {
        try {
            Page<Region> all = null;
            if (checkRole.isAdmin(user) || checkRole.isSuperAdmin(user)) {
                all = regionRepository.findAll(PageRequest.of(page, size));
            } else {
                all = regionRepository.findAllByActiveIsTrue(PageRequest.of(page, size));
            }
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeRegionForGet).collect(Collectors.toList())
                    ));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }


    public RegionDto makeRegionForGet(Region region) {
        return new RegionDto(
                region.getId(),
                region.getName(),
                region.getDescription(),
                region.isActive(),
                region.getRegion() != null ? region.getRegion().getId() : null,
                region.getRegion()
        );
    }

    public Region makeRegion(RegionDto regionDto, Region parentRegion) {
        return regionRepository.save(new Region(
                regionDto.getName(),
                regionDto.getDescription(),
                regionDto.isActive(),
                parentRegion
        ));
    }
}


