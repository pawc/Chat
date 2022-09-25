package pl.pawc.chat.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class PrivateMessage implements Serializable {

    private String sender;
    private String recipient;
    private String message;

}