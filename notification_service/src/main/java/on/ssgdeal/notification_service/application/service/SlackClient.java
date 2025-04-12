package on.ssgdeal.notification_service.application.service;

public interface SlackClient {
    String getSlackIdByEmail(String email);
    String getChannelIdByUserId(String userId);
    String sendNotificationToUser(String email, String content);
}
