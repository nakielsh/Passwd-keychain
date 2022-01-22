package pw.edu.pl.passwdkeychain.security.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginAttemptsLogger {

    @EventListener
    public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {

        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();

        WebAuthenticationDetails details =
                (WebAuthenticationDetails) auditEvent.getData().get("details");

        log.warn("Principal " + auditEvent.getPrincipal() + " - " + auditEvent.getType());
        log.warn("  Remote IP address: " + details.getRemoteAddress());
        log.warn("  Session Id: " + details.getSessionId());
    }

    @Bean
    public InMemoryAuditEventRepository auditEventRepository() {
        return new InMemoryAuditEventRepository();
    }
}
