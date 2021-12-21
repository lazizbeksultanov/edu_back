package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.PayType;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.PayTypeDto;
import uz.gvs.admin_crm.repository.PayTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PayTypeService {
    @Autowired
    PayTypeRepository payTypeRepository;
    @Autowired
    ApiResponseService apiResponseService;

    public ApiResponse save(PayTypeDto payTypeDto) {
        try {
            if (!(payTypeDto.getName().replaceAll(" ", "").length() > 1))
                return apiResponseService.notEnoughErrorResponse();
            PayType payType = new PayType();
            // edit bo'lsa
            if (payTypeDto.getId() != null) {
                if (payTypeRepository.existsByNameEqualsIgnoreCaseAndIdNot(payTypeDto.getName(), payTypeDto.getId()))
                    return apiResponseService.existResponse();
                payType = payTypeRepository.findById(payTypeDto.getId()).orElseThrow(() -> new ResourceNotFoundException("get payType"));
            } else {
                // yangi qo'shilsa bo'lsa
                if (payTypeRepository.existsByNameEqualsIgnoreCase(payTypeDto.getName()))
                    return apiResponseService.existResponse();
            }
            payType.setName(payTypeDto.getName());
            payType.setActive(payTypeDto.isActive());
            payTypeRepository.save(payType);
            return apiResponseService.saveResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }

    }

    public ApiResponse getPayTypeList() {
        try {
            List<PayType> allPayTypes = payTypeRepository.findAll();
            if (allPayTypes.isEmpty()) return apiResponseService.notFoundResponse();
            return apiResponseService.getResponse(allPayTypes);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse getPayType(Integer id) {
        try {
            Optional<PayType> payType = payTypeRepository.findById(id);
            if (payType.isEmpty()) return apiResponseService.notFoundResponse();
            return apiResponseService.getResponse(payType);
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse deletePayType(Integer id) {
        try{
            if (payTypeRepository.findById(id).isEmpty()) return apiResponseService.notFoundResponse();
            payTypeRepository.deleteById(id);
            return apiResponseService.deleteResponse();
        }catch (Exception e){
            return apiResponseService.tryErrorResponse();
        }
    }

}
