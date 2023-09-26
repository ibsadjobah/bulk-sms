package com.ibsadjobah.bulksms.bulksms.model.responses;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
public class CampagneResponse {

    private Long id;
    private String ref;
    private String message;
    private String type;
    private LocalDateTime schedule_at;
}
