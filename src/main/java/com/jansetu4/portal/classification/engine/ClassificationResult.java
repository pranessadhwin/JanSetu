package com.jansetu4.portal.classification.engine;

import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResult {

    private Domain domain;
    private Double confidenceScore;
    private ClassificationMethod method;
}
