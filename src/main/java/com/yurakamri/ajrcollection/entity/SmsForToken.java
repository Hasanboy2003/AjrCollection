package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SmsForToken extends AbsLongEntity {

    @NotNull(message = "email bo`sh bo`lishi mumkin emas")
    private String email="search@eskiz.uz";

    @NotNull(message = "parol bo`sh bo`lishi mumkin emas")
    private String password="j6DWtQjjpLDNjWEk74Sx";

    @Type(type = "text")
    private String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjUsInJvbGUiOiJ1c2VyIiwiZGF0YSI6eyJpZCI6NSwibmFtZSI6Ilx1MDQyN1x1MDQxZiBCZXN0IEludGVybmV0IFNvbHV0aW9uIiwiZW1haWwiOiJ0ZXN0QGVza2l6LnV6Iiwicm9sZSI6InVzZXIiLCJhcGlfdG9rZW4iOm51bGwsInN0YXR1cyI6ImFjdGl2ZSIsInNtc19hcGlfbG9naW4iOiJlc2tpejIiLCJzbXNfYXBpX3Bhc3N3b3JkIjoiZSQkayF6IiwidXpfcHJpY2UiOjUwLCJiYWxhbmNlIjotMTkwMCwiaXNfdmlwIjowLCJob3N0Ijoic2VydmVyMSIsImNyZWF0ZWRfYXQiOm51bGwsInVwZGF0ZWRfYXQiOiIyMDIyLTA1LTIwVDIxOjIwOjE4LjAwMDAwMFoifSwiaWF0IjoxNjUzMTEwMDE0LCJleHAiOjE2NTU3MDIwMTR9.DywblHfP5FOdD9q2Wn6ktJFaFr28ed5ab8t4J-59WDg";

    private String url="https://notify.eskiz.uz/api/auth/login";
}
