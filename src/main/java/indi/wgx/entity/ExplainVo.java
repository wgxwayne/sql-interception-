package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExplainVo {
    private String id;
    private String select_type;
    private String table;
    private String partitions;
    private String type;
    private String possible_keys;
    private String key;
    private String key_len;
    private String ref;
    private String rows;
    private String filtered;
    private String Extra;
}
