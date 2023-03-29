package com.ivanzkyanto.stolia.controller;

import com.ivanzkyanto.stolia.entity.Plan;
import com.ivanzkyanto.stolia.entity.forms.PlanForm;
import com.ivanzkyanto.stolia.service.PlanGroupService;
import com.ivanzkyanto.stolia.service.PlanService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PlanController {

    @NonNull
    private PlanService service;

    @NonNull
    private PlanGroupService groupService;

    @GetMapping("/plans")
    public String showPlansPage(Model model, @RequestParam("group") String groupId) {
        var plans = service.getAllByGroupId(groupId);
        var planForm = new PlanForm();
        planForm.setGroupId(groupId);

        model.addAttribute("plans", plans);
        model.addAttribute("planForm", planForm);
        return "plans";
    }

    @PostMapping("/plan/create")
    public String createPlan(@ModelAttribute PlanForm planForm) {
        var plan = Plan.builder()
                .todo(planForm.getTodo())
                .description(planForm.getDescription())
                .group(groupService.getById(planForm.getGroupId()))
                .build();
        service.createNew(plan);
        return "redirect:/plans?group=" + planForm.getGroupId();
    }

}
