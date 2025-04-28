package on.ssgdeal.common.mdc;

import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import org.slf4j.MDC;

public class PassportMdcContext implements AutoCloseable {

    private final PassportUtil passportUtil;

    public PassportMdcContext(PassportUtil passportUtil, String passportId) {
        this.passportUtil = passportUtil;
        inject(passportId);
    }

    private void inject(String passportId) {
        MDC.clear();

        Passport passport = passportUtil.getPassportBy(passportId);

        MDC.put(MdcKey.PASSPORT_ID.getKey(), passportId);
        MDC.put(MdcKey.USER_ID.getKey(), String.valueOf(passport.getUserId()));
        MDC.put(MdcKey.USERNAME.getKey(), passport.getUsername());
        MDC.put(MdcKey.ROLE.getKey(), passport.getRole().toString());
        MDC.put(MdcKey.NICKNAME.getKey(), passport.getNickname());
        MDC.put(MdcKey.SLACK_EMAIL.getKey(), passport.getSlackEmail());
    }

    @Override
    public void close() throws Exception {
        MDC.remove(MdcKey.USER_ID.getKey());
        MDC.remove(MdcKey.PASSPORT_ID.getKey());
        MDC.remove(MdcKey.USERNAME.getKey());
        MDC.remove(MdcKey.ROLE.getKey());
        MDC.remove(MdcKey.NICKNAME.getKey());
        MDC.remove(MdcKey.SLACK_EMAIL.getKey());
    }
}
