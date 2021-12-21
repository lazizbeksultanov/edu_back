package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Cashback;
import uz.gvs.admin_crm.entity.Region;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.CashbackDto;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.repository.CashbackRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CashbackService {
    @Autowired
    CashbackRepository cashbackRepository;
    @Autowired
    ApiResponseService apiResponseService;

    public ApiResponse saveCashback(CashbackDto cashbackDto) {
        try {
            if (cashbackDto.getPercent() >= 0 && cashbackDto.getPrice() >= 0) {
                if (!cashbackRepository.existsByPriceEquals(cashbackDto.getPrice())) {
                    cashbackRepository.save(new Cashback(
                            cashbackDto.getPrice(),
                            cashbackDto.getPercent(),
                            cashbackDto.isActive()
                    ));
                    return apiResponseService.saveResponse();
                }
                return apiResponseService.existResponse();
            }
            return apiResponseService.notEnoughErrorResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editCashback(Integer id, CashbackDto cashbackDto) {
        try {
            Optional<Cashback> optional = cashbackRepository.findById(id);
            if (optional.isPresent()) {
                if (cashbackDto.getPercent() >= 0 && cashbackDto.getPrice() >= 0) {
                    if (!cashbackRepository.existsByPriceEqualsAndIdNot(cashbackDto.getPrice(), id)) {
                        Cashback cashback = optional.get();
                        cashback.setPrice(cashbackDto.getPrice());
                        cashback.setPercent(cashbackDto.getPercent());
                        cashback.setActive(cashbackDto.isActive());
                        cashbackRepository.save(cashback);
                        return apiResponseService.updatedResponse();
                    }
                    return apiResponseService.existResponse();
                }
                return apiResponseService.notEnoughErrorResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getCashbackList(int page, int size, User user) {
        try {
            Page<Cashback> all = null;
            all = cashbackRepository.findAll(PageRequest.of(page, size));
            return apiResponseService.getResponse(
                    new PageableDto(
                            all.getTotalPages(),
                            all.getTotalElements(),
                            all.getNumber(),
                            all.getSize(),
                            all.get().map(this::makeCashbackDto).collect(Collectors.toList())
                    ));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public CashbackDto makeCashbackDto(Cashback cashback) {
        return new CashbackDto(
                cashback.getId(),
                cashback.getPrice(),
                cashback.getPercent(),
                cashback.isActive()
        );
    }

    public ApiResponse getCashback(Integer id) {
        try {
            Optional<Cashback> optional = cashbackRepository.findById(id);
            if (optional.isPresent()) {
                return apiResponseService.getResponse(makeCashbackDto(optional.get()));
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
