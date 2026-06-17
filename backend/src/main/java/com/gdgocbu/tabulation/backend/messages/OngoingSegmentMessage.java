package com.gdgocbu.tabulation.backend.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class OngoingSegmentMessage {
    private UUID id;
}
