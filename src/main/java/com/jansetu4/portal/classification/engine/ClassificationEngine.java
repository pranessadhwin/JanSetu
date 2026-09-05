package com.jansetu4.portal.classification.engine;

import com.jansetu4.portal.citizen.entity.Challenge;

public interface ClassificationEngine {

    ClassificationResult classify(Challenge challenge);
}
