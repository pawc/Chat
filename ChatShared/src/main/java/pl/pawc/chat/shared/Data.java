package pl.pawc.chat.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Data implements Serializable {

    String command;
    Object arguments;

}