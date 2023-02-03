package com.epam.alextuleninov.taxiservice.model.route.adress;

/**
 * Record for Address entity.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public record Address(

        long id,
        String startEnd,
        String startEndUk
) {
}
