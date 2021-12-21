package uz.gvs.admin_crm.service;

import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.utils.MessageConst;

@Service
public class ApiResponseService {
    public ApiResponse saveResponse() {
        return new ApiResponse(MessageConst.SAVED_MESSAGE, true);
    }

    public ApiResponse existResponse() {
        return new ApiResponse(MessageConst.EXISTS_MESSAGE, false);
    }

    public ApiResponse getResponse(Object object) {
        return new ApiResponse(MessageConst.GET_SUCCESS, true, object);
    }

    public ApiResponse notFoundResponse() {
        return new ApiResponse(MessageConst.NOT_FOUND_MESSAGE, false);
    }

    public ApiResponse deleteResponse() {
        return new ApiResponse(MessageConst.DELETED_MESSAGE, true);
    }

    public ApiResponse updatedResponse() {
        return new ApiResponse(MessageConst.UPDATED_MESSAGE, true);
    }

    public ApiResponse errorResponse() {
        return new ApiResponse(MessageConst.ERROR_MESSAGE, false);
    }

    public ApiResponse tryErrorResponse() {
        return new ApiResponse(MessageConst.TRY_ERROR_MESSAGE, false);
    }

    public ApiResponse notEnoughErrorResponse() {
        return new ApiResponse(MessageConst.ENOUGH_ERROR_MESSAGE, false);
    }

}
