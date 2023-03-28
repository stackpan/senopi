package com.ivanzkyanto.stolia.entity.forms;

import lombok.*;

@Data
@NoArgsConstructor
public class PlanGroupForm {

    @NonNull
    private String name;

    private String description;

}
