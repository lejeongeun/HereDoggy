package org.project.heredoggy.systemAdmin.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.systemAdmin.service.AdminShelterRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminShelterRequestController {
    private final AdminShelterRequestService requestService;

}
