package com.epam.alextuleninov.taxiservice.config.pagination;

import com.epam.alextuleninov.taxiservice.Constants;
import jakarta.servlet.http.HttpServletRequest;

import static com.epam.alextuleninov.taxiservice.Constants.*;

/**
 * To customize the pagination.
 */
public class PaginationConfig {

    // how many links are displayed starting from the very first one (cannot be set to 0)
    private static final int N_PAGES_FIRST = 1;
    // how many links are displayed to the left of the current one (can be set to 0)
    private static final int N_PAGES_PREV = 1;
    // how many links are displayed to the right of the current one (can be set to 0)
    private static final int N_PAGES_NEXT = 1;
    // how many links appear at the end of the page list (cannot be set to 0)
    private static final int N_PAGES_LAST = 1;

    /**
     * To customize the pagination.
     *
     * @param req HttpServletRequest request
     */
    public int config(HttpServletRequest req) {
        String pageFromRequest = req.getParameter(SCOPE_PAGE);
        int currentPage;
        /*
         * if the page is loaded for the first time or the user has entered an incorrect page number in the address bar
         * */
        if (pageFromRequest == null || !pageFromRequest.matches("\\d+")) {
            currentPage = 0;
        } else {
            currentPage = Integer.parseInt(pageFromRequest);
        }

        long totalRecords = (long) req.getAttribute(SCOPE_TOTAL_RECORDS);
        int pageSize = Constants.PAGE_SIZE;
        long pages = totalRecords / pageSize;
        int lastPage = (int) (pages * pageSize < totalRecords ? pages : pages - 1);
        // if customer haven`t any orders, because app get customer from DB, customer not really orders
        if (lastPage < 0) lastPage = 0;
        // if the user has entered an incorrect page number in the address bar
        if (lastPage < currentPage) currentPage = lastPage;
        req.setAttribute(SCOPE_CURRENT_PAGE, currentPage);
        req.setAttribute(SCOPE_LAST_PAGE, lastPage);

        // whether to show in full all links to pages to the left of the current one, or insert an ellipsis
        boolean showAllPrev = N_PAGES_FIRST >= (currentPage - N_PAGES_PREV);
        // whether to show in full all links to pages to the right of the current one, or insert an ellipsis
        boolean showAllNext = currentPage + N_PAGES_NEXT >= lastPage - N_PAGES_LAST;

        req.setAttribute("N_PAGES_FIRST", N_PAGES_FIRST);
        req.setAttribute("N_PAGES_PREV", N_PAGES_PREV);
        req.setAttribute("N_PAGES_NEXT", N_PAGES_NEXT);
        req.setAttribute("N_PAGES_LAST", N_PAGES_LAST);
        req.setAttribute("showAllPrev", showAllPrev);
        req.setAttribute("showAllNext", showAllNext);

        return currentPage;
    }
}
