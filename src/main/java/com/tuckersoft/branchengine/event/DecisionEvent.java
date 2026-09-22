package com.tuckersoft.branchengine.event;
import com.tuckersoft.branchengine.model.BranchType;
import com.tuckersoft.branchengine.model.ImpactType;
public record DecisionEvent(Long decisionId, Long userId, String nodeKey,
                            BranchType branchType, ImpactType impactType, String impactLabel) {}
