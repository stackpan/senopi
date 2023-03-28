package com.ivanzkyanto.stolia.controller;

import com.ivanzkyanto.stolia.service.PlanService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PlanController {

    @NonNull
    private PlanService service;

    @GetMapping("/plans")
    public String showPlansPage(Model model, @RequestParam("group") String groupId) {
        var plans = service.getAllByGroupId(groupId);
        model.addAttribute("plans", plans);

        return "plans";
    }

}
