package github.io.truongbn.distributedcache.model;

import lombok.Data;

@Data
public class FallbackDTO implements ItemType {
    private final String msg;
}
