package github.io.truongbn.distributedcache.model;

import lombok.Data;

@Data
public class ItemDTO implements ItemType {
    private final long id;
    private final String name;
    private final double price;
}
