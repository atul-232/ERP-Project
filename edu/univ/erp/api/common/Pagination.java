package edu.univ.erp.api.common;

public class Pagination {
    private final int page;
    private final int pageSize;
    private final int totalItems;
    private final int totalPages;

    public Pagination(int page, int pageSize, int totalItems) {
        System.out.println("[DEBUG] Pagination constructor called -> page=" + page + ", pageSize=" + pageSize + ", totalItems=" + totalItems);
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
        System.out.println("[DEBUG] Pagination created -> totalPages=" + this.totalPages);
    }

    public int getPage() {
        System.out.println("[DEBUG] getPage -> " + page);
        return page;
    }

    public int getPageSize() {
        System.out.println("[DEBUG] getPageSize -> " + pageSize);
        return pageSize;
    }

    public int getTotalItems() {
        System.out.println("[DEBUG] getTotalItems -> " + totalItems);
        return totalItems;
    }

    public int getTotalPages() {
        System.out.println("[DEBUG] getTotalPages -> " + totalPages);
        return totalPages;
    }

    public int getOffset() {
        int offset = (page - 1) * pageSize;
        System.out.println("[DEBUG] getOffset -> " + offset);
        return offset;
    }

    public boolean hasNext() {
        boolean result = page < totalPages;
        System.out.println("[DEBUG] hasNext -> " + result);
        return result;
    }

    public boolean hasPrevious() {
        boolean result = page > 1;
        System.out.println("[DEBUG] hasPrevious -> " + result);
        return result;
    }
}
