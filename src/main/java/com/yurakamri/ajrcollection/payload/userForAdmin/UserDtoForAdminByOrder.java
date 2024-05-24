package com.yurakamri.ajrcollection.payload.userForAdmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoForAdminByOrder {

    private UUID id;

    private String firstName;

    private String lastName;

    private boolean active;

    private UUID attachmentId;

    private List<OrderByUserId> orderByUserIds;
}
