package chat.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@AllArgsConstructor
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String body;

}
