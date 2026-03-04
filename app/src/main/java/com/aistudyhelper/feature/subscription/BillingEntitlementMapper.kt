package com.aistudyhelper.feature.subscription

import com.aistudyhelper.domain.Entitlement
import com.aistudyhelper.domain.PlanType

object BillingEntitlementMapper {
    private const val PREMIUM_PRODUCT_ID = "ai_study_helper_premium_monthly"

    fun fromPurchases(activeProductIds: Set<String>, expiryAt: Long?): Entitlement {
        val isPremium = activeProductIds.contains(PREMIUM_PRODUCT_ID)
        return Entitlement(
            planType = if (isPremium) PlanType.PREMIUM else PlanType.FREE,
            expiresAtEpochMillis = expiryAt,
        )
    }
}
