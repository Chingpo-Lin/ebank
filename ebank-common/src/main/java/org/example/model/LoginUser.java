package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

    /**
     * key
     */
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     * mail
     */
    private String mail;
}
