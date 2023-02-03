package com.epam.alextuleninov.taxiservice.data.pageable;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;

/**
 * Record for the PageableRequest.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public record PageableRequest(

        String sortField,
        String orderBy,
        long limit,
        long offset
) {

    /**
     * Create the new record from page.
     *
     * @param page          page from request
     * @return              new record from page
     */
    public static PageableRequest getPageableRequest(int page) {
        return new PageableRequest(
                DataSourceFields.ORDER_ID,
                Constants.SORTING_ASC,
                Constants.PAGE_SIZE,
                (long) (page + 1) * Constants.PAGE_SIZE - Constants.PAGE_SIZE
        );
    }
}
