package on.ssgdeal.common.auth.enums;

public enum AuthRole {
    MASTER,
    HUB_MANAGER,
    DELIVERY_MANAGER,
    COMPANY_MANAGER,
    ;

    public static boolean isAllowedSearchingOtherUserOrders(AuthRole role) {
        return role != null
            && (role.equals(MASTER) || role.equals(HUB_MANAGER) || role.equals(DELIVERY_MANAGER));
    }

    public boolean isHubManager() {
        return this.equals(HUB_MANAGER);
    }

    public boolean isCompanyManager() {
        return this.equals(COMPANY_MANAGER);
    }

    public boolean isDeliveryManager() {
        return this.equals(DELIVERY_MANAGER);
    }
}
