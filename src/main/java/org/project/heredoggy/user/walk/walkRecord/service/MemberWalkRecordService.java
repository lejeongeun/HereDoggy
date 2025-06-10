package org.project.heredoggy.user.walk.walkRecord.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWalkRecordService {
    private final WalkRecordRepository walkRecordRepository;
}
