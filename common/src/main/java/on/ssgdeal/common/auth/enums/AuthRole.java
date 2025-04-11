package on.ssgdeal.common.auth.enums;

public enum AuthRole {
    MASTER,
    COMPANY_MANAGER,
    CONSUMER,
    ;

    public boolean isMaster() {
        return this.equals(MASTER);
    }

    public boolean isCompanyManager() {
        return this.equals(COMPANY_MANAGER);
    }

    public boolean isConsumer() {
        return this.equals(CONSUMER);
    }
}
