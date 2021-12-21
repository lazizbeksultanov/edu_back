package uz.gvs.admin_crm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalElements;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer number;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer size;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object object;

    public PageableDto(Long totalElements, Integer number, Integer size, Object object) {
        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
        this.object = object;
    }
}
