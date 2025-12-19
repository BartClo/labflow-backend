package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Representa la estructura de respuesta paginada devuelta por los endpoints de muestras.
 * Se usa únicamente para documentar correctamente la salida en OpenAPI/Swagger.
 */
public class MuestraPageResponse {

    @Schema(description = "Listado de muestras de la página solicitada")
    private List<MuestraDTO> content;

    @Schema(description = "Información de paginación")
    private PageableInfo pageable;

    @Schema(description = "Total de páginas disponibles")
    private Integer totalPages;

    @Schema(description = "Total de elementos en la consulta")
    private Long totalElements;

    @Schema(description = "Indica si esta es la última página")
    private Boolean last;

    @Schema(description = "Cantidad de elementos por página")
    private Integer size;

    @Schema(description = "Número de la página actual (0-indexed)")
    private Integer number;

    @Schema(description = "Información de ordenamiento")
    private SortInfo sort;

    @Schema(description = "Cantidad de elementos en la página actual")
    private Integer numberOfElements;

    @Schema(description = "Indica si esta es la primera página")
    private Boolean first;

    @Schema(description = "Indica si la página está vacía")
    private Boolean empty;

    public List<MuestraDTO> getContent() {
        return content;
    }

    public void setContent(List<MuestraDTO> content) {
        this.content = content;
    }

    public PageableInfo getPageable() {
        return pageable;
    }

    public void setPageable(PageableInfo pageable) {
        this.pageable = pageable;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public SortInfo getSort() {
        return sort;
    }

    public void setSort(SortInfo sort) {
        this.sort = sort;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    /**
     * Representa la sección pageable del objeto Page devuelto por Spring Data.
     */
    public static class PageableInfo {
        @Schema(description = "Información de ordenamiento aplicada")
        private SortInfo sort;

        @Schema(description = "Número de página (0-indexed)")
        private Integer pageNumber;

        @Schema(description = "Cantidad de elementos por página")
        private Integer pageSize;

        @Schema(description = "Desplazamiento calculado")
        private Long offset;

        @Schema(description = "Indica si está paginado")
        private Boolean paged;

        @Schema(description = "Indica si no está paginado")
        private Boolean unpaged;

        public SortInfo getSort() {
            return sort;
        }

        public void setSort(SortInfo sort) {
            this.sort = sort;
        }

        public Integer getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Long getOffset() {
            return offset;
        }

        public void setOffset(Long offset) {
            this.offset = offset;
        }

        public Boolean getPaged() {
            return paged;
        }

        public void setPaged(Boolean paged) {
            this.paged = paged;
        }

        public Boolean getUnpaged() {
            return unpaged;
        }

        public void setUnpaged(Boolean unpaged) {
            this.unpaged = unpaged;
        }
    }

    /**
     * Representa la sección sort del objeto Page devuelto por Spring Data.
     */
    public static class SortInfo {
        @Schema(description = "Indica si no hay ordenamiento")
        private Boolean empty;

        @Schema(description = "Indica si hay ordenamiento aplicado")
        private Boolean sorted;

        @Schema(description = "Indica si está desordenado")
        private Boolean unsorted;

        public Boolean getEmpty() {
            return empty;
        }

        public void setEmpty(Boolean empty) {
            this.empty = empty;
        }

        public Boolean getSorted() {
            return sorted;
        }

        public void setSorted(Boolean sorted) {
            this.sorted = sorted;
        }

        public Boolean getUnsorted() {
            return unsorted;
        }

        public void setUnsorted(Boolean unsorted) {
            this.unsorted = unsorted;
        }
    }
}
