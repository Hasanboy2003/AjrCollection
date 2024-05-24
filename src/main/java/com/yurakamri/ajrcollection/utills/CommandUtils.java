package com.yurakamri.ajrcollection.utills;

import com.yurakamri.ajrcollection.exception.PageSizeException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CommandUtils {

    public static void validatePageAndSize(int page, int size) throws PageSizeException {
        if (page < 0) {
            throw new PageSizeException("Invalid Page number. Page number must not be less than zero!");
        } else if (size < 1) {
            throw new PageSizeException("Invalid Page size. Page size must not be less than one!");
        } else if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new PageSizeException("Invalid Page size. Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE + "!");
        }
    }

    public static Pageable simplePageable(int page, int size, Sort.Direction direction, String... sortProperties) throws PageSizeException {
        validatePageAndSize(page, size);
        return PageRequest.of(page, size, Sort.by(direction, sortProperties));
    }

    public static Pageable descOrAscByCreatedAtPageable(int page, int size, boolean asc) throws PageSizeException {
        validatePageAndSize(page, size);
        return PageRequest.of(page, size, asc ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt");
    }
}
