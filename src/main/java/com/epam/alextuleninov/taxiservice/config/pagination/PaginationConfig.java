package com.epam.alextuleninov.taxiservice.config.pagination;

import com.epam.alextuleninov.taxiservice.Constants;
import jakarta.servlet.http.HttpServletRequest;

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

    public PaginationConfig() {
    }

    /**
     * To customize the current page.
     *
     * @param req           HttpServletRequest request
     * @return              number current page
     */
    public int configPage(HttpServletRequest req) {
        String pageFromRequest = req.getParameter("page");
        int page;
        if (pageFromRequest == null) {
            page = 0;
        } else {
            page = Integer.parseInt(pageFromRequest);
        }
        req.setAttribute("current_page", page);
        return page;
    }

    /**
     * To customize the pagination.
     *
     * @param req           HttpServletRequest request
     */
    public void config(HttpServletRequest req) {
        long totalRecords = (long) req.getAttribute("total_records");
        int pageSize = Constants.PAGE_SIZE;
        int currentPage = (Integer) req.getAttribute("current_page");
        long pages = totalRecords / pageSize;
        int lastPage = (int) (pages * pageSize < totalRecords ? pages : pages - 1);
        req.setAttribute("last_page", lastPage);

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
        req.setAttribute("url", "report");
    }
}
