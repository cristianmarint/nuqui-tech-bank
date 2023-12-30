package co.nuqui.tech.msusers.domain.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HumanEvent extends ApplicationEvent {

    private final Human human;

    public HumanEvent(Object source, Human human) {
        super(source);
        this.human = human;
    }

}
