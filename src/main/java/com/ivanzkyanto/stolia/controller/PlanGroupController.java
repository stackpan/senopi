package com.ivanzkyanto.stolia.controller;

import com.ivanzkyanto.stolia.entity.PlanGroup;
import com.ivanzkyanto.stolia.entity.forms.PlanGroupForm;
import com.ivanzkyanto.stolia.service.PlanGroupService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PlanGroupController {

    @NonNull
    private PlanGroupService service;

    @GetMapping("/groups")
    public String showGroupsPage(Model model) {
        var groups = service.getAll();
        model.addAttribute("groups", groups);
        model.addAttribute("groupForm", new PlanGroupForm());
        return "groups";
    }

    @PostMapping("/group/create")
    public String createGroup(@ModelAttribute PlanGroupForm groupForm) {
        var group = PlanGroup.builder()
                .name(groupForm.getName())
                .description(groupForm.getDescription())
                .build();
        service.createNew(group);
        return "redirect:/groups";
    }

}
