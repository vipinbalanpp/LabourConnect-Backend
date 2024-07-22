package com.example.user.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class WorkerResponse {
    private List<WorkerResponseDto> workers;
    private Integer totalNumberOfPages;
}
