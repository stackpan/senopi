package com.ivanzkyanto.stolia.entity.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PlanForm {

    @NonNull
    private String groupId;

    @NonNull
    private String todo;

    private String description;

}
