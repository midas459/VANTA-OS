package dev.vanta

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkspaceQuotaTest {
    @Test fun allowsWriteWithinLogicalQuotaAndRealCapacity() {
        assertTrue(WorkspaceQuota.canAcceptWrite(10, 100, WorkspaceQuota.RESERVE_BYTES + 100))
    }

    @Test fun rejectsWriteBeyondTwoHundredGiBQuota() {
        assertFalse(WorkspaceQuota.canAcceptWrite(WorkspaceQuota.TARGET_BYTES - 10, 11,
            WorkspaceQuota.TARGET_BYTES))
    }

    @Test fun preservesDeviceHeadroom() {
        assertFalse(WorkspaceQuota.canAcceptWrite(0, 2 * WorkspaceQuota.GIB,
            2 * WorkspaceQuota.GIB))
    }

    @Test fun rejectsNegativeOrInvalidSizes() {
        assertFalse(WorkspaceQuota.canAcceptWrite(-1, 1, 10))
        assertFalse(WorkspaceQuota.canAcceptWrite(0, -1, 10))
        assertFalse(WorkspaceQuota.canAcceptWrite(0, 1, -1))
    }

    @Test fun rejectsWhenDeviceDoesNotHaveEnoughSpace() {
        assertFalse(WorkspaceQuota.canAcceptWrite(0, 10_000, 9_000, 0))
    }
}
