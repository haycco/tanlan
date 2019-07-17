package org.haycco.tanlan.common.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author haycco
 */
@Builder
@Data
public class IgnorePath {

    private String methods;
    private String path;

}
