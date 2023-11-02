package dev.solanium.solaniumtowny;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DatabaseObject {

    private boolean changed = true;
}
